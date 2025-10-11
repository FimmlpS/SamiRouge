package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;

public class DelayActionAction extends AbstractGameAction {
    public DelayActionAction(AbstractGameAction action){
        this(new ArrayList<>());
        delays.add(action);
    }

    public DelayActionAction(AbstractGameAction action1,AbstractGameAction action2){
        this(new ArrayList<>());
        delays.add(action1);
        delays.add(action2);
    }

    ArrayList<AbstractGameAction> delays;

    public DelayActionAction(ArrayList<AbstractGameAction> actions){
        delays = actions;
    }

    @Override
    public void update() {
        for(AbstractGameAction action:delays){
            addToBot(action);
        }
        this.isDone = true;
    }
}
