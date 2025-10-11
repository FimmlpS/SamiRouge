package SamiRouge.actions;

import SamiRouge.helper.CardHelper;
import SamiRouge.modifiers.HighColdModifier;
import SamiRouge.modifiers.IcyDirtModifier;
import SamiRouge.modifiers.PolarRegionModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;

public class MarkOneGDJAction extends AbstractGameAction {
    public MarkOneGDJAction(CardGroup group) {
        this.group = group;
    }

    CardGroup group;

    public void update() {
        ArrayList<AbstractCard> cards = new ArrayList<>(group.group);
        if (cards.isEmpty()) {
            this.isDone = true;
            return;
        }
        Collections.shuffle(cards, AbstractDungeon.cardRandomRng.random);
        ArrayList<AbstractCard> privilege = CardHelper.getGDJPrivilegeCards(cards);

        AbstractCard c = privilege.get(0);
        CardHelper.markRandomGDJ(c);
        c.flash();
        this.isDone = true;
    }
}
