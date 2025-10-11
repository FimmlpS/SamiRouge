package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RoadVine extends CustomRelic {

    public static final String ID = "samirg:RoadVine";
    private static final String IMG = "SamiRougeResources/img/relics/RoadVine.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/RoadVine_O.png";

    public RoadVine(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.SOLID);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onTrigger() {
        this.flash();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RoadVine();
    }
}




