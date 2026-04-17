package sc2002.entity.combatant;

import java.util.ArrayList;
import java.util.List;
import sc2002.action.SpecialSkill;
import sc2002.entity.items.Item;

//entity.Player is an abstract class inherited from entity.Combatant class and will be the super class of warrior and wizard
public abstract class Player extends Combatant {
    protected List<Item> items;
    protected SpecialSkill specialSkill; 

    public Player(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
        this.items = new ArrayList<>();
        this.setFaction(Faction.PLAYER);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public void addItem(Item item) {
        if (item != null) {
            items.add(item);
        }
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
    public SpecialSkill getSpecialSkill(){
        return specialSkill;
    }

}