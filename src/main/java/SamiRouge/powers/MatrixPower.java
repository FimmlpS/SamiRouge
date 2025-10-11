package SamiRouge.powers;

import SamiRouge.actions.InactivateMatrixAction;
import SamiRouge.cards.skill.ActivateMatrix_SamiRouge;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MatrixPower extends AbstractPower {
    public static final String POWER_ID = "samirg:MatrixPower";
    private static final PowerStrings powerStrings;

    public MatrixPower(AbstractCreature owner, int amt){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amt;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Matrix_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Matrix_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if(!card.cardID.equals(ActivateMatrix_SamiRouge.ID)){
            this.flash();
            addToBot(new ReducePowerAction(this.owner,this.owner,this,1));
        }
    }

    @Override
    public void onRemove() {
        addToTop(new InactivateMatrixAction());
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
