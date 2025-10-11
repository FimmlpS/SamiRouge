package SamiRouge.relics;

import SamiRouge.actions.EveryNightAction;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class EveryNight extends CustomRelic {

    public static final String ID = "samirg:EveryNight";
    private static final String IMG = "SamiRougeResources/img/relics/EveryNight.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/EveryNight_O.png";

    boolean triggered = false;
    boolean secondTurn = false;

    public EveryNight(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        triggered = false;
        secondTurn = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        if(!triggered){
            if(!secondTurn){
                secondTurn = true;
            }
            else{
                triggered = true;
                //action
                addToBot(new EveryNightAction(5));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EveryNight();
    }
}







