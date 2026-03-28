package entity.statuseffects;

public class SmokeBombEffect implements StatusEffect {
    private int duration;

    public SmokeBombEffect(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        // Effect is checked externally in StatusEffectManager.hasSmokeBombActive()
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
        return "Smoke Bomb";
    }

    @Override
    public int getRemainingDuration() {
        return duration;
    }
}
