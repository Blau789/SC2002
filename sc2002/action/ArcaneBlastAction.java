package sc2002.action;

import sc2002.entity.combatant.Combatant;
import sc2002.entity.combatant.statuseffects.ArcaneBlastBuff;
import java.util.List;

public class ArcaneBlastAction extends SpecialSkill {

    public ArcaneBlastAction() {
        super("Arcane Blast");
    }

    @Override
    protected String performSkillEffect(Combatant source, List<Combatant> targets) {
        StringBuilder result = new StringBuilder(source.getName() + " used Arcane Blast!");
        int defeatedCount = 0;

        if (targets == null || targets.isEmpty()) {
            return result.append("\nBut there were no targets.").toString();
        }

        for (Combatant target : targets) {
            if (target != null && target.isAlive()) {
                int damage = Math.max(0, source.getAttack() - target.getDefense());
                target.takeDamage(damage);

                result.append("\n").append(target.getName()).append(" took ").append(damage).append(" damage.");

                if (!target.isAlive()) {
                    defeatedCount++;
                    result.append("\n").append(target.getName()).append(" was defeated!");
                }
            }
        }

        if (defeatedCount > 0) {
            int bonus = defeatedCount * 10;
            ArcaneBlastBuff buff = new ArcaneBlastBuff(bonus);
            // Buff uses getAttackModifier() via StatusEffect; addStatusEffect is enough.
            source.addStatusEffect(buff);
            result.append("\n").append(source.getName()).append(" gained +").append(bonus)
                    .append(" Attack until end of combat.");
        }

        return result.toString();
    }
    @Override 
    public TargetType getTargetType(){
        return TargetType.All_enemies;   
    }

}