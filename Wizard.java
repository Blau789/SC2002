import java.util.List;

public class Wizard extends Player {
    private int bonusAttack;

    public Wizard(String name) {
        super(name, 200, 50, 10, 20);
        this.bonusAttack = 0;
    }

    @Override
    public int getAttack() {
        return attack + bonusAttack;
    }

    public void addBonusAttack(int amount) {
        if (amount > 0) {
            bonusAttack += amount;
        }
    }

    public int getBonusAttack() {
        return bonusAttack;
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast Effect";
    }

    @Override
    public String useSpecialSkill(List<Combatant> targets) {
        if (!isSpecialSkillReady()) {
            return name + " tried to use Arcane Blast Effect but it is on cooldown!";
        }

        String result = name + " used Arcane Blast Effect!";
        int defeatedCount = 0;

        //attack on all the enemies that are alive
        for (Combatant target : targets) {
            if (target != null && target.isAlive()) {
                int damage = Math.max(0, this.getAttack() - target.getDefense());
                target.takeDamage(damage);

                result += "\n" + target.getName() + " took " + damage + " damage.";

                //count how many enemies were defeated  in this term
                if (!target.isAlive()) {
                    defeatedCount++;
                }
            }
        }

        //each defeated enemy will increase the attack amount by 10
        if (defeatedCount > 0) {
            addBonusAttack(defeatedCount * 10);
            result += "\n" + name + " gained " + (defeatedCount * 10) + " bonus attack!";
        }

        resetCooldown();
        return result;
    }
}
