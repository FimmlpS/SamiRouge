package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class OperationCall extends CustomRelic {

    public static final String ID = "samirg:OperationCall";
    private static final String IMG = "SamiRougeResources/img/relics/OperationCall.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/OperationCall_O.png";

    public OperationCall(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.BOSS,LandingSound.FLAT);
    }

    @Override
    public int changeNumberOfCardsInReward(int numberOfCards) {
        return numberOfCards+1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}



