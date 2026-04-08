package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.statuseffects.StunEffect;
import java.util.List;

public class ShieldBashAction extends SpecialSkill {
    public ShieldBashAction (){
        super("Shield Bash");

    }   
    @Override
    protected String performSkillEffect(Combatant source, List<Combatant> targets) {
        if (targets == null || targets.isEmpty()) {
            return ("Shield Bash failed: No target.");
        }
        Combatant target = targets.get(0);
        
        // Calculate damage 
        int rawDamage = source.getAttack() - target.getDefense();
        int finalDamage = Math.max(0, rawDamage);
        
        target.takeDamage(finalDamage);

        // Stun for TWO turns (current turn + next turn)
        StunEffect stun = new StunEffect(2);
        target.addStatusEffect(stun);

        return String.format("%s used Shield Bash on %s, dealing %d damage and stunning them for 2 turns!", 
                source.getName(), target.getName(), finalDamage);
    }

}

