package com.game.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.game.model.combatant.Combatant;

public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> sorted = new ArrayList<>(combatants);
        sorted.sort(Comparator.comparingInt(Combatant::getSpeed).reversed());
        return sorted;
    }
}