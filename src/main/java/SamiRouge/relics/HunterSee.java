package SamiRouge.relics;

import SamiRouge.actions.HuntAction;
import SamiRouge.dungeons.TheSami;
import SamiRouge.modifiers.HuntModifier;
import SamiRouge.patches.RelicPatch;
import SamiRouge.samiMod.ModConfig;
import basemod.abstracts.CustomRelic;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HunterSee extends CustomRelic {

    public static final String ID = "samirg:HunterSee";
    private static final String IMG = "SamiRougeResources/img/relics/HunterSee.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HunterSee_O.png";

    private boolean applied = false;

    public HunterSee(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.SOLID);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atTurnStart() {
        applied = false;
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(applied)
            return;
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        if(drawnCard.type!= AbstractCard.CardType.ATTACK&&drawnCard.type!= AbstractCard.CardType.SKILL)
            return;
        if(CardModifierManager.hasModifier(drawnCard,HuntModifier.ID))
            return;
        applied = true;
        this.flash();
        drawnCard.flash();
        CardModifierManager.addModifier(drawnCard,new HuntModifier());
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new HuntAction());
    }

    @Override
    public String getUpdatedDescription() {
        return (ModConfig.all_fans?DESCRIPTIONS[1] : DESCRIPTIONS[0]) +DESCRIPTIONS[2];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HunterSee();
    }
}

