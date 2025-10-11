package SamiRouge.relics;

import SamiRouge.dungeons.TheSami;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LiveWood extends CustomRelic {

    public static final String ID = "samirg:LiveWood";
    private static final String IMG = "SamiRougeResources/img/relics/LiveWood.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/LiveWood_O.png";

    boolean triggered = false;

    public LiveWood(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.UNCOMMON,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atTurnStart() {
        if(triggered)
            return;
        triggered = true;
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new GainBlockAction(AbstractDungeon.player,(int) (AbstractDungeon.player.maxHealth*(AbstractDungeon.id.equals(TheSami.ID)?0.4F:0.2F))));
    }

    @Override
    public void atPreBattle() {
        triggered = false;
    }

    @Override
    public void onVictory() {
        triggered = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LiveWood();
    }
}


