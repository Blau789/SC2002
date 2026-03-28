public interface StatusEffect {
    public interface StatusEffect {
    void apply(Combatant target);   // called when effect is first added
    void tick(Combatant target);    // called at the start of each turn
    void remove(Combatant target);  // called when effect expires/is removed
    boolean isExpired();
    String getName();
    int getRemainingDuration();
}
}
