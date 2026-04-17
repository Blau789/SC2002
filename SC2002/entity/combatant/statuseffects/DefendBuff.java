package sc2002.entity.combatant.statuseffects;

import sc2002.entity.combatant.Combatant;


public class DefendBuff implements StatusEffect {
    private int duration;
    private static final int DEFENSE_BOOST = 10;

    public DefendBuff(int duration) {
        this.duration = duration;
    }

    @Override
    public void apply(Combatant target) {

    }

    @Override
    public void tick(Combatant target) {
        // Defend is event-based; round ticks do not consume it.
    }

    @Override
    public void remove(Combatant target) {

    }
    @Override
    public int getDefenseModifier(){
        return duration > 0 ? DEFENSE_BOOST : 0;
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

    @Override
    public boolean ticksOnRoundStart() {
        return false;
    }

    @Override
    public void onOwnerAction(Combatant target) {
        duration--;
    }
}
