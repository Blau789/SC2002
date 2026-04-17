package sc2002.entity.combatant;

import java.util.List;
import sc2002.action.Actions;
import sc2002.strategy.EnemyActionStrategy;
//entity.Enemy is an abstract class inherited from Combatant class and will be the super class of wolf and goblin
public abstract class Enemy extends Combatant {
    protected EnemyActionStrategy actionStrategy;

    public Enemy(String name, int maxHp, int attack, int defense, int speed, EnemyActionStrategy actionStrategy) {
        super(name, maxHp, attack, defense, speed);
        this.actionStrategy = actionStrategy;
        this.setFaction(Faction.ENEMY);
    }


    public EnemyActionStrategy getActionStrategy() {
        return actionStrategy;
    }

    public Actions decideAction(List<Combatant> targets) {
        if (actionStrategy == null) {
            return null;
        }
        return actionStrategy.decideAction(this, targets);
    }

    public Combatant selectTarget(List<Combatant> targets) {
        if (actionStrategy == null) {
            return null;
        }
        return actionStrategy.selectTarget(targets);
    }
}