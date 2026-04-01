package SC2002.entity.items;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.Player;
import java.util.List;

public class PowerStone implements Item {

	@Override
    public String use(Combatant user, List<Combatant> allEnemies) {
        if (user instanceof Player) {
            Player player = (Player) user;
            // Save current cooldown — Power Stone does not affect it
            int savedCooldown = player.getCooldown();
            String result = player.useSpecialSkill(allEnemies);
            // Restore cooldown to what it was before
            player.setCooldown(savedCooldown);
            return "Power Stone used! " + result;
        }
        return "Power Stone fizzles...";
	}
	
	@Override
	public String getName() {
		return "Power Stone";
	}
}