package SC2002.strategy;

import SC2002.Action.Actions;
import SC2002.Action.BasicAttack;
import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.Enemy;
import java.util.List;
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
        return possibleTargets.get(0);
    }
}