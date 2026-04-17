package sc2002.action;

import sc2002.entity.combatant.Combatant;
import java.util.List;

public interface Actions {
    String execute(Combatant source, List<Combatant> targets);
    String getActionName();
    TargetType getTargetType();
}

