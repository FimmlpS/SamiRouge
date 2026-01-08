package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HandOfOpportunity extends CustomRelic {

    public static final String ID = "samirg:HandOfOpportunity";
    private static final String IMG = "SamiRougeResources/img/relics/HandOfOpportunity.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HandOfOpportunity_O.png";

    public HandOfOpportunity(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(drawnCard.type== AbstractCard.CardType.POWER&&drawnCard.cost>1){
            drawnCard.flash();
            drawnCard.updateCost(-1);
        }
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(targetCard.type== AbstractCard.CardType.POWER){
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new DrawCardAction(1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfOpportunity();
    }
}


