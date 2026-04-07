package SC2002.entity.combatant;

import SC2002.Action.ArcaneBlastAction;
public class Wizard extends Player {

    public Wizard(String name) {
        super(name, 200, 50, 10, 20);
        this.specialSkill = new ArcaneBlastAction(); 
    }

}

