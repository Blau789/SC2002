package sc2002.entity.items;

import java.util.List;
import sc2002.action.TargetType;
import sc2002.entity.combatant.Combatant;

public interface Item{
	String use(Combatant user, List<Combatant> targets);
	String getName();
	TargetType getTargetType();
}