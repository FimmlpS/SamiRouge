package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class IncreaseMaxHpAction extends AbstractGameAction {
    public IncreaseMaxHpAction(float blv) {
        this.amount = (int) (blv* AbstractDungeon.player.maxHealth);
    }

    public IncreaseMaxHpAction(boolean none,int amount) {
        this.amount = amount;
    }

    public void update() {
        AbstractDungeon.player.increaseMaxHp(amount, true);
        this.isDone = true;
    }
}
