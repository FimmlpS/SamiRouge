package SamiRouge.actions;

import SamiRouge.powers.ColdnessPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ResetColdnessAction extends AbstractGameAction {
    public ResetColdnessAction(ColdnessPower c, int maxLevel){
        this.c =c;
        this.amount = maxLevel;
    }

    @Override
    public void update() {
        if(c!=null){
            c.resetColdness(this.amount);
        }

        this.isDone=true;
    }

    ColdnessPower c;
}
