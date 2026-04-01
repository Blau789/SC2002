package SC2002.entity.items;

import SC2002.entity.combatant.Combatant;
import java.util.List;

public interface Item{
	String use(Combatant user, List<Combatant> enemies);
	String getName();
}