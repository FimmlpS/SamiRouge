package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ShellShield extends CustomRelic {
    public static final String ID = "samirg:ShellShield";
    private static final String IMG = "SamiRougeResources/img/relics/ShellShield.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/ShellShield_O.png";

    public ShellShield(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O), AbstractRelic.RelicTier.UNCOMMON,LandingSound.SOLID);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MalleablePower(AbstractDungeon.player,3),3));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}


