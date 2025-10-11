package SamiRouge.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DamageOtherAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private boolean skipWait;
    private boolean muteSfx;

    AbstractCreature extraTarget;

    public DamageOtherAction(AbstractCreature target1,AbstractCreature target2, DamageInfo info, AttackEffect effect) {
        this.skipWait = false;
        this.muteSfx = false;
        this.info = info;
        this.setValues(target1, info);
        extraTarget = target2;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
    }

    public void update() {
        if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
        } else {
            if (this.duration == 0.1F) {
                if (this.info.type != DamageInfo.DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                    this.isDone = true;
                    return;
                }

                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
                if(extraTarget!=null)
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.extraTarget.hb.cX,this.extraTarget.hb.cY,this.attackEffect,this.muteSfx));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.attackEffect == AttackEffect.POISON) {
                    this.target.tint.color.set(Color.CHARTREUSE.cpy());
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    if(extraTarget!=null){
                        this.extraTarget.tint.color.set(Color.CHARTREUSE.cpy());
                        this.extraTarget.tint.changeColor(Color.WHITE.cpy());
                    }
                } else if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color.set(Color.RED);
                    this.target.tint.changeColor(Color.WHITE.cpy());
                    if(extraTarget!=null){
                        this.extraTarget.tint.color.set(Color.RED);
                        this.extraTarget.tint.changeColor(Color.WHITE.cpy());
                    }
                }

                this.target.damage(this.info);
                if(extraTarget!=null)
                    this.extraTarget.damage(this.info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                if (!this.skipWait && !Settings.FAST_MODE) {
                    this.addToTop(new WaitAction(0.1F));
                }
            }

        }
    }
}
