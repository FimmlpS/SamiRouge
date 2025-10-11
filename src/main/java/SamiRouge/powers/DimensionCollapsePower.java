package SamiRouge.powers;

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
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DimensionCollapsePower extends AbstractPower {
    public static final String POWER_ID = "samirg:DimensionCollapsePower";
    private static final PowerStrings powerStrings;

    public DimensionCollapsePower(AbstractCreature owner, int amt){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amt;
        this.loadRegion("lockon");
        this.updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.DEBUFF;
        this.priority = 99;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        addToBot(new ReducePowerAction(this.owner,this.owner,this,1));
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if(card.cardID.equals(ActivateMatrix_SamiRouge.ID)){
            card.cantUseMessage = powerStrings.DESCRIPTIONS[2];
            return false;
        }
        return super.canPlayCard(card);
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
