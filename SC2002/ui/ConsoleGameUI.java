package SC2002.ui;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.Enemy;
import SC2002.entity.combatant.Player;
import SC2002.entity.items.Item;
import java.util.List;
import java.util.Scanner;

public class ConsoleGameUI implements GameUI {
    private final Scanner scanner;

    public ConsoleGameUI(Scanner scanner) {
        this.scanner = scanner;
    }

    private int readInt(int min, int max) {
        while (true) {
            try {
                int val = scanner.nextInt();
                scanner.nextLine();
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.print("Invalid input. Enter a number (" + min + "-" + max + "): ");
            } catch (java.util.InputMismatchException e) {
                scanner.nextLine(); // consume the bad input
                System.out.print("Invalid input. Enter a number (" + min + "-" + max + "): ");
            }
        }
    }
    @Override
    public void showRoundHeader(int roundNumber) {
        System.out.println("\n--- Round " + roundNumber + " ---");
    }

    @Override
    public void showWelcome() {
        System.out.println("=== Welcome to RPG Combat Game ===\n");
    }

    @Override
    public int promptPlayerClass() {
        System.out.println("Select your class:");
        System.out.println("1. Warrior (High HP & Defense)");
        System.out.println("2. Wizard (High Attack & Speed)");
        System.out.print("Choice (1-2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    @Override
    public String promptPlayerName() {
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    @Override
    public int promptDifficulty() {
        System.out.print("Select difficulty (1=Easy, 2=Medium, 3=Hard): ");
        int difficulty = scanner.nextInt();
        scanner.nextLine();
        return difficulty;
    }

    @Override
    public void showStartingLevel(Object level) {
        System.out.println("\nStarting: " + level + "\n");
    }

    @Override
    public void showPlayerStatus(Player player) {
        System.out.println(player.getName() + " (Player): HP " + player.getHp() + "/" + player.getMaxHp()
                + " | ATK " + player.getAttack() + " | DEF " + player.getDefense());
    }

    @Override
    public void showEnemiesStatus(List<Enemy> enemies) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.isAlive()) {
                System.out.println((i + 1) + ") " + enemy.getName() + ": HP " + enemy.getHp() + "/" + enemy.getMaxHp());
            }
        }
    }

    @Override
    public int promptPlayerAction(Player player) {
        System.out.println("\n" + player.getName() + "'s turn!");
        System.out.println("1. Basic Attack");
        System.out.println("2. Defend");
        System.out.println("3. Special Skill (" + player.getSpecialSkill().getActionName() + ")");
        if (player != null && player.hasItems()) {
            System.out.println("4. Use Item");
        }
        System.out.print("Choose action (1-" + ((player != null && player.hasItems()) ? 4 : 3) + "): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    @Override
    public int promptEnemyTargetIndex(List<Enemy> enemies) {
        System.out.print("Select target (1-" + enemies.size() + "): ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        return idx - 1;
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showUnableToAct(Combatant actor) {
        System.out.println("\n" + actor.getName() + " is unable to act this turn!");
    }

    @Override
    public void showVictory(Player player, int totalRounds) {
        System.out.println("\n*** VICTORY! ***");
        System.out.println("Congratulations, you have defeated all your enemies.");
        System.out.println("Statistics: Remaining HP: " + player.getHp() + "/" + player.getMaxHp()
                + " | Total Rounds: " + totalRounds);
    }

    @Override
    public void showDefeat(Player player, List<Enemy> remainingEnemies, int totalRoundsSurvived) {
        System.out.println("\n*** DEFEATED. Do not give up, try again! ***");
        int remaining = 0;
        if (remainingEnemies != null) {
            for (Enemy e : remainingEnemies) {
                if (e != null && e.isAlive()) remaining++;
            }
        }
        System.out.println("Statistics: Enemies remaining: " + remaining + " | Total Rounds Survived: " + totalRoundsSurvived);
    }

    @Override
    public List<Item> promptStartingItems(List<Item> availableItems) {
        if (availableItems == null || availableItems.isEmpty()) {
            throw new IllegalArgumentException("No items available to choose from.");
        }

        System.out.println("\nChoose 2 starting items (single-use consumables). Duplicates allowed:");
        for (int i = 0; i < availableItems.size(); i++) {
            System.out.println((i + 1) + ". " + availableItems.get(i).getName());
        }

        int first;
        while (true) {
            System.out.print("Pick item #1 (1-" + availableItems.size() + "): ");
            first = scanner.nextInt() - 1;
            scanner.nextLine();
            if (first < 0 || first >= availableItems.size()) {
                System.out.println("Invalid choice.");
                continue;
            }
            break;
        }

        int second;
        while (true) {
            System.out.print("Pick item #2 (1-" + availableItems.size() + ", can be same as #1): ");
            second = scanner.nextInt() - 1;
            scanner.nextLine();
            if (second < 0 || second >= availableItems.size()) {
                System.out.println("Invalid choice.");
                continue;
            }
            break;
        }

        return List.of(availableItems.get(first), availableItems.get(second));
    }

    @Override
    public void showItemInventory(Player player) {
        if (player == null) return;

        var items = player.getItems();
        if (items == null || items.isEmpty()) {
            return;
        }

        java.util.Map<String, Integer> counts = new java.util.LinkedHashMap<>();
        for (Item item : items) {
            if (item == null) continue;
            counts.put(item.getName(), counts.getOrDefault(item.getName(), 0) + 1);
        }

        System.out.println("Items:");
        int i = 1;
        for (var e : counts.entrySet()) {
            System.out.println("  " + (i++) + ". " + e.getKey() + " x" + e.getValue());
        }
    }

    @Override
    public int promptItemChoice(Player player) {
        var items = player.getItems();
        if (items == null || items.isEmpty()) {
            return -1;
        }

        // Build the unique list in insertion order for stable menu numbering.
        java.util.LinkedHashMap<String, Item> unique = new java.util.LinkedHashMap<>();
        for (Item item : items) {
            if (item != null) {
                unique.putIfAbsent(item.getName(), item);
            }
        }

        System.out.print("Choose item (1-" + unique.size() + "): ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice - 1;
    }

    @Override
    public int promptPostGameOption() {
        while (true) {
            System.out.println("\nWhat would you like to do next?");
            System.out.println("1. Replay with same settings");
            System.out.println("2. Start a new game");
            System.out.println("3. Exit");
            System.out.print("Choice (1-3): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice >= 1 && choice <= 3) {
                return choice;
            }
            System.out.println("Invalid choice.");
        }
    }
}
