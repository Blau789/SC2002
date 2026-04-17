package sc2002.strategy;

import java.util.List;
import sc2002.action.Actions;
import sc2002.entity.combatant.Combatant;
import sc2002.entity.combatant.Enemy;
public interface EnemyActionStrategy {

    Actions decideAction(Enemy enemy, List<Combatant> possibleTargets);

    Combatant selectTarget(List<Combatant> possibleTargets);
}
