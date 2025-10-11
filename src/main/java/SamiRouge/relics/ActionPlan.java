package SamiRouge.relics;

import SamiRouge.actions.MarkDrawGDJAction;
import SamiRouge.actions.MarkOneGDJAction;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class ActionPlan extends CustomRelic {

    public static final String ID = "samirg:ActionPlan";
    private static final String IMG = "SamiRougeResources/img/relics/ActionPlan.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/ActionPlan_O.png";

    public ActionPlan(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.FLAT);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new MarkOneGDJAction(AbstractDungeon.player.hand));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}




