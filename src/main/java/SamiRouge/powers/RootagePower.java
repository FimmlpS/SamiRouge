package SamiRouge.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RootagePower extends AbstractPower {
    public static final String POWER_ID = "samirg:RootagePower";
    private static final PowerStrings powerStrings;
    private int extra = 0;

    public RootagePower(AbstractCreature owner, int amount){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        String path128 = "SamiRougeResources/img/powers_SamiRouge/Rootage_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Rootage_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);

        this.updateDescription();

        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.priority = 98;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0]+this.extra+powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(info.type!= DamageInfo.DamageType.NORMAL){
            return damageAmount*2;
        }
        return super.onAttacked(info, damageAmount);
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage + (float)this.extra : damage;
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if(damageAmount>0){
            this.flashWithoutSound();
            this.extra+=this.amount;
            if(this.owner instanceof AbstractMonster){
                ((AbstractMonster)this.owner).applyPowers();
            }
        }
    }

    @Override
    public void atEndOfRound() {
        this.extra = 0;
        if(this.owner instanceof AbstractMonster){
            ((AbstractMonster)this.owner).applyPowers();
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}





