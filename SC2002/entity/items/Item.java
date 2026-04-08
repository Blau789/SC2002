package SC2002.entity.items;

import SC2002.Action.TargetType;
import SC2002.entity.combatant.Combatant;
import java.util.List;

public interface Item{
	String use(Combatant user, List<Combatant> targets);
	String getName();
	TargetType getTargetType();
}