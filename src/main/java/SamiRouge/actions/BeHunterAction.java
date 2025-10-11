package SamiRouge.actions;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.relics.ToBeAHunter;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class BeHunterAction extends AbstractGameAction {
    public BeHunterAction(ToBeAHunter toBeAHunter){
        this.toBeAHunter = toBeAHunter;
    }

    @Override
    public void update() {
        if(toBeAHunter!=null){
            AbstractMonster enemy = null;
            for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                if(!m.isDeadOrEscaped()){
                    if(enemy==null||enemy.currentHealth<m.currentHealth){
                        enemy = m;
                    }
                }
            }
            if(enemy!=null){
                toBeAHunter.attackAnimation();
                int baseDMG = 2;
                //2025/6/9 UPDATE
                AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
                if(ir!=null){
                    baseDMG+= Math.max(0,ir.counter);
                }
                int buffAmt = 0;
                AbstractPower weak = enemy.getPower(WeakPower.POWER_ID);
                if(weak!=null){
                    buffAmt+= Math.max(0,weak.amount);
                }
                AbstractPower vul = enemy.getPower(VulnerablePower.POWER_ID);
                if(vul!=null){
                    buffAmt+= Math.max(0,vul.amount);
                }
                baseDMG += Math.min(10,buffAmt);
                if(enemy.type == AbstractMonster.EnemyType.BOSS){
                    baseDMG+= 2;
                }
                for(int i =0;i<5;i++){
                    addToTop(new DamageAction(enemy,new DamageInfo(AbstractDungeon.player,baseDMG, DamageInfo.DamageType.THORNS),AttackEffect.SLASH_HORIZONTAL));
                }
            }
        }
        this.isDone = true;
    }

    ToBeAHunter toBeAHunter;
}
