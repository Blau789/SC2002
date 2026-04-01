package SC2002.entity.combatant;

import entity.statuseffects.StunEffect;
import java.util.List;

public class Warrior extends Player {

    public Warrior(String name) {
        super(name, 260, 40, 20, 30);
    }

    @Override
    public String getSpecialSkillName() {
        return "Shield Bash Effect";
    }

    @Override
    public String useSpecialSkill(List<Combatant> targets) {
        if (!isSpecialSkillReady()) {
            return name + " tried to use Shield Bash Effect but it is on cooldown!";
        }

        Combatant target = targets.get(0);

        int damage = Math.max(0, this.attack - target.getDefense());
        target.takeDamage(damage);
        StunEffect stun = new StunEffect(2);
        stun.apply(target);
        target.addStatusEffect(stun);

        // reset cooldown to 3 turns
        resetCooldown();

        return name + " used Shield Bash Effect on " + target.getName()
                + " dealing " + damage + " damage and increasing defense!";
    }
}
