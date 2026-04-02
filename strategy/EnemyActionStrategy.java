package com.game.strategy;

import java.util.List;
import com.game.model.action.Action;
import com.game.model.combatant.Combatant;
import com.game.model.combatant.Enemy;

public interface EnemyActionStrategy {

    Action decideAction(Enemy enemy, List<Combatant> possibleTargets);

    Combatant selectTarget(List<Combatant> possibleTargets);
}
