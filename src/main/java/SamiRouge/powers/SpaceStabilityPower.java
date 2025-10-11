package SamiRouge.powers;

import SamiRouge.cards.skill.ActivateMatrix_SamiRouge;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpaceStabilityPower extends AbstractPower {
    public static final String POWER_ID = "samirg:SpaceStabilityPower";
    private static final PowerStrings powerStrings;
    boolean upgrade;

    public SpaceStabilityPower(AbstractCreature owner, int amt, boolean upgrade){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amt;
        this.upgrade =upgrade;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/SpaceStability_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/SpaceStability_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        String des = powerStrings.DESCRIPTIONS[0]+this.amount + powerStrings.DESCRIPTIONS[1]+(upgrade?powerStrings.DESCRIPTIONS[3]:powerStrings.DESCRIPTIONS[2]);
        this.description = des;
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        AbstractCard c = new ActivateMatrix_SamiRouge();
        if(upgrade)
            c.upgrade();
        addToBot(new MakeTempCardInHandAction(c,this.amount));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}


