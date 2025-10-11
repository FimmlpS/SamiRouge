package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SetHpToOneAction extends AbstractGameAction {
    public SetHpToOneAction(AbstractMonster monster){
        this.monster = monster;
    }

    AbstractMonster monster;

    @Override
    public void update() {
        monster.currentHealth = 1;
        monster.healthBarUpdatedEvent();

        this.isDone = true;
    }
}
