package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class RitualBell extends CustomRelic {

    public static final String ID = "samirg:RitualBell";
    private static final String IMG = "SamiRougeResources/img/relics/RitualBell.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/RitualBell_O.png";

    public RitualBell(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.UNCOMMON,LandingSound.CLINK);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onTrigger() {
        this.flash();
        AbstractDungeon.player.increaseMaxHp(5,true);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RitualBell();
    }
}

