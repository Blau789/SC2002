package entity.items;

import java.util.List;
public class Potion implements Item{
	private static final int HEAL_AMOUNT = 100;

	@Override
	public String use(Combatant user, List<Combatant> enemies) {
		int newHp = Math.min(user.getHp() + HEAL_AMOUNT, user.getMaxHp());
		user.setHp(newHp);
		return user.getName() + " used entity.items.Potion and healed to " + user.getHp() + " HP.";
	}

	@Override
	public String getName() {
		return "entity.items.Potion";
	}
}
