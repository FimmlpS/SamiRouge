package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ImprovePackage extends CustomRelic {

    public static final String ID = "samirg:ImprovePackage";
    private static final String IMG = "SamiRougeResources/img/relics/ImprovePackage.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/ImprovePackage_O.png";

    public ImprovePackage(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.UNCOMMON,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    boolean triggered = false;

    @Override
    public void atBattleStart() {
        triggered = false;
    }

    @Override
    public void onPlayerEndTurn() {
        if(triggered)
            return;
        this.flash();
        triggered = true;
        addToBot(new RetainCardsAction(AbstractDungeon.player,3));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ImprovePackage();
    }
}



