package sc2002.strategy;

import java.util.List;
import sc2002.action.Actions;
import sc2002.action.BasicAttack;
import sc2002.entity.combatant.Combatant;
import sc2002.entity.combatant.Enemy;
public class BasicAttackStrategy implements EnemyActionStrategy {

    @Override
    public Actions decideAction(Enemy enemy, List<Combatant> possibleTargets) {
        return new BasicAttack();
    }

    @Override
    public Combatant selectTarget(List<Combatant> possibleTargets) {
        if (possibleTargets == null || possibleTargets.isEmpty()) {
            return null;
        }
        // In this assignment, there is only one player, so return the first alive target
        for (Combatant target : possibleTargets) {
            if (target.isAlive()) {
                return target;
            }
        }
        return null; //signal that battle is over if null is returned
    }
}