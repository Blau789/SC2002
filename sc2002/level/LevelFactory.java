package sc2002.level;

import sc2002.strategy.BasicAttackStrategy;
import sc2002.strategy.EnemyActionStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import sc2002.entity.combatant.Goblin;
import sc2002.entity.combatant.Wolf;


public class LevelFactory {

    public static Level createLevel(int difficulty) {
        EnemyActionStrategy strategy = new BasicAttackStrategy();

        switch (difficulty) {
            case 1:
                return new Level(1, "Easy",
                        Arrays.asList(
                                new Goblin("Goblin A", strategy),
                                new Goblin("Goblin B", strategy),
                                new Goblin("Goblin C", strategy)
                        ),
                        new ArrayList<>()
                );

            case 2:
                return new Level(2, "Medium",
                        Arrays.asList(
                                new Goblin("Goblin", strategy),
                                new Wolf("Wolf", strategy)
                        ),
                        Arrays.asList(
                                new Wolf("Wolf A", strategy),
                                new Wolf("Wolf B", strategy)
                        )
                );

            case 3:
                return new Level(3, "Hard",
                        Arrays.asList(
                                new Goblin("Goblin A", strategy),
                                new Goblin("Goblin B", strategy)
                        ),
                        Arrays.asList(
                                new Goblin("Goblin C", strategy),
                                new Wolf("Wolf A", strategy),
                                new Wolf("Wolf B", strategy)
                        )
                );

            default:
                throw new IllegalArgumentException("Invalid difficulty level: " + difficulty);
        }
    }
}