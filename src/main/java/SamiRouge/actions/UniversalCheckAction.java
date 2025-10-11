package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UniversalCheckAction extends AbstractGameAction {
    public UniversalCheckAction(){
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        for(AbstractMonster monster: AbstractDungeon.getMonsters().monsters){
            monster.useUniversalPreBattleAction();
        }
        this.isDone = true;
    }
}
