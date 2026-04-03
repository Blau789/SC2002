package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.combatant.statuseffects.ArcaneBlastBuff;
import java.util.List;

public class ArcaneBlastAction extends SpecialSkill {

    public ArcaneBlastAction() {
        super("Arcane Blast Effect");
    }

    @Override
    protected String performSkillEffect(Combatant source, List<Combatant> targets) {
        String result = source.getName() + " used Arcane Blast Effect!";
        int defeatedCount = 0;

        for (Combatant target : targets) {
            if (target != null && target.isAlive()) {
                int damage = Math.max(0, source.getAttack() - target.getDefense());
                target.takeDamage(damage);

                result += "\n" + target.getName() + " took " + damage + " damage.";

                if (!target.isAlive()) {
                    defeatedCount++;
                }
            }
        }

        if (defeatedCount > 0) {
            int bonus = defeatedCount * 10;
            ArcaneBlastBuff buff = new ArcaneBlastBuff(bonus);
            buff.apply(source); // Applies the bonus to the wizard
            source.addStatusEffect(buff);
            result += "\n" + source.getName() + " gained " + bonus + " bonus attack!";
        }

        return result;
    }
}
