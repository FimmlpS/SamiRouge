package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExhaustToDrawAction extends AbstractGameAction {
    AbstractCard c;
    boolean top = false;

    public ExhaustToDrawAction(AbstractCard c){
        this.c = c;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public ExhaustToDrawAction(AbstractCard c,boolean top){
        this.c = c;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.top = top;
    }

    @Override
    public void update() {
        if(this.duration== Settings.ACTION_DUR_MED){
            if(AbstractDungeon.player.exhaustPile.contains(c)){
                AbstractDungeon.player.exhaustPile.removeCard(c);
                if(!top)
                    AbstractDungeon.player.drawPile.addToRandomSpot(c);
                else{
                    AbstractDungeon.player.drawPile.addToTop(c);
                }
                c.stopGlowing();
                c.unhover();
                c.unfadeOut();
                c.setAngle(0.0F,true);
                c.lighten(false);
                c.drawScale = 0.12F;
                c.targetDrawScale=0.75F;

            }
        }

        this.tickDuration();

        this.isDone = true;
    }
}

