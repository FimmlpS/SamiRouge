package SamiRouge.actions;

import SamiRouge.effects.AltShockWaveEffect;
import SamiRouge.monsters.Smkght;
import SamiRouge.monsters.Smlion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class MoveTowardAction extends AbstractGameAction {
    public MoveTowardAction(AbstractCreature mover, float xTo){
        this.startDuration = duration = 0.5F;
        this.xTo = xTo;
        this.xFrom = AbstractDungeon.player.drawX;
        this.source = mover;
    }

    float xFrom;
    float xTo;

    @Override
    public void update() {
        if(startDuration==duration){
            if(source instanceof Smkght)
                AbstractDungeon.effectList.add(new ShockWaveEffect(source.hb.cX, source.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC));
            else if(source instanceof Smlion){
                AbstractDungeon.effectList.add(new AltShockWaveEffect(source.hb.cX,source.hb.cY, Settings.LIGHT_YELLOW_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC));
            }
        }
        this.tickDuration();
        if(this.isDone){
            AbstractDungeon.player.drawX = xTo;
        }
        else{
            AbstractDungeon.player.drawX = Interpolation.fade.apply(xTo, xFrom, this.duration);
        }
    }
}
