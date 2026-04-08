package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import java.util.List;

public interface Actions {
    String execute(Combatant source, List<Combatant> targets);
    String getActionName();
    TargetType getTargetType();
}

