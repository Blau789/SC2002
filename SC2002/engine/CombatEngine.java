package SC2002.engine;

import SC2002.Action.BasicAttack;
import SC2002.Action.Defend;
import SC2002.Action.SpecialSkill;
import SC2002.Action.UseItem;
import SC2002.Action.Actions;
import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.Enemy;
import SC2002.entity.combatant.Player;
import SC2002.entity.items.Item;
import SC2002.strategy.SpeedBasedTurnOrder;
import SC2002.strategy.TurnOrderStrategy;
import SC2002.ui.GameUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CombatEngine {
    private final TurnOrderStrategy turnOrder;

    public CombatEngine() {
        this.turnOrder = new SpeedBasedTurnOrder();
    }

    public static class BattleResult {
        public enum Outcome { VICTORY, DEFEAT }

        private final Outcome outcome;
        private final int totalRounds;
        private final List<Enemy> remainingEnemies;

        public BattleResult(Outcome outcome, int totalRounds, List<Enemy> remainingEnemies) {
            this.outcome = outcome;
            this.totalRounds = totalRounds;
            this.remainingEnemies = remainingEnemies;
        }

        public Outcome getOutcome() { return outcome; }

        public int getTotalRounds() { return totalRounds; }

        public List<Enemy> getRemainingEnemies() { return remainingEnemies; }
    }

    private List<Combatant> getOpponents(Combatant actor, List<Combatant> allCombatants) {
        List<Combatant> opponents = new ArrayList<>();
        if (actor == null || allCombatants == null) return opponents;

        for (Combatant c : allCombatants) {
            if (c == null || !c.isAlive()) continue;
            if (c.getFaction() != actor.getFaction()) {
                opponents.add(c);
            }
        }
        return opponents;
    }

    public BattleResult playLevel(Player player, List<Enemy> enemies, List<Enemy> backupEnemies, GameUI ui) {
        List<Enemy> currentEnemies = new ArrayList<>(enemies);
        int rounds = 0;

        BasicAttack basicAttack = new BasicAttack();
        Defend defend = new Defend();

        while (player.isAlive() && !currentEnemies.isEmpty()) {
            rounds++;

            tickStatusEffects(player);
            for (Enemy enemy : currentEnemies) {
                tickStatusEffects(enemy);
            }

            ui.showRoundHeader();
            ui.showPlayerStatus(player);
            ui.showEnemiesStatus(currentEnemies);

            List<Combatant> allCombatants = new ArrayList<>();
            allCombatants.add(player);
            allCombatants.addAll(currentEnemies);
            List<Combatant> turnSequence = turnOrder.determineTurnOrder(allCombatants);

            for (Combatant actor : turnSequence) {
                if (!player.isAlive()) break;
                if (actor == null || !actor.isAlive()) continue;

                if (!actor.canTakeTurn()) {
                    ui.showUnableToAct(actor);
                    if (actor instanceof Player p) {
                        reduceSpecialCooldown(p);
                    }
                    continue;
                }

                if (actor instanceof Player) {
                    Player p = (Player) actor;
                    reduceSpecialCooldown(p);
                    playerTurn(p, currentEnemies, ui, basicAttack, defend);
                } else if (actor instanceof Enemy) {
                    Enemy enemy = (Enemy) actor;
                    if (enemy.isAlive()) {
                        enemyTurn(enemy, allCombatants, ui);
                    }
                }

                currentEnemies.removeIf(e -> !e.isAlive());
                if (currentEnemies.isEmpty()) break;
            }

            if (!player.isAlive()) {
                return new BattleResult(BattleResult.Outcome.DEFEAT, rounds, new ArrayList<>(currentEnemies));
            }

            if (currentEnemies.isEmpty() && !backupEnemies.isEmpty()) {
                ui.showMessage("\n*** Backup enemies appear! ***\n");
                currentEnemies.addAll(backupEnemies);
                backupEnemies.clear();
            }
        }

        return new BattleResult(BattleResult.Outcome.VICTORY, rounds, new ArrayList<>());
    }

    private void tickStatusEffects(Combatant combatant) {
        for (var effect : new ArrayList<>(combatant.getStatusEffects())) {
            effect.tick(combatant);
        }
        combatant.removeExpiredEffects();
    }

    private void reduceSpecialCooldown(Player player) {
        if (player.getSpecialSkill() instanceof SpecialSkill) {
            ((SpecialSkill) player.getSpecialSkill()).reduceCooldown();
        }
    }

    private void playerTurn(Player player, List<Enemy> enemies, GameUI ui,
                            BasicAttack basicAttack, Defend defend) {
        while (true) {
            // Show item inventory (only chosen items are ever added to player).
            ui.showItemInventory(player);

            int choice = ui.promptPlayerAction(player);

            if (choice == 1) {
                Combatant target = selectEnemyTarget(enemies, ui);
                if (target == null) {
                    ui.showMessage("No valid target.");
                    continue;
                }
                ui.showMessage(basicAttack.execute(player, Collections.singletonList(target)));
                return;
            }

            if (choice == 2) {
                ui.showMessage(defend.execute(player, Collections.emptyList()));
                return;
            }

            if (choice == 3) {
                boolean isArcaneBlast = player.getSpecialSkill().getActionName().toLowerCase().contains("arcane blast");

                if (player.getSpecialSkill() instanceof SpecialSkill
                        && ((SpecialSkill) player.getSpecialSkill()).isOnCooldown()) {
                    ui.showMessage(player.getSpecialSkill().execute(player, Collections.emptyList()));
                    ui.showMessage("Pick another action.");
                    continue;
                }

                if (isArcaneBlast) {
                    List<Combatant> targets = new ArrayList<>(enemies);
                    ui.showMessage(player.getSpecialSkill().execute(player, targets));
                    return;
                }

                Combatant target = selectEnemyTarget(enemies, ui);
                if (target == null) {
                    ui.showMessage("No valid target.");
                    continue;
                }
                ui.showMessage(player.getSpecialSkill().execute(player, Collections.singletonList(target)));
                return;
            }

            if (choice == 4) {
                if (!player.hasItems()) {
                    ui.showMessage("No items available.");
                    continue;
                }

                Item selected = selectPlayerItem(player, ui);
                if (selected == null) {
                    ui.showMessage("Invalid item choice.");
                    continue;
                }

                List<Combatant> itemTargets = Collections.singletonList(player);

                // Power Stone should target the same way as the player's special skill.
                if (selected instanceof SC2002.entity.items.PowerStone) {
                    boolean isArcaneBlast = player.getSpecialSkill() != null
                            && player.getSpecialSkill().getActionName().toLowerCase().contains("arcane blast");

                    if (isArcaneBlast) {
                        itemTargets = new ArrayList<>(enemies);
                    } else {
                        Combatant target = selectEnemyTarget(enemies, ui);
                        if (target == null) {
                            ui.showMessage("No valid target.");
                            continue;
                        }
                        itemTargets = Collections.singletonList(target);
                    }
                }

                UseItem useItemAction = new UseItem(selected);
                String result = useItemAction.execute(player, itemTargets);
                ui.showMessage(result);

                player.removeItem(selected);
                return;
            }

            ui.showMessage("Invalid choice. Try again.");
        }
    }

    private Item selectPlayerItem(Player player, GameUI ui) {
        // Build a unique list for menu ordering.
        java.util.LinkedHashMap<String, Item> unique = new java.util.LinkedHashMap<>();
        for (Item item : player.getItems()) {
            if (item != null) unique.putIfAbsent(item.getName(), item);
        }
        if (unique.isEmpty()) return null;

        int idx = ui.promptItemChoice(player);
        if (idx < 0 || idx >= unique.size()) return null;

        return new java.util.ArrayList<>(unique.values()).get(idx);
    }

    private Combatant selectEnemyTarget(List<Enemy> enemies, GameUI ui) {
        if (enemies == null || enemies.isEmpty()) return null;

        while (true) {
            int listIndex = ui.promptEnemyTargetIndex(enemies);

            if (listIndex < 0 || listIndex >= enemies.size()) {
                ui.showMessage("Invalid target.");
                continue;
            }

            Enemy target = enemies.get(listIndex);
            if (target == null || !target.isAlive()) {
                ui.showMessage("That target is already defeated.");
                continue;
            }

            return target;
        }
    }

    private void enemyTurn(Enemy enemy, List<Combatant> allCombatants, GameUI ui) {
        ui.showMessage("\n" + enemy.getName() + "'s turn!");

        List<Combatant> opponents = getOpponents(enemy, allCombatants);
        if (opponents.isEmpty()) {
            return;
        }

        Actions action = enemy.decideAction(opponents);
        if (action == null) {
            ui.showMessage(enemy.getName() + " hesitates...");
            return;
        }

        Combatant target = enemy.selectTarget(opponents);
        if (target == null) {
            return;
        }

        // Ensure we use takeDamage (not setHp) so Smoke Bomb can reduce damage to 0.
        if (action instanceof BasicAttack) {
            int rawDamage = Math.max(0, enemy.getAttack() - target.getDefense());
            int before = target.getHp();
            target.takeDamage(rawDamage);
            int dealt = before - target.getHp();
            ui.showMessage(enemy.getName() + " attacks " + target.getName() + " for " + dealt + " damage!");
            return;
        }

        ui.showMessage(action.execute(enemy, java.util.Collections.singletonList(target)));
    }
}
