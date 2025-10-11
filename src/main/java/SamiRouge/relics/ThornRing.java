package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class ThornRing extends CustomRelic {

    public static final String ID = "samirg:ThornRing";
    private static final String IMG = "SamiRougeResources/img/relics/ThornRing.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/ThornRing_O.png";

    public ThornRing(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.UNCOMMON,LandingSound.MAGICAL);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onTrigger() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}




