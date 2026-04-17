package sc2002.entity.items;

import sc2002.qction.SpecialSkill;
import sc2002.qction.TargetType;
import sc2002.entity.combatant.Combatant;
import sc2002.entity.combatant.Player;
import java.util.List;

public class PowerStone implements Item {

	@Override
    public String use(Combatant user, List<Combatant> targets) {
        if (user instanceof Player) {
            Player player = (Player) user;
            SpecialSkill skill = player.getSpecialSkill();
            if (skill == null){
                return "no special skill equipped to use the Power Stone";
            }
            int savedCooldown = skill.getCurrentCooldown();
            skill.setCurrentCooldown(0);
            String result = skill.execute(player,targets);

            skill.setCurrentCooldown(savedCooldown);
            return "Power Stone used! " + result;
        }
        return "Power Stone fizzles...";
	}
	
	@Override
	public String getName() {
		return "Power Stone";
	}
    @Override 
    public TargetType getTargetType(){
        return TargetType.Dependent;
    }
}