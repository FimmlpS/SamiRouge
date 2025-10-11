package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class DeepBurn extends CustomRelic {

    public static final String ID = "samirg:DeepBurn";
    private static final String IMG = "SamiRougeResources/img/relics/DeepBurn.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/DeepBurn_O.png";

    public DeepBurn(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        AbstractCard c = new Burn();
        c.upgrade();
        addToBot(new MakeTempCardInDiscardAction(c,3));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}





