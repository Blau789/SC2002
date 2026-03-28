package entity.statuseffects;

import entity.Combatant;


public class DefendBuff implements StatusEffect {
    private int duration;
    private static final int DEFENSE_BOOST = 10;

    public DefendBuff(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {
        target.setDefense(target.getDefense() + DEFENSE_BOOST);
    }

    @Override
    public void tick(Combatant target) {
        duration--;
    }

    @Override
    public void remove(Combatant target) {
        target.setDefense(target.getDefense() - DEFENSE_BOOST);
    }

    @Override
    public boolean isExpired() {
        return duration <= 0;
    }

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public int getRemainingDuration() {
        return duration;
    }
}
