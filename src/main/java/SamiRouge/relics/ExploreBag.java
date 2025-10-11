package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ExploreBag extends CustomRelic {

    public static final String ID = "samirg:ExploreBag";
    private static final String IMG = "SamiRougeResources/img/relics/ExploreBag.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/ExploreBag_O.png";

    public ExploreBag(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onTrigger() {
        this.flash();
        addToBot(new DrawCardAction(1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ExploreBag();
    }
}






