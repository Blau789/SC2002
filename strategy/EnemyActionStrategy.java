package SC2002.strategy;

import SC2002.Action.Actions;
import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.Enemy;
import java.util.List;
public interface EnemyActionStrategy {

    Actions decideAction(Enemy enemy, List<Combatant> possibleTargets);

    Combatant selectTarget(List<Combatant> possibleTargets);
}
