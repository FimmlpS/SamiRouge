package SamiRouge.actions;

import SamiRouge.monsters.Smhond;
import SamiRouge.monsters.Smkght;
import SamiRouge.monsters.Smlion;
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

public class SummonSmhondAction extends AbstractGameAction {
    public SummonSmhondAction(float targetX,float targetY){
        x= targetX;
        y= targetY;
        this.startDuration = duration = 0.8F;
    }

    float x;
    float y;
    AbstractMonster shhd;

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
            boolean lionAlive = false;
            for(AbstractMonster monster: AbstractDungeon.getMonsters().monsters){
                if(monster instanceof Smlion){
                    if(!monster.isDeadOrEscaped())
                        lionAlive = true;
                }
            }
            if(!lionAlive){
                this.isDone = true;
                return;
            }

            shhd = new Smhond(800F,800F);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onSpawnMonster(shhd);
            }
            shhd.drawX = x;
            shhd.drawY = y;
            shhd.animX = 1200F* Settings.xScale;
            shhd.init();
            shhd.applyPowers();
            shhd.state.setAnimation(0,"Move",true);
            AbstractDungeon.getCurrRoom().monsters.addMonster(getSmartPosition(shhd), shhd);
            this.addToBot(new ApplyPowerAction(shhd, shhd, new MinionPower(shhd)));
            shhd.useUniversalPreBattleAction();
            shhd.createIntent();
        }
        this.tickDuration();
        if(this.isDone){
            shhd.animX = 0F;
            shhd.state.setAnimation(0,"Idle",true);
            shhd.showHealthBar();
            shhd.usePreBattleAction();
        }
        else{
            shhd.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        }
    }
}
