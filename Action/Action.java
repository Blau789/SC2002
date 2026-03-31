

package SC2002.Action;

import java.util.List;

public interface Action {
    String execute(Combatant source, List<Combatant> targets);
    String getActionName();
}

