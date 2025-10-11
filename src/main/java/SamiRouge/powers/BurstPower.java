package SamiRouge.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BurstPower extends AbstractPower {
    public static final String POWER_ID = "samirg:BurstPower";
    private static final PowerStrings powerStrings;

    private void calculateAmount(){
        if(this.owner.maxHealth<=0)
            return;
        int tmpAmt = (int)(((float) (this.owner.maxHealth-this.owner.currentHealth))*100F/(float)this.owner.maxHealth);
        if(tmpAmt>50)
            tmpAmt = 50;
        if(this.amount!=tmpAmt){
            this.amount = tmpAmt;
            updateDescription();
        }
    }

    public BurstPower(AbstractCreature owner){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Burst_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Burst_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        calculateAmount();
        this.updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.DEBUFF;
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if(type== DamageInfo.DamageType.NORMAL){
            calculateAmount();
            float deHp = (float)this.amount / 100F;
            if(deHp>0.5F)
                deHp = 0.5F;
            return damage*(1F+deHp);
        }
        return damage;
    }

    @Override
    public void update(int slot) {
        calculateAmount();
        super.update(slot);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}




