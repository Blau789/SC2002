package SC2002.strategy;

import SC2002.entity.combatant.Combatant;
import java.util.List;


public interface TurnOrderStrategy {
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}