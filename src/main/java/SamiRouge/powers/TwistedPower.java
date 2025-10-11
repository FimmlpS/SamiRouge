package SamiRouge.powers;

import SamiRouge.actions.MarkExtremelyColdAction;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TwistedPower extends AbstractPower {
    public static final String POWER_ID = "samirg:TwistedPower";
    private static final PowerStrings powerStrings;

    public TwistedPower(AbstractCreature owner){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Twisted_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Twisted_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        addToBot(new MarkExtremelyColdAction());
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}






