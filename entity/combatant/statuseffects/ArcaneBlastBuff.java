package SC2002.entity.statuseffects;

import entity.Combatant;
import entity.Wizard;

public class ArcaneBlastBuff implements StatusEffect {
    private final int bonusAttack;

    public ArcaneBlastBuff(int bonusAttack) {
        this.bonusAttack = bonusAttack;
    }

    @Override
    public void apply(Combatant target) {
        if (target instanceof Wizard) {
            ((Wizard) target).addBonusAttack(bonusAttack);
        }
    }

    @Override
    public void tick(Combatant target) {
        // Permanent for the level — does not tick down
    }

    @Override
    public void remove(Combatant target) {
        // Not reversed mid-level
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String getName() {
        return "Arcane Blast Buff";
    }

    @Override
    public int getRemainingDuration() {
        return Integer.MAX_VALUE;
    }

    public int getBonusAttack() {
        return bonusAttack;
    }
}