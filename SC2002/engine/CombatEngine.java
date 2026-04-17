package SC2002.engine;

import SC2002.Action.Actions;
import SC2002.Action.BasicAttack;
import SC2002.Action.Defend;
import SC2002.Action.SpecialSkill;
import SC2002.Action.TargetType;
import SC2002.Action.UseItem;
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
                    for (var effect : new ArrayList<>(actor.getStatusEffects())) {
                        effect.onTurnSkipped(actor);
                    }
                    actor.removeExpiredEffects();
                    
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
            if (effect.ticksOnRoundStart()) {
                effect.tick(combatant);
            }
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
            Actions chosenAction = null;
            Item selectedItem = null;
            if (choice == 1) {
                chosenAction = basicAttack;
            } else if (choice == 2) {
                chosenAction = defend;
            } else if (choice == 3) {
                if (player.getSpecialSkill() instanceof SpecialSkill
                        && ((SpecialSkill) player.getSpecialSkill()).isOnCooldown()) {
                    ui.showMessage(player.getSpecialSkill().execute(player, Collections.emptyList()));
                    ui.showMessage("Pick another action.");
                    continue;
                }
                chosenAction = player.getSpecialSkill();
            } else if (choice == 4) {
                if (!player.hasItems()) {
                    ui.showMessage("No items available.");
                    continue;
                }
                selectedItem = selectPlayerItem(player, ui);
                if (selectedItem == null) {
                    ui.showMessage("Invalid item choice.");
                    continue;
                }
                chosenAction = new UseItem(selectedItem);
            } else {
                ui.showMessage("Invalid choice. Try again.");
                continue;
            }
            TargetType type = chosenAction.getTargetType();
            List<Combatant> targets = new ArrayList<>();
            switch(type){
                case Single_enemy: 
                    Combatant target = selectEnemyTarget(enemies,ui);
                    if (target == null){
                        ui.showMessage("no Vaid target");
                        continue;
                    }
                    targets.add(target);
                    break; 
                case All_enemies:
                    targets.addAll(enemies);
                    break; 
                case Self: 
                case Single_ally: 
                    targets.add(player);
                    break;
                case Dependent:
                    TargetType DependentType = player.getSpecialSkill().getTargetType();
                    if (DependentType == TargetType.All_enemies){
                        targets.addAll(enemies);
                    }
                    else if (DependentType == TargetType.Single_enemy){
                        Combatant depTarget = selectEnemyTarget(enemies,ui);
                        if (depTarget == null){
                            ui.showMessage("no cvalid target");
                            continue;
                        }
                        targets.add(depTarget);
                        }
                        else{
                            targets.add(player);
                        }
                        break;
                    default: 
                    ui.showMessage("Action failed: Unknown Target type");
                    continue;
            }

            // Snapshot effects active before this action so freshly applied effects
            // are not consumed immediately on the same turn.
            List<SC2002.entity.combatant.statuseffects.StatusEffect> effectsBeforeAction =
                    new ArrayList<>(player.getStatusEffects());

            ui.showMessage(chosenAction.execute(player,targets));
            if (selectedItem != null){
                player.removeItem(selectedItem);
            }
            // Consume event-based owner-action timers only after a successful action.
            for (var effect : effectsBeforeAction) {
                if (player.getStatusEffects().contains(effect)) {
                    effect.onOwnerAction(player);
                }
            }
            player.removeExpiredEffects();
            return;
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

        ui.showMessage(action.execute(enemy, java.util.Collections.singletonList(target)));
    }
}
