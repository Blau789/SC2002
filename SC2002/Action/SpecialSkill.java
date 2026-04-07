package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import java.util.List;

public abstract class SpecialSkill implements Actions {

    protected int currentCooldown;
    protected final int maxCooldown;
    protected String skillName;

    public SpecialSkill(String skillName) {
        this(skillName, 2);
    }

    public SpecialSkill(String skillName, int maxCooldown) {
        this.skillName = skillName;
        this.maxCooldown = Math.max(0, maxCooldown);
        this.currentCooldown = 0;
    }

    // The Engine MUST call this ONLY when the combatant actively takes a turn.
    public void reduceCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    public boolean isOnCooldown() {
        return currentCooldown > 0;
    }

    @Override
    public String execute(Combatant source, List<Combatant> targets) {
        if (currentCooldown > 0) {
            return String.format("%s is on cooldown for %d more turn(s)!", skillName, currentCooldown);
        }

        // Put it on cooldown after use.
        this.currentCooldown = maxCooldown;

        return performSkillEffect(source, targets);
    }

    // Child classes implement their specific damage/effects here
    protected abstract String performSkillEffect(Combatant source, List<Combatant> targets);

    @Override
    public String getActionName() {
        return this.skillName;
    }

    public int getCurrentCooldown() {
        return currentCooldown;
    }

    public void setCurrentCooldown(int cooldown) {
        this.currentCooldown = Math.max(0, cooldown);
    }
}