package SamiRouge.actions;

import SamiRouge.monsters.Smkght;
import SamiRouge.monsters.Smshdw;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class SummonSmshdwAction extends AbstractGameAction {
    public SummonSmshdwAction(float targetX,float targetY){
        x= targetX;
        y= targetY;
        this.startDuration = duration = 0.8F;
    }

    float x;
    float y;
    AbstractMonster shdw;

    private int getSmartPosition(AbstractMonster m) {
        int position = 0;

        for(Iterator var2 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator(); var2.hasNext(); ++position) {
            AbstractMonster mo = (AbstractMonster)var2.next();
            if (!(m.drawX > mo.drawX)) {
                break;
            }
        }

        return position;
    }

    @Override
    public void update() {
        if(startDuration==duration){
            boolean khanAlive = false;
            for(AbstractMonster monster: AbstractDungeon.getMonsters().monsters){
                if(monster instanceof Smkght){
                    if(!monster.isDeadOrEscaped())
                        khanAlive = true;
                }
            }
            if(!khanAlive){
                this.isDone = true;
                return;
            }

            shdw = new Smshdw(800F,800F);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(shdw);
            }
            shdw.drawX = x;
            shdw.drawY = y;
            shdw.animX = -1200F* Settings.xScale;
            shdw.init();
            shdw.applyPowers();
            shdw.state.setAnimation(0,"Move",true);
            AbstractDungeon.getCurrRoom().monsters.addMonster(getSmartPosition(shdw),shdw);
            this.addToBot(new ApplyPowerAction(shdw, shdw, new MinionPower(shdw)));
            shdw.useUniversalPreBattleAction();
            shdw.createIntent();
        }
        this.tickDuration();
        if(this.isDone){
            shdw.animX = 0F;
            shdw.state.setAnimation(0,"Idle",true);
            shdw.showHealthBar();
            shdw.usePreBattleAction();
        }
        else{
            shdw.animX = Interpolation.fade.apply(0.0F, -1200.0F * Settings.xScale, this.duration);
        }
    }
}
