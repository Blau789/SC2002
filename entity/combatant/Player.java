package SC2002.entity.combatant;

import SC2002.entity.items.Item;
import Combatant;

import java.util.ArrayList;
import java.util.List;

//entity.Player is an abstract class inherited from entity.Combatant class and will be the super class of warrior and wizard
public abstract class Player extends Combatant {
    protected List<Item> items;
    protected int specialSkillCooldown;

    public Player(String name, int maxHp, int attack, int defense, int speed) {
        super(name, maxHp, attack, defense, speed);
        this.items = new ArrayList<>();
        this.specialSkillCooldown = 0;
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

    public boolean isSpecialSkillReady() {
        return specialSkillCooldown == 0;
    }

    public void resetCooldown() {
        specialSkillCooldown = 3;
    }

    public void decrementCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    public int getCooldown() {
        return specialSkillCooldown;
    }

    //for cases when the users use power stone (cooldown = 0)
    public void setCooldown(int cd) {
        if (cd < 0) {
            specialSkillCooldown = 0;
        } else {
            specialSkillCooldown = cd;
        }
    }

    public abstract String useSpecialSkill(List<Combatant> targets);

    public abstract String getSpecialSkillName();
}