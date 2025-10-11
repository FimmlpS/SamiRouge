package SamiRouge.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class RemoveLastDebuffAction extends AbstractGameAction {
    public RemoveLastDebuffAction(AbstractCreature c) {
        this.c = c;
    }

    AbstractCreature c;

    @Override
    public void update() {
        if (c.powers.size() > 0) {
            for (int i = c.powers.size() - 1; i >= 0; i--) {
                AbstractPower p = c.powers.get(i);
                if (p.type == AbstractPower.PowerType.DEBUFF) {
                    this.addToTop(new RemoveSpecificPowerAction(this.c, this.c, p.ID));
                    break;
                }
            }
            this.isDone = true;
        }
    }
}
