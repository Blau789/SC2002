package SC2002.entity.combatant.statuseffects;

import SC2002.entity.combatant.Combatant;

public class StunEffect implements StatusEffect{
    private int duration;

    public StunEffect(int duration) {
        this.duration = duration;
    }
    @Override
    public void apply(Combatant target) {
        // No stat changes needed — stun is checked externally by StatusEffectManager
    }

    @Override
    public void tick(Combatant target) {
        duration--;
    }

    @Override
    public void remove(Combatant target) {
        // Nothing to reverse
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public String getName() {
        return "Stun";
    }

    @Override
    public int getRemainingDuration() {
        return duration;
    }
}
