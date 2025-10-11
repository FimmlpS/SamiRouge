package SamiRouge.actions;

import SamiRouge.modifiers.ExtremelyColdModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MarkExtremelyColdAction extends AbstractGameAction {
    public MarkExtremelyColdAction(){

    }

    @Override
    public void update() {
        //draw
        if(AbstractDungeon.player.drawPile.size()>0){
            AbstractCard c = AbstractDungeon.player.drawPile.getRandomCard(AbstractDungeon.cardRandomRng);
            CardModifierManager.addModifier(c,new ExtremelyColdModifier());
            c.flash();
        }

        //hand
        if(AbstractDungeon.player.hand.size()>0){
            AbstractCard c = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
            CardModifierManager.addModifier(c,new ExtremelyColdModifier());
            c.flash();
        }

        //discard
        if(AbstractDungeon.player.discardPile.size()>0){
            AbstractCard c = AbstractDungeon.player.discardPile.getRandomCard(AbstractDungeon.cardRandomRng);
            CardModifierManager.addModifier(c,new ExtremelyColdModifier());
            c.flash();
        }
        this.isDone = true;
    }
}
