package SamiRouge.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LionCardPower extends AbstractPower {
    public static final String POWER_ID = "samirg:LionCardPower";
    private static final PowerStrings powerStrings;

    public LionCardPower(AbstractCreature owner){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/LionCard_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/LionCard_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.DEBUFF;
        this.priority = 99;
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if(card.type == AbstractCard.CardType.SKILL){
            card.cantUseMessage = powerStrings.DESCRIPTIONS[1];
            return false;
        }
        return true;
    }


    @Override
    public void atEndOfRound() {
        addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }


    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}


