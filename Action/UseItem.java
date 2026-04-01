
// 1. FIXED: Package name is now lowercase to respect Java conventions.
package SC2002.Action;

import SC2002.entity.combatant.Combatant;
import SC2002.entity.items.Item;
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
}