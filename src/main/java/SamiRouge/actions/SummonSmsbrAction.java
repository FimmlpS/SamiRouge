package SamiRouge.actions;

import SamiRouge.monsters.Smdrn;
import SamiRouge.monsters.Smsbr;
import SamiRouge.monsters.Smwiz;
import basemod.BaseMod;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SummonSmsbrAction extends AbstractGameAction {
    public SummonSmsbrAction(float targetX,float targetY){
        x= targetX;
        y= targetY;
        this.startDuration = duration = 0.9F;
    }

    float x;
    float y;
    Smsbr sbr;

    @Override
    public void update() {
        if(startDuration==duration){
            if(BaseMod.hasModID("spireTogether:")){
                int count = 0;
                for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(m instanceof Smsbr || m instanceof Smwiz){
                        count++;
                    }
                    if(count>=2){
                        this.isDone = true;
                        return;
                    }
                }
            }

            sbr = new Smsbr(800F,800F);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(sbr);
            }
            sbr.drawX = x;
            sbr.drawY = y;
            sbr.animX = 1000F* Settings.xScale;
            sbr.state.setAnimation(0,"Run",true);
            sbr.state.addAnimation(0,"Idle",true,0F);
            sbr.init();
            sbr.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(0, sbr);
            sbr.useUniversalPreBattleAction();
            sbr.createIntent();
        }
        this.tickDuration();
        if(this.isDone){
            sbr.initializeSpine();
            sbr.animX = 0F;
            sbr.showHealthBar();
            sbr.usePreBattleAction();
        }
        else{
            sbr.animX = Interpolation.fade.apply(0.0F, 1000.0F * Settings.xScale, this.duration);
        }
    }
}

