package entity.items;

import entity.Combatant;
import entity.statuseffects.SmokeBombEffect;
import java.util.List;

public class SmokeBomb implements Item {
	private static final int DURATION = 2;

	@Override
	public String use(Combatant user, List<Combatant> enemies) {
		SmokeBombEffect effect = new SmokeBombEffect(DURATION);
		effect.apply(user);
		user.addStatusEffect(effect);
		return user.getName() + " used Smoke Bomb! Enemy attacks will do 0 damage for " + DURATION + " turns.";
	}

	@Override
	public String getName() {
		return "Smoke Bomb";
	}
}