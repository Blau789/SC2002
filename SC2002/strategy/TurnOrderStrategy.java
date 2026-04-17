package sc2002.strategy;

import java.util.List;
import sc2002.entity.combatant.Combatant;

public interface TurnOrderStrategy {
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}