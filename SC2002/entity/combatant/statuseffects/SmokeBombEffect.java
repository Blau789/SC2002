package sc2002.entity.combatant.statuseffects;

import sc2002.entity.combatant.Combatant;

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
        // Smoke Bomb is event-based; round ticks do not consume it.
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
    public void onOwnerAction(Combatant target) {
        duration--;
    }

    @Override
    public double getDamageReceivedMultiplier(){
        return duration > 0 ? 0.0 : 1.0;
    }
}
