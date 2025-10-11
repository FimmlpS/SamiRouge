package SamiRouge.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;

public class OneLivePower extends AbstractPower {
    public static final String POWER_ID = "samirg:OneLivePower";
    private static final PowerStrings powerStrings;
    boolean triggered = false;

    public OneLivePower(AbstractCreature owner,int amt){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.amount = amt;
        this.owner = owner;
        this.loadRegion("time");
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public void atEndOfRound() {
        triggered = false;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!triggered){
            if(info.type== DamageInfo.DamageType.NORMAL){
                triggered = true;
                AbstractDungeon.actionManager.callEndTurnEarlySequence();
                CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
                AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
                AbstractDungeon.topLevelEffectsQueue.add(new TimeWarpTurnEndEffect());
                addToTop(new RemoveSpecificPowerAction(this.owner,this.owner,this));
                addToTop(new GainBlockAction(this.owner,this.amount));
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}








