package SC2002.entity.combatant.statuseffects;

import SC2002.entity.combatant.Combatant;

public class SmokeBombEffect implements StatusEffect {
    private int duration;

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        // Effect is checked externally by StatusEffectManager
    }

    @Override
    public void tick(Combatant target) {
        // Smoke Bomb is consumed by enemy attack actions, not by round ticks.
    }

    @Override
    public void remove(Combatant target) {

    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public int getRemainingDuration() {
        return duration;
    }

    @Override
    public boolean ticksOnRoundStart() {
        return false;
    }

    @Override
    public void onDamageReceived(Combatant target) {
        duration--;
    }

    @Override
    public double getDamageReceivedMultiplier(){
        return 0.0;
    }
}
