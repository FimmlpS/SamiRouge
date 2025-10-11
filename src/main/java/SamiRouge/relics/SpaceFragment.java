package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class SpaceFragment extends CustomRelic {
    public static final String ID = "samirg:SpaceFragment";
    private static final String IMG = "SamiRougeResources/img/relics/SpaceFragment.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/SpaceFragment_O.png";

    public SpaceFragment(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStartPostDraw() {
        this.flash();
        addToBot(new MakeTempCardInHandAction(new VoidCard()));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}




