package SC2002.entity.combatant.statuseffects;

import SC2002.entity.combatant.Combatant;

public interface StatusEffect {
    void apply(Combatant target);
    void tick(Combatant target);
    void remove(Combatant target);
    boolean isExpired();
    String getName();
    int getRemainingDuration();

    default int getDefenseModifier(){
        return 0;
    }
}
