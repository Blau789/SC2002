package SC2002.entity.items;

import SC2002.entity.combatant.Combatant;
import java.util.List;

public class Potion implements Item{
	private static final int HEAL_AMOUNT = 100;

	@Override
    public String use(Combatant user, List<Combatant> targets) {
        if (targets == null || targets.isEmpty()){
            return "potion failed:no valid targets selected";
        }
        if (targets.size() > 1){
            return "potion can only be used on one teammate!";
        } 
        Combatant target = targets.get(0);
        if (!target.isAlive()){
            return "potion failed" + target.getName() + " is already defeated"; 
        }
        int oldHp = target.getHp();
        target.heal(HEAL_AMOUNT);
        int healed = target.getHp() - oldHp;    

        return String.format("%s used potion on %s! Hp: %d -> %d (+%d", user.getName(), target.getName(), oldHp, target.getHp(), healed);
    }

	@Override
	public String getName() {
		return "entity.items.Potion";
	}
}
