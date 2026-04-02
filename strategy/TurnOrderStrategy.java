package com.game.strategy;

import java.util.List;
import com.game.model.combatant.Combatant;

public interface TurnOrderStrategy {
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}