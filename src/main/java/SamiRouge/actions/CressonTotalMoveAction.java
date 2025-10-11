package SamiRouge.actions;

import SamiRouge.monsters.Cresson;
import SamiRouge.monsters.Nursery;
import SamiRouge.powers.NonAndExistencePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CressonTotalMoveAction extends AbstractGameAction {
    public CressonTotalMoveAction(Cresson cresson){
        this.cresson = cresson;
    }

    Cresson cresson;

    @Override
    public void update() {
        if(cresson==null||cresson.isDeadOrEscaped()||!cresson.canMove()){
            this.isDone = true;
            return;
        }

        ArrayList<AbstractGameAction> actionToAdd = new ArrayList<>();
        actionToAdd.add(0,new ChangeStateAction(cresson, "MOVE_START"));
        actionToAdd.add(0,new LongWaitAction(cresson.move01));
        actionToAdd.add(0,new CressonMoveAction(cresson));
        float tmpX = cresson.posX[cresson.curX*3+cresson.curY];
        float tmpY = cresson.posY[cresson.curX*3+cresson.curY];
        Nursery nursery = new Nursery(tmpX,tmpY);
        nursery.setCresson(cresson,cresson.curX*3+cresson.curY);
        actionToAdd.add(0,new SpawnMonsterAction(nursery,true, AbstractDungeon.getMonsters().monsters.size()));
        if(AbstractDungeon.ascensionLevel>=19)
            actionToAdd.add(0,new CreateIntentAction(nursery));
        actionToAdd.add(0,new ApplyPowerAction(nursery,cresson,new NonAndExistencePower(nursery)));
        actionToAdd.add(0,new ChangeStateAction(cresson,"MOVE_END"));
        actionToAdd.add(0,new LongWaitAction(0.2F));

        for(AbstractGameAction action:actionToAdd){
            addToTop(action);
        }

        this.isDone = true;
    }
}
