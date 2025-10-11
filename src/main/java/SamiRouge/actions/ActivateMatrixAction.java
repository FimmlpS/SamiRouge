package SamiRouge.actions;

import SamiRouge.monsters.Cresson;
import SamiRouge.monsters.Nursery;
import SamiRouge.powers.MatrixPower;
import SamiRouge.powers.NonAndExistencePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public class ActivateMatrixAction extends AbstractGameAction {

    public ActivateMatrixAction(int amt){
        this.amount = amt;
    }

    @Override
    public void update() {
        Iterator<AbstractMonster> var1 = AbstractDungeon.getMonsters().monsters.iterator();
        while (var1.hasNext()){
            AbstractMonster m = var1.next();
            if(!m.isDeadOrEscaped()){
                if(m.id.equals(Cresson.ID)||m.id.equals(Nursery.ID))
                    addToTop(new RemoveSpecificPowerAction(m,m, NonAndExistencePower.POWER_ID));
            }
        }
        //add power to self
        addToTop(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MatrixPower(AbstractDungeon.player,this.amount),this.amount));
        this.isDone = true;
    }
}
