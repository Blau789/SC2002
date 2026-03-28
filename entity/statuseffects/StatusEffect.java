package entity.statuseffects;

import entity.Combatant;

public interface StatusEffect {
    void apply(Combatant target);
    void tick(Combatant target);
    void remove(Combatant target);
    boolean isExpired();
    String getName();
    int getRemainingDuration();
}
