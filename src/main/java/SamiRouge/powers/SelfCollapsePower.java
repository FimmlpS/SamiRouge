package SamiRouge.powers;

import SamiRouge.monsters.WoodCrack;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class SelfCollapsePower extends AbstractPower {
    public static final String POWER_ID = "samirg:SelfCollapsePower";
    private static final PowerStrings powerStrings;
    private static int idOffset = 0;

    WoodCrack ml = null;
    boolean hasTrigger = false;
    AbstractCard curse;
    int talk;
    int triggerAmount;
    boolean more;

    public SelfCollapsePower(AbstractMonster owner, int triggerAmount, int blockAmount, int talkTime, AbstractCard curse){
        idOffset++;
        this.name = powerStrings.NAME;
        this.ID = POWER_ID+idOffset;
        if(owner instanceof WoodCrack)
            this.ml = (WoodCrack) owner;
        this.owner = owner;
        this.amount = blockAmount;
        this.curse = curse;
        this.triggerAmount = triggerAmount;
        this.talk = talkTime;

        String path128 = "SamiRougeResources/img/powers_SamiRouge/SelfCollapse_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/SelfCollapse_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);

        this.updateDescription();
        more = AbstractDungeon.ascensionLevel>=19;

        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.priority = 98;
    }

    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        if(!hasTrigger&&owner!=null&&owner.currentHealth-damageAmount<triggerAmount){
            hasTrigger = true;
            if(more){
                addToTop(new ApplyPowerAction(this.owner,this.owner,new MetallicizePower(this.owner,4),4));
                addToTop(new ApplyPowerAction(this.owner,this.owner,new StrengthPower(this.owner,4),4));
            }
            addToTop(new ApplyPowerAction(this.owner,this.owner,new IntangiblePlayerPower(this.owner,1),1));
            addToTop(new GainBlockAction(this.owner,this.amount));
            addToTop(new MakeTempCardInHandAction(curse));
            addToBot(new RemoveSpecificPowerAction(this.owner,this.owner,this));
            if(ml!=null)
                ml.talk(this.talk);
        }
        super.wasHPLost(info, damageAmount);
    }

    @Override
    public void updateDescription() {
        String des = powerStrings.DESCRIPTIONS[0]+triggerAmount+powerStrings.DESCRIPTIONS[1]+ curse.name +powerStrings.DESCRIPTIONS[2] + this.amount + powerStrings.DESCRIPTIONS[3];
        this.description = des;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}
