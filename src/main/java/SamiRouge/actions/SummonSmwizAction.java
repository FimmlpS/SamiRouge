package SamiRouge.actions;

import SamiRouge.monsters.Smsbr;
import SamiRouge.monsters.Smwiz;
import basemod.BaseMod;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SummonSmwizAction extends AbstractGameAction {
    public SummonSmwizAction(float targetX,float targetY){
        x= targetX;
        y= targetY;
        this.startDuration = duration = 0.9F;
    }

    float x;
    float y;
    Smwiz wiz;

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

            wiz = new Smwiz(800F,800F);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(wiz);
            }
            wiz.drawX = x;
            wiz.drawY = y;
            wiz.animX = 1000F* Settings.xScale;
            wiz.state.setAnimation(0,"Run",true);
            wiz.state.addAnimation(0,"Idle",true,0F);
            wiz.init();
            wiz.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(0, wiz);
            wiz.useUniversalPreBattleAction();
            wiz.createIntent();
        }
        this.tickDuration();
        if(this.isDone){
            wiz.initializeSpine();
            wiz.animX = 0F;
            wiz.showHealthBar();
            wiz.usePreBattleAction();
        }
        else{
            wiz.animX = Interpolation.fade.apply(0.0F, 1000.0F * Settings.xScale, this.duration);
        }
    }
}


