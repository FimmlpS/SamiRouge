package SamiRouge.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

public class CreateIntentAction extends AbstractGameAction {
    public CreateIntentAction(AbstractMonster m){
        this.m  = m;
    }

    @Override
    public void update() {
        EnemyMoveInfo move = ReflectionHacks.getPrivate(m,AbstractMonster.class,"move");
        if(move!=null){
            m.createIntent();
        }
        this.isDone = true;
    }

    AbstractMonster m;
}
