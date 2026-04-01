package SC2002.entity.combatant;

public class Goblin extends Enemy {

    public Goblin(EnemyActionStrategy strategy) {
        super("Goblin", 55, 35, 15, 25, strategy);
    }

    public Goblin(String name, EnemyActionStrategy strategy) {
        super(name, 55, 35, 15, 25, strategy);
    }
}
