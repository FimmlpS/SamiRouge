package SamiRouge.actions;

import SamiRouge.monsters.Cresson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CressonMoveAction extends AbstractGameAction {

    public CressonMoveAction(Cresson cresson){
        this.cresson =cresson;
    }

    Cresson cresson;

    @Override
    public void update() {
        if(cresson!=null){
            ArrayList<Integer> index = new ArrayList<>();
            for(int i =0;i<cresson.entities.length;i++){
                if(!cresson.entities[i])
                    index.add(i);
            }
            int foreX = cresson.curX;
            int foreY = cresson.curY;
            int randomIndex = -1;
            if(index.size()==0){

            }
            else if(index.size()==1){
                randomIndex = index.get(0);
            }
            else
                randomIndex = index.get(AbstractDungeon.monsterRng.random(index.size()-1));
            int x = randomIndex/3;
            cresson.setPosition(x,randomIndex-x*3);
        }
        this.isDone = true;
    }
}
