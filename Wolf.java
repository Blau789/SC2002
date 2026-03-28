public class Wolf extends Enemy {
    public Wolf(EnemyActionStrategy strategy) {
        super("Wolf", 40, 45, 5, 35);

    }

    public Wolf(String name, EnemyActionStrategy strategy) {
        super(name, 40, 45, 5, 35, strategy);
    }


}
