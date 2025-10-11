package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class EveryNightAction extends AbstractGameAction {
    public EveryNightAction(int previewAmt){
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = previewAmt;
        duration = startDuration = Settings.ACTION_DUR_FAST;
    }

    boolean selected = false;

    @Override
    public void update() {
        if(duration == startDuration){
            if(AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()){
                this.isDone = true;
                return;
            }
            ArrayList<AbstractCard> cards = new ArrayList<>();
            cards.addAll(AbstractDungeon.player.drawPile.group);
            cards.addAll(AbstractDungeon.player.discardPile.group);
            if(cards.size()==0){
                this.isDone = true;
                return;
            }
            Collections.shuffle(cards,AbstractDungeon.cardRandomRng.random);
            String des = CardCrawlGame.languagePack.getUIString("samirg:Operate").TEXT[1];
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for(int i =0;i<this.amount&&i<cards.size();i++){
                tmp.group.add(cards.get(i));
            }
            AbstractDungeon.gridSelectScreen.open(tmp,1,false,des);
        }
        else if(!selected){
            Iterator<AbstractCard> var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
            while (var1.hasNext()){
                AbstractCard c = var1.next();
                c.flash();
                if(AbstractDungeon.player.drawPile.contains(c)){
                    AbstractDungeon.player.drawPile.moveToHand(c);
                }
                else if(AbstractDungeon.player.discardPile.contains(c)){
                    AbstractDungeon.player.discardPile.moveToHand(c);
                }
                c.setCostForTurn(0);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            selected = true;
        }

        this.tickDuration();
    }
}
