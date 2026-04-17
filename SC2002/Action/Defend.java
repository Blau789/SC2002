package sc2002.action;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.statuseffects.DefendBuff;
import SC2002.entity.combatant.statuseffects.StatusEffect;
import java.util.ArrayList;
import java.util.List;

public class Defend implements Actions {

    @Override
    public String execute(Combatant source, List<Combatant> targets) {
        // Refresh defend instead of stacking it indefinitely.
        for (StatusEffect effect : new ArrayList<>(source.getStatusEffects())) {
            if (effect instanceof DefendBuff) {
                source.removeStatusEffects(effect);
            }
        }
    
        // Instantiate the effect: lasts for 2 player action windows, +10 defense
        DefendBuff buff = new DefendBuff(2);        
        // Apply the immediate stat change to the source
        buff.apply(source);
        
        // Register it so the Combatant can track its expiration
        source.addStatusEffect(buff);
        
        return String.format("%s took a defensive stance! Defense increased by 10 for 2 player actions.", source.getName());
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
