package SamiRouge.actions;

import SamiRouge.monsters.Smdrn;
import basemod.BaseMod;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SummonSmdrnAction extends AbstractGameAction {
    public SummonSmdrnAction(float targetX,float targetY){
        x= targetX;
        y= targetY;
        this.startDuration = duration = 0.7F;
    }

    float x;
    float y;
    AbstractMonster drn;

    @Override
    public void update() {
        if(startDuration==duration){
            if(BaseMod.hasModID("spireTogether:")){
                int count = 0;
                for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(m instanceof Smdrn){
                        count++;
                    }
                    if(count>=2){
                        this.isDone = true;
                        return;
                    }
                }
            }

            drn = new Smdrn(800F,800F);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(drn);
            }
            drn.drawX = x;
            drn.drawY = y;
            drn.animX = -1200F* Settings.xScale;
            drn.init();
            drn.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(AbstractDungeon.getCurrRoom().monsters.monsters.size(), drn);
            drn.useUniversalPreBattleAction();
            drn.createIntent();
        }
        this.tickDuration();
        if(this.isDone){
            drn.animX = 0F;
            drn.showHealthBar();
            drn.usePreBattleAction();
        }
        else{
            drn.animX = Interpolation.fade.apply(0.0F, -1200.0F * Settings.xScale, this.duration);
        }
    }
}

