package SamiRouge.powers;

import SamiRouge.actions.ReduceColdnessAction;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

public class ColdnessPower extends AbstractPower {
    public static final String POWER_ID = "samirg:ColdnessPower";
    private static final PowerStrings powerStrings;

    int maxLevel = 16;
    boolean more;

    public ColdnessPower(AbstractCreature owner, int maxLevel){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.maxLevel = maxLevel;
        this.amount = maxLevel;

        String path128 = "SamiRougeResources/img/powers_SamiRouge/Coldness_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Coldness_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        more = AbstractDungeon.ascensionLevel>=19;
        this.updateDescription();

        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.priority = 97;

    }

    private void reset(){
        this.amount = this.maxLevel;
        addToBot(new ApplyPowerAction(AbstractDungeon.player,this.owner,new ConstrictedPower(AbstractDungeon.player,this.owner,more?4:3),more?4:3));
    }

    public void resetColdness(int maxLevel){
        if(maxLevel>0)
            this.maxLevel = maxLevel;
        reset();
        this.flashWithoutSound();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);
        this.flash();
        addToBot(new ReduceColdnessAction(this.owner,this.owner,this,(this.amount+1)/2));
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if(this.amount==0){
            reset();
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if(damageType== DamageInfo.DamageType.NORMAL)
            return (damage-this.amount>0)?(damage-this.amount):0;
        return super.atDamageReceive(damage, damageType);
    }

    @Override
    public void updateDescription() {
        String des = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1]+this.maxLevel+powerStrings.DESCRIPTIONS[2]+(more?4:3)+powerStrings.DESCRIPTIONS[3];
        this.description = des;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}

