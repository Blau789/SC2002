package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.statuseffects.DefendBuff;
import java.util.List;

public class Defend implements Actions {

    @Override
    public String execute(Combatant source, List<Combatant> targets) {
    
        // Instantiate the effect: 2 rounds total (current + next), +10 defense
        DefendBuff buff = new DefendBuff(2);        
        // Apply the immediate stat change to the source
        buff.apply(source);
        
        // Register it so the Combatant can track its expiration
        source.addStatusEffect(buff);
        
        return String.format("%s took a defensive stance! Defense increased by 10 for 2 rounds.", source.getName());
    }

    @Override
    public String getActionName() {
        return "Defend";
    }
    @Override 
    public TargetType getTargetType(){
        return TargetType.Self;   
    }
}
