package sc2002.entity.combatant;

import sc2002.action.ShieldBashAction;

public class Warrior extends Player {

    public Warrior(String name) {
        super(name, 260, 40, 20, 30);
        this.specialSkill = new ShieldBashAction();
    }
 }

