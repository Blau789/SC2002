package SC2002;

import SC2002.Action.BasicAttack;
import SC2002.Action.Defend;
import SC2002.Action.SpecialSkill;
import SC2002.entity.combatant.Player;
import SC2002.entity.combatant.Warrior;
import SC2002.entity.combatant.Wizard;
import SC2002.entity.combatant.Enemy;
import SC2002.entity.combatant.Combatant;
import SC2002.level.Level;
import SC2002.level.LevelFactory;
import SC2002.strategy.SpeedBasedTurnOrder;
import SC2002.strategy.TurnOrderStrategy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class GameMain {
    public static void main(String[] args) {
        System.out.println("=== Welcome to RPG Combat Game ===\n");

        // Select player class
        Scanner scanner = new Scanner(System.in);
        Player player = selectPlayerClass(scanner);

        // Select difficulty
        System.out.print("Select difficulty (1=Easy, 2=Medium, 3=Hard): ");
        int difficulty = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Level level = LevelFactory.createLevel(difficulty);
        System.out.println("\nStarting: " + level + "\n");

        // Start combat
        List<Enemy> enemies = level.getInitialEnemies();
        playLevel(player, enemies, level.getBackupEnemies(), scanner);

        scanner.close();
    }

    private static Player selectPlayerClass(Scanner scanner) {
        System.out.println("Select your class:");
        System.out.println("1. Warrior (High HP & Defense)");
        System.out.println("2. Wizard (High Attack & Speed)");
        System.out.print("Choice (1-2): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        if (choice == 1) {
            return new Warrior(name);
        } else {
            return new Wizard(name);
        }
    }

    private static void playLevel(Player player, List<Enemy> enemies,
                                  List<Enemy> backupEnemies, Scanner scanner) {
        List<Enemy> currentEnemies = new ArrayList<>(enemies);
        TurnOrderStrategy turnOrder = new SpeedBasedTurnOrder();

        BasicAttack basicAttack = new BasicAttack();
        Defend defend = new Defend();

        while (player.isAlive() && !currentEnemies.isEmpty()) {
            // Tick & remove effects at start of round
            tickStatusEffects(player);
            for (Enemy enemy : currentEnemies) {
                tickStatusEffects(enemy);
            }

            // Display status
            displayRound(player, currentEnemies);

            // Get turn order - combine player and enemies into one list
            List<Combatant> allCombatants = new ArrayList<>();
            allCombatants.add(player);
            allCombatants.addAll(currentEnemies);
            List<Combatant> turnSequence = turnOrder.determineTurnOrder(allCombatants);

            // Execute turns
            for (Combatant actor : turnSequence) {
                if (!player.isAlive()) {
                    break;
                }

                if (actor == null || !actor.isAlive()) {
                    continue;
                }

                // Stun (or other effects) block turns here
                if (!actor.canTakeTurn()) {
                    System.out.println("\n" + actor.getName() + " is unable to act this turn!");
                    // Special skill cooldown should still reduce only on an ACTIVE turn.
                    if (actor instanceof Player) {
                        reduceSpecialCooldown((Player) actor);
                    }
                    continue;
                }

                if (actor instanceof Player) {
                    reduceSpecialCooldown((Player) actor);
                    playerTurn(player, currentEnemies, scanner, basicAttack, defend);
                } else if (actor instanceof Enemy) {
                    Enemy enemy = (Enemy) actor;
                    if (enemy.isAlive()) {
                        enemyTurn(enemy, player);
                    }
                }

                // After each action, clear dead enemies so player can’t target them.
                currentEnemies.removeIf(e -> !e.isAlive());
                if (currentEnemies.isEmpty()) {
                    break;
                }
            }

            // Check if player is defeated
            if (!player.isAlive()) {
                System.out.println("\n*** YOU HAVE BEEN DEFEATED! ***");
                return;
            }

            // Spawn backup enemies if all current enemies defeated
            if (currentEnemies.isEmpty() && !backupEnemies.isEmpty()) {
                System.out.println("\n*** Backup enemies appear! ***\n");
                currentEnemies.addAll(backupEnemies);
                backupEnemies.clear();
            }
        }

        System.out.println("\n*** VICTORY! ***");
        System.out.println("You defeated all enemies!");
        System.out.println("Remaining HP: " + player.getHp() + "/" + player.getMaxHp());
    }

    private static void tickStatusEffects(Combatant combatant) {
        for (var effect : new ArrayList<>(combatant.getStatusEffects())) {
            effect.tick(combatant);
        }
        combatant.removeExpiredEffects();
    }

    private static void reduceSpecialCooldown(Player player) {
        if (player.getSpecialSkill() instanceof SpecialSkill) {
            ((SpecialSkill) player.getSpecialSkill()).reduceCooldown();
        }
    }

    private static void displayRound(Player player, List<Enemy> enemies) {
        System.out.println("\n--- Round ---");
        System.out.println(player.getName() + " (Player): HP " + player.getHp() + "/" + player.getMaxHp()
                + " | ATK " + player.getAttack() + " | DEF " + player.getDefense());
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isAlive()) {
                System.out.println((i + 1) + ") " + enemy.getName() + ": HP " + enemy.getHp() + "/" + enemy.getMaxHp());
            }
        }
    }

    private static void playerTurn(Player player, List<Enemy> enemies, Scanner scanner,
                                   BasicAttack basicAttack, Defend defend) {
        while (true) {
            System.out.println("\n" + player.getName() + "'s turn!");
            System.out.println("1. Basic Attack");
            System.out.println("2. Defend");
            System.out.println("3. Special Skill (" + player.getSpecialSkill().getActionName() + ")");
            System.out.print("Choose action (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                Combatant target = selectEnemyTarget(enemies, scanner);
                if (target == null) {
                    System.out.println("No valid target.");
                    continue;
                }
                System.out.println(basicAttack.execute(player, Collections.singletonList(target)));
                return;
            }

            if (choice == 2) {
                System.out.println(defend.execute(player, Collections.emptyList()));
                return;
            }

            if (choice == 3) {
                // Arcane Blast targets all enemies automatically.
                boolean isArcaneBlast = player.getSpecialSkill().getActionName().toLowerCase().contains("arcane blast");

                if (player.getSpecialSkill() instanceof SpecialSkill && ((SpecialSkill) player.getSpecialSkill()).isOnCooldown()) {
                    System.out.println(player.getSpecialSkill().execute(player, Collections.emptyList()));
                    System.out.println("Pick another action.");
                    continue; // must choose another action
                }

                if (isArcaneBlast) {
                    List<Combatant> targets = new ArrayList<>(enemies);
                    System.out.println(player.getSpecialSkill().execute(player, targets));
                    return;
                }

                // Single-target special (Shield Bash)
                Combatant target = selectEnemyTarget(enemies, scanner);
                if (target == null) {
                    System.out.println("No valid target.");
                    continue;
                }
                System.out.println(player.getSpecialSkill().execute(player, Collections.singletonList(target)));
                return;
            }

            System.out.println("Invalid choice. Try again.");
        }
    }

    private static Combatant selectEnemyTarget(List<Enemy> enemies, Scanner scanner) {
        if (enemies == null || enemies.isEmpty()) {
            return null;
        }

        while (true) {
            System.out.print("Select target (1-" + enemies.size() + "): ");
            int idx = scanner.nextInt();
            scanner.nextLine();

            int listIndex = idx - 1;
            if (listIndex < 0 || listIndex >= enemies.size()) {
                System.out.println("Invalid target.");
                continue;
            }

            Enemy target = enemies.get(listIndex);
            if (target == null || !target.isAlive()) {
                System.out.println("That target is already defeated.");
                continue;
            }

            return target;
        }
    }

    private static void enemyTurn(Enemy enemy, Player player) {
        System.out.println("\n" + enemy.getName() + "'s turn!");
        int damage = Math.max(0, enemy.getAttack() - player.getDefense());
        player.setHp(player.getHp() - damage);
        System.out.println(enemy.getName() + " attacks " + player.getName() + " for " + damage + " damage!");
    }
}
