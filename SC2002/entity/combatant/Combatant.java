package SC2002.entity.combatant;

import SC2002.entity.combatant.statuseffects.StatusEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Combatant {
    private String name;
    private int hp;
    private int maxHp;
    private int baseAttack;
    private int baseDefense;
    private int speed;
    private List<StatusEffect> statusEffects;

    public Combatant(String name, int maxHp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.baseAttack = attack;
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
        int totalAttack = this.baseAttack;
        for (StatusEffect effect: this.statusEffects){
            totalAttack += effect.getAttackModifier();
        }
        return Math.max(0,totalAttack);
    }

    public int getDefense() {
        int totalDefense = this.baseDefense;
        for (StatusEffect effect : this.statusEffects) {
            totalDefense += effect.getDefenseModifier();
        }
        return Math.max(0, totalDefense);
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

        double multiplier = 1.0;
        for (StatusEffect effect : statusEffects) {
            multiplier *= effect.getDamageReceivedMultiplier();
        }

        int actualDamage = (int)(damage * multiplier);
        hp -= actualDamage;

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

    //add new effect to this combatant
    public void addStatusEffect(StatusEffect effect) {
        if (effect != null) {
            statusEffects.add(effect);
        }
    }

    //remove a specific effects
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

    public boolean canTakeTurn(){
        for (StatusEffect effect : statusEffects) {
            if (!effect.canTakeTurn()) {
                return false;
            }
        }
        return true;
    }
}


