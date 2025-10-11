package SamiRouge.relics;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MidMachine extends CustomRelic {

    public static final String ID = "samirg:MidMachine";
    private static final String IMG = "SamiRougeResources/img/relics/MidMachine.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/MidMachine_O.png";

    public MidMachine(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.CLINK);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onVictory() {
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!=null){
            int c = ir.counter;
            if(c>0){
                this.flash();
                this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractPlayer p = AbstractDungeon.player;
                if (p.currentHealth > 0) {
                    p.heal(c);
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MidMachine();
    }
}






