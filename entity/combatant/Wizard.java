package SC2002.entity.combatant;

import SC2002.Action.ArcaneBlastAction;
public class Wizard extends Player {
    private int bonusAttack;

    public Wizard(String name) {
        super(name, 200, 50, 10, 20);
        this.bonusAttack = 0;
        this.specialSkill = new ArcaneBlastAction(); 
    }

    @Override
    public int getAttack() {
        return attack + bonusAttack;
    }

    public void addBonusAttack(int amount) {
        if (amount > 0) {
            bonusAttack += amount;
        }
    }

    public int getBonusAttack() {
        return bonusAttack;
    }
}

