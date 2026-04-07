package SC2002.entity.combatant;

import SC2002.Action.ShieldBashAction;


public class Warrior extends Player {

    public Warrior(String name) {
        super(name, 260, 40, 20, 30);
        this.specialSkill = new ShieldBashAction();
    }
 }

