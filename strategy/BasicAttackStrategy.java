package com.game.strategy;

import com.game.model.action.Action;
import com.game.model.action.BasicAttack;
import com.game.model.combatant.Combatant;
import com.game.model.combatant.Enemy;
import java.util.List;

public class BasicAttackStrategy implements EnemyActionStrategy {

    @Override
    public Action decideAction(Enemy enemy, List<Combatant> possibleTargets) {
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