package SC2002.entity.items;

import SC2002.Action.TargetType;
import SC2002.entity.combatant.Combatant;
import java.util.List;

public class Potion implements Item{
    private static final int HEAL_AMOUNT = 100;

    @Override
    public String use(Combatant user, List<Combatant> targets) {
        Combatant target;
        if (targets == null || targets.isEmpty()) {
            target = user;
        } else {
            target = targets.get(0);
        }

        if (target == null) {
            return "Potion failed: no valid target.";
        }
        if (!target.isAlive()) {
            return "Potion failed: " + target.getName() + " is already defeated.";
        }

        int oldHp = target.getHp();
        target.heal(HEAL_AMOUNT);
        int healed = target.getHp() - oldHp;

        return String.format("%s used Potion! HP: %d -> %d (+%d)",
                user.getName(), oldHp, target.getHp(), healed);
    }

    @Override
    public String getName() {
        return "Potion";
    }
    @Override 
    public TargetType getTargetType(){
        return TargetType.Self;
    }
}
