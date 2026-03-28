package entity;

public class Wolf extends Enemy {
    public Wolf(EnemyActionStrategy strategy) {
        super("entity.Wolf", 40, 45, 5, 35, strategy);

    }

    public Wolf(String name, EnemyActionStrategy strategy) {
        super(name, 40, 45, 5, 35, strategy);
    }


}
