package entity.items;

import entity.Combatant;
import java.util.List;

public interface Item{
	String use(Combatant user, List<Combatant> enemies);
	String getName();
}