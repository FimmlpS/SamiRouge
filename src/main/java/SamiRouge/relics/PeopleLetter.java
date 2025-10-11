package SamiRouge.relics;

import SamiRouge.actions.MarkOneGDJAction;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class PeopleLetter extends CustomRelic {

    public static final String ID = "samirg:PeopleLetter";
    private static final String IMG = "SamiRougeResources/img/relics/PeopleLetter.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/PeopleLetter_O.png";

    public PeopleLetter(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.FLAT);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new MarkOneGDJAction(AbstractDungeon.player.hand));
        addToBot(new MarkOneGDJAction(AbstractDungeon.player.drawPile));
        addToBot(new MarkOneGDJAction(AbstractDungeon.player.discardPile));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}






