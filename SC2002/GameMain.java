package sc2002;

import java.util.List;
import java.util.Scanner;
import sc2002.engine.CombatEngine;
import sc2002.entity.combatant.Enemy;
import sc2002.entity.combatant.Player;
import sc2002.entity.combatant.Warrior;
import sc2002.entity.combatant.Wizard;
import sc2002.entity.items.Item;
import sc2002.entity.items.Potion;
import sc2002.entity.items.PowerStone;
import sc2002.entity.items.SmokeBomb;
import sc2002.level.Level;
import sc2002.level.LevelFactory;
import sc2002.strategy.SpeedBasedTurnOrder;
import sc2002.ui.ConsoleGameUI;
import sc2002.ui.GameUI;
public class GameMain {

    public static void main(String[] args) throws Exception{

        Scanner scanner = new Scanner(System.in);
        GameUI ui = new ConsoleGameUI(scanner);

        Integer lastClassChoice = null;
        String lastName = null;
        Integer lastDifficulty = null;
        List<Item> lastChosenItems = null;

        while (true) {
            ui.showWelcome();

            // Start new game if we don't have settings, otherwise reuse for replay.
            if (lastClassChoice == null || lastDifficulty == null || lastName == null || lastChosenItems == null) {
                int classChoice = ui.promptPlayerClass();
                String name = ui.promptPlayerName();
                int difficulty = ui.promptDifficulty();

                List<Item> availableItems = List.of(new Potion(), new PowerStone(), new SmokeBomb());
                List<Item> chosen = ui.promptStartingItems(availableItems);

                lastClassChoice = classChoice;
                lastName = name;
                lastDifficulty = difficulty;
                lastChosenItems = chosen;
            }

            Player player = (lastClassChoice == 1) ? new Warrior(lastName) : new Wizard(lastName);
            for (Item item : lastChosenItems) {
                player.addItem(item);
            }

            Level level = LevelFactory.createLevel(lastDifficulty);
            ui.showStartingLevel(level);

            List<Enemy> enemies = level.getInitialEnemies();
            
            CombatEngine engine = new CombatEngine(new SpeedBasedTurnOrder());            
            CombatEngine.BattleResult result = engine.playLevel(player, enemies, level.getBackupEnemies(), ui);

            if (result.getOutcome() == CombatEngine.BattleResult.Outcome.VICTORY) {
                ui.showVictory(player, result.getTotalRounds());
            } else {
                ui.showDefeat(player, result.getRemainingEnemies(), result.getTotalRounds());
            }

            int post = ui.promptPostGameOption();
            if (post == 1) {
                // replay with same settings
                continue;
            }
            if (post == 2) {
                // new game
                lastClassChoice = null;
                lastName = null;
                lastDifficulty = null;
                lastChosenItems = null;
                continue;
            }

            break; // exit
        }

        scanner.close();

    }

}
