package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import java.util.List;

public class Defend implements Actions {

    @Override
    public String execute(Combatant source, List<Combatant> targets) {
        
        
        
        return String.format("%s took a defensive stance! Defense increased by 10 for 2 rounds.", source.getName());
    }

    @Override
    public String getActionName() {
        return "Defend";
    }
}
