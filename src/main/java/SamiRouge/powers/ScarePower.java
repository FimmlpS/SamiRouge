package SamiRouge.powers;

import SamiRouge.actions.InactivateMatrixAction;
import SamiRouge.cards.skill.ActivateMatrix_SamiRouge;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ScarePower extends AbstractPower {
    public static final String POWER_ID = "samirg:ScarePower";
    private static final PowerStrings powerStrings;

    public ScarePower(AbstractCreature owner, int amt){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amt;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Scare_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Scare_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        triggered = false;
        this.updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.DEBUFF;
    }

    boolean triggered;

    public void onCardDraw(AbstractCard card) {
        if(triggered)
            return;
        if (card.cost >= 0) {
            int newCost = card.cost+this.amount;
            if (card.cost != newCost) {
                card.cost = newCost;
                card.costForTurn = card.cost;
                card.isCostModified = true;
            }

            card.freeToPlayOnce = false;
            triggered = true;
            addToTop(new RemoveSpecificPowerAction(this.owner,this.owner,this));
        }

    }

    @Override
    public void updateDescription() {
        String des = powerStrings.DESCRIPTIONS[0]+this.amount+powerStrings.DESCRIPTIONS[1];
        this.description = des;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}

