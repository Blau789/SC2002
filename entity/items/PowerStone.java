package entity.items;

import entity.Combatant;
import entity.Player;
import java.util.List;

public class PowerStone implements Item {

	@Override
	public String use(Combatant user, List<Combatant> enemies) {
		if (user instanceof Player) {
			Player player = (Player) user;
			String result = player.useSpecialSkill(enemies);
			return user.getName() + " used Power Stone! " + result;
		}
		return user.getName() + " tried to use Power Stone but couldn't!";
	}

	@Override
	public String getName() {
		return "Power Stone";
	}
}