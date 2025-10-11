package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class UrsasBlade extends CustomRelic {

    public static final String ID = "samirg:UrsasBlade";
    private static final String IMG = "SamiRougeResources/img/relics/UrsasBlade.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/UrsasBlade_O.png";

    public UrsasBlade(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.UNCOMMON,LandingSound.CLINK);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new UrsasBlade();
    }
}
