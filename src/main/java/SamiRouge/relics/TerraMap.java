package SamiRouge.relics;

import SamiRouge.actions.MarkDrawGDJAction;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class TerraMap extends CustomRelic {

    public static final String ID = "samirg:TerraMap";
    private static final String IMG = "SamiRougeResources/img/relics/TerraMap.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/TerraMap_O.png";

    public TerraMap(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.FLAT);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new MarkDrawGDJAction());
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}



