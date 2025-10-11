package SamiRouge.actions;

import SamiRouge.monsters.Smdrn;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class SamiDroneMoveAction extends AbstractGameAction {
    public SamiDroneMoveAction(Smdrn smdrn){
        this.duration = startDuration = 0.35F;
        this.sm = smdrn;
    }

    float targetX;
    float targetY;
    float startX;
    float startY;

    @Override
    public void update() {
        if(duration == startDuration){
            targetX = MathUtils.random(sm.leftDraw,sm.rightDraw);
            targetY = MathUtils.random(sm.downDraw,sm.upDraw);
            startX = sm.drawX;
            startY = sm.drawY;
        }
        this.tickDuration();
        if(this.isDone){
//            sm.state.setAnimation(0,"Idle",true);
        }
        else{
            sm.drawX = Interpolation.fade.apply(targetX, startX, this.duration);
            sm.drawY = Interpolation.fade.apply(targetY,startY,this.duration);
        }
    }

    Smdrn sm;
}
