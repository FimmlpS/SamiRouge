package SamiRouge.relics;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class WarnWood extends CustomRelic {

    public static final String ID = "samirg:WarnWood";
    private static final String IMG = "SamiRougeResources/img/relics/WarnWood.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/WarnWood_O.png";

    public WarnWood(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onEquip() {
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!=null&&ir.counter>0){
            ir.flash();
            ir.setCounter(ir.counter-1);
        }
    }

    @Override
    public boolean canSpawn() {
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!=null&&ir.counter>0)
            return true;
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WarnWood();
    }
}


