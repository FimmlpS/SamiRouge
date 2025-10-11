package SamiRouge.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class KhanPower extends AbstractPower {
    public static final String POWER_ID = "samirg:KhanPower";
    private static final PowerStrings powerStrings;
    boolean special;

    public KhanPower(AbstractCreature owner,int amount){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        special = AbstractDungeon.ascensionLevel>=19;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Khan_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Khan_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99;

    }

    @Override
    public void updateDescription() {
        this.description = (special?powerStrings.DESCRIPTIONS[2]:powerStrings.DESCRIPTIONS[0]) + this.amount*5+powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        return super.canPlayCard(card);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if(damageType== DamageInfo.DamageType.NORMAL){
            float blv = (1F-0.05F*this.amount);
            if(blv<0)
                blv = 0;
            return damage*blv;
        }
        return damage;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if(type== DamageInfo.DamageType.NORMAL){
            float blv = (1F+0.05F*this.amount);
            return damage*blv;
        }
        return damage;
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if(card.type!= AbstractCard.CardType.ATTACK)
            return;
        this.flashWithoutSound();
        this.amount++;
        this.updateDescription();
        AbstractDungeon.onModifyPower();
        if(special&&this.amount>=20&&!AbstractDungeon.player.hasPower(KhanCardPower.POWER_ID)){
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new KhanCardPower(AbstractDungeon.player)));
        }
    }

    @Override
    public void atEndOfRound() {
        int half = (this.amount+1)/2;
        if(half>0){
            reducePower(half);
            updateDescription();
            AbstractDungeon.onModifyPower();
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}
