package SamiRouge.relics;

import SamiRouge.blights.IrreversibleMatrix;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DimensionalFluidity extends CustomRelic {

    public static final String ID = "samirg:DimensionalFluidity";
    private static final String IMG = "SamiRougeResources/img/relics/DimensionalFluidity.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/DimensionalFluidity_O.png";

    public DimensionalFluidity(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        int amt = 0;
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!=null)
            amt = ir.counter*2;
        if(amt>0){
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MetallicizePower(AbstractDungeon.player,amt),amt));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DimensionalFluidity();
    }
}


