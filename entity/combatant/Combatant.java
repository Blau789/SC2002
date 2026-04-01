package SC2002.entity.combatant;

import SC2002.entity.combatant.statuseffects.StatusEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Combatant {
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int attack;
    protected int defense;
    protected int baseDefense;
    protected int speed;
    protected List<StatusEffect> statusEffects;

    public Combatant(String name, int maxHp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.baseDefense = defense;
        this.speed = speed;
        this.statusEffects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int damage) {
        if (damage < 0) {
            damage = 0;
        }

        hp -= damage;

        if (hp < 0) {
            hp = 0;
        }
    }

    public void heal(int amount) {
        if (amount <= 0) {
            return;
        }

        hp += amount;

        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    //update new hp based on the effect(hp<0, +healed hp >0)
    public void setHp(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else if (hp > maxHp) {
            this.hp = maxHp;
        } else {
            this.hp = hp;
        }
    }

    //update the current defense value
    public void setDefense(int defense) {
        this.defense = defense;
    }

    //add new effect to this combatant
    public void addStatusEffect(StatusEffect effect) {
        if (effect != null) {
            statusEffects.add(effect);
        }
    }

    //remove a specific effect
    public void removeStatusEffects(StatusEffect effect) {
        statusEffects.remove(effect);
    }

    //return all current effect
    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    //remove the expired effect(s)
    public void removeExpiredEffects() {
        Iterator<StatusEffect> iterator = statusEffects.iterator();

        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next();
            if (effect.isExpired()) {
                effect.remove(this);
                iterator.remove();
            }
        }
    }

}
