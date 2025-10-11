package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.random.Random;

public class FarScry extends CustomRelic {

    public static final String ID = "samirg:FarScry";
    private static final String IMG = "SamiRougeResources/img/relics/FarScry.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/FarScry_O.png";

    public FarScry() {
        super(ID, ImageMaster.loadImage(IMG), ImageMaster.loadImage(IMG_O), RelicTier.SPECIAL, LandingSound.CLINK);
    }

    public void onEquip() {
        this.counter = 0;
    }

    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter += 2;
        } else {
            ++this.counter;
        }

        if (this.counter == 3) {
            this.counter = 0;
            this.stopPulse();
        } else if (this.counter == 2) {
            this.beginLongPulse();
        }

    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {

        if (drawnCard.baseDamage >= 0) {
            if (counter != 2) {
                drawnCard.flash();
                drawnCard.baseDamage += 2;
            }
            else{
                drawnCard.flash(Color.RED.cpy());
                drawnCard.baseDamage -= 2;
            }
        }
        drawnCard.baseDamage = Math.max(0, drawnCard.baseDamage);
        drawnCard.applyPowers();
    }



    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}




