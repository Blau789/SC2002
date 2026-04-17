package sc2002.action;

import sc2002.entity.combatant.Combatant;
import sc2002.entity.items.Item;
import java.util.List;

public class UseItem implements Actions {

   
    private Item itemToUse; 
    
  
    public UseItem(Item itemToUse) {
        this.itemToUse = itemToUse;
    }

    @Override
    public String execute(Combatant source, List<Combatant> targets) {
        if (this.itemToUse == null) {
            return "No item selected!";
        }
        
        return itemToUse.use(source, targets);
    }

    @Override
    public String getActionName() {
        return "Use " + itemToUse.getName();
    }
    @Override 
    public TargetType getTargetType(){
        return itemToUse.getTargetType();   
    }
}