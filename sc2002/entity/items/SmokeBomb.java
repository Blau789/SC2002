package sc2002.entity.items;

import java.util.List;
import sc2002.action.TargetType;
import sc2002.entity.combatant.Combatant;
import sc2002.entity.combatant.statuseffects.SmokeBombEffect;

public class SmokeBomb implements Item {
	private static final int DURATION = 2;

	@Override
	public String use(Combatant user, List<Combatant> targets) {
		SmokeBombEffect effect = new SmokeBombEffect(DURATION);
		effect.apply(user);
		user.addStatusEffect(effect);
		return user.getName() + " used Smoke Bomb! Enemy attacks will do 0 damage for the next " + DURATION + " player actions.";
	}

	@Override
	public String getName() {
		return "Smoke Bomb";
	}
	@Override 
    public TargetType getTargetType(){
        return TargetType.Self;
    }
}