package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class HuntAction extends AbstractGameAction {
    @Override
    public void update() {

        int level = 1;

        AbstractMonster m = AbstractDungeon.getRandomMonster();
        if(m!=null){
            boolean hasVul = m.hasPower("Weakened");
            if(!hasVul){
                addToTop(new ApplyPowerAction(m,AbstractDungeon.player,new WeakPower(m,level,false)));

            }
            else{
                addToTop(new ApplyPowerAction(m,AbstractDungeon.player,new VulnerablePower(m,level,false),level));
            }

        }

        this.isDone=true;
    }
}

