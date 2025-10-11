package SamiRouge.actions;

import SamiRouge.modifiers.ForeverModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class MarkAsForeverAction extends AbstractGameAction {
    public MarkAsForeverAction(){
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if(this.duration==Settings.ACTION_DUR_MED){
            if(AbstractDungeon.player.hand.isEmpty()){
                this.isDone = true;
            }
            else if (AbstractDungeon.player.hand.size()==1){
                AbstractCard tmp = AbstractDungeon.player.hand.getBottomCard();
                CardModifierManager.addModifier(tmp,new ForeverModifier());
                tmp.flash();
                AbstractDungeon.player.hand.addToTop(tmp);
                AbstractDungeon.handCardSelectScreen.selectedCards.clear();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                this.isDone = true;
            }
            else{
                UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Operate");
                AbstractDungeon.handCardSelectScreen.open(uiStrings.TEXT[0],1,false,false);
                this.tickDuration();
            }
        }
        else{
            if(!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved){
                AbstractCard tmp = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();
                CardModifierManager.addModifier(tmp,new ForeverModifier());
                tmp.flash();
                AbstractDungeon.player.hand.addToTop(tmp);
                AbstractDungeon.handCardSelectScreen.selectedCards.clear();
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
            this.tickDuration();
        }
    }
}
