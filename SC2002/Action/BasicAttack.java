package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import java.util.List;

public class BasicAttack implements Actions {

    @Override
    public String execute(Combatant source, List<Combatant> targets) {
        if (targets == null || targets.isEmpty()) {
            return "Attack failed: No target selected.";
        }

        Combatant target = targets.get(0);
        
        //   Damage = max(0, Attacker Attack - Target Defense)
        int rawDamage = source.getAttack() - target.getDefense();
        int finalDamage = Math.max(0, rawDamage);
        int actualDamage = target.takeDamage(finalDamage);



        // Apply damage
        target.takeDamage(finalDamage);

        return String.format("%s used Basic Attack on %s for %d damage!",
                source.getName(), target.getName(), actualDamage);
    }

    @Override
    public String getActionName() {
        return "Basic Attack";
    }
    @Override 
    public TargetType getTargetType(){
        return TargetType.Single_enemy;   
    }
}
