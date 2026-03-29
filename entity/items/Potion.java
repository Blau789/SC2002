package entity.items;

import entity.Combatant;
import java.util.List;

public class Potion implements Item{
	private static final int HEAL_AMOUNT = 100;

	@Override
    public String use(Combatant user, List<Combatant> allEnemies) {
        int oldHp = user.getHp();
        user.heal(HEAL_AMOUNT);
        int healed = user.getHp() - oldHp;
        return user.getName() + " uses Potion! HP: " + oldHp + " → " + user.getHp()
                + " (+" + healed + ")";
    }

	@Override
	public String getName() {
		return "entity.items.Potion";
	}
}
