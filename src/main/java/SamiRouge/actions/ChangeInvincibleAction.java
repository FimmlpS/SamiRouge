package SamiRouge.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;

public class ChangeInvincibleAction extends AbstractGameAction {
    public ChangeInvincibleAction(AbstractMonster monster,float blv){
        this.blv = blv;
        this.monster = monster;
    }

    @Override
    public void update() {
        AbstractPower vin = monster.getPower(InvinciblePower.POWER_ID);
        if(vin!=null){
            int max = ReflectionHacks.getPrivate(vin,InvinciblePower.class,"maxAmt");
            max *= (1F+blv);
            ReflectionHacks.setPrivate(vin,InvinciblePower.class,"maxAmt",max);
            vin.stackPower((int) (vin.amount*blv));
        }
        this.isDone = true;
    }

    AbstractMonster monster;
    float blv;
}
