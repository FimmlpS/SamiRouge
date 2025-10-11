package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HatOfTreeScar extends CustomRelic {

    public static final String ID = "samirg:HatOfTreeScar";
    private static final String IMG = "SamiRougeResources/img/relics/HatOfTreeScar.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HatOfTreeScar_O.png";

    public HatOfTreeScar(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.HEAVY);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(targetCard.costForTurn>=3){
            this.flash();
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HatOfTreeScar();
    }
}







