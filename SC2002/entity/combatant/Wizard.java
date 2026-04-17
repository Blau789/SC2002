package sc2002.entity.combatant;

import sc2002.action.ArcaneBlastAction;
public class Wizard extends Player {

    public Wizard(String name) {
        super(name, 200, 50, 10, 20);
        this.specialSkill = new ArcaneBlastAction(); 
    }

}

