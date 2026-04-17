package sc2002.ui;

import java.util.List;
import sc2002.entity.combatant.Combatant;
import sc2002.entity.combatant.Enemy;
import sc2002.entity.combatant.Player;
import sc2002.entity.items.Item;

public interface GameUI {
    void showWelcome();

    /** Returns 1 for Warrior, 2 for Wizard. */
    int promptPlayerClass();

    String promptPlayerName();

    /** Returns 1..3 */
    int promptDifficulty();

    void showStartingLevel(Object level);

    void showRoundHeader(int roundNumber);

    void showPlayerStatus(Player player);

    void showEnemiesStatus(List<Enemy> enemies);

    /** Returns 1..3 */
    int promptPlayerAction(Player player);

    /** Returns 0-based index into enemies list. */
    int promptEnemyTargetIndex(List<Enemy> enemies);

    void showMessage(String message);

    void showUnableToAct(Combatant actor);

    /**
     * Prompt the player to pick exactly 2 items out of the given list.
     * Duplicates are allowed (e.g., 2x Potion).
     * Returns the selected items (size 2).
     */
    List<Item> promptStartingItems(List<Item> availableItems);

    /** Display the player's chosen items with remaining counts. */
    void showItemInventory(Player player);

    /** Prompt for which item to use. Returns 0-based index into the displayed item list. */
    int promptItemChoice(Player player);

    /** Prompt what to do after game ends. Returns 1=Replay, 2=New game, 3=Exit. */
    int promptPostGameOption();

    void showVictory(Player player, int totalRounds);

    void showDefeat(Player player, List<Enemy> remainingEnemies, int totalRoundsSurvived);

}
