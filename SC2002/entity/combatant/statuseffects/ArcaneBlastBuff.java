package sc2002.entity.combatant.statuseffects;

import sc2002.entity.combatant.Combatant;

public class ArcaneBlastBuff implements StatusEffect {
    private final int bonusAttack;

    public ArcaneBlastBuff(int bonusAttack) {
        this.bonusAttack = bonusAttack;
    }

    @Override
    public void apply(Combatant target) {
        // No immediate mutation; effect is applied via getAttackModifier().
    }

    @Override
    public void tick(Combatant target) {
        // Permanent for the combat — does not tick down
    }

    @Override
    public void remove(Combatant target) {
        // Not reversed mid-combat
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

    @Override
    public int getAttackModifier() {
        return bonusAttack;
    }

    public int getBonusAttack() {
        return bonusAttack;
    }
}