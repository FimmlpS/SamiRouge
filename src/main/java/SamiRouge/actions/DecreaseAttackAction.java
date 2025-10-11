package SamiRouge.actions;

import SamiRouge.modifiers.SubstantialCollapseModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DecreaseAttackAction extends AbstractGameAction {
    public DecreaseAttackAction(AbstractCard c,int amt){
        this.c = c;
        this.amount = amt;
    }

    AbstractCard c;

    @Override
    public void update() {
        for(int i =0; i<amount; i++){
            CardModifierManager.addModifier(c,new SubstantialCollapseModifier());
        }
        c.applyPowers();
        this.isDone = true;
    }
}
