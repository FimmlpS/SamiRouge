package SamiRouge.helper;

import SamiRouge.modifiers.HighColdModifier;
import SamiRouge.modifiers.IcyDirtModifier;
import SamiRouge.modifiers.PolarRegionModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CardHelper {
    public static ArrayList<AbstractCard> getGDJPrivilegeCards(ArrayList<AbstractCard> group){
        ArrayList<AbstractCard> privilegeCards = new ArrayList<>();
        for (AbstractCard c : group){
            if(c.type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.SKILL){
                privilegeCards.add(c);
            }
        }
        for (AbstractCard c : group){
            if(c.type == AbstractCard.CardType.POWER){
                privilegeCards.add(c);
            }
        }
        for(AbstractCard c :group){
            if(c.type != AbstractCard.CardType.ATTACK && c.type != AbstractCard.CardType.SKILL && c.type != AbstractCard.CardType.POWER){
                privilegeCards.add(c);
            }
        }
        return privilegeCards;
    }

    public static void markRandomGDJ(AbstractCard c){
        int i = AbstractDungeon.cardRandomRng.random(0, 2);
        if (i == 0) {
            CardModifierManager.addModifier(c, new HighColdModifier());
        } else if (i == 1) {
            CardModifierManager.addModifier(c, new IcyDirtModifier());
        } else if (i == 2) {
            CardModifierManager.addModifier(c, new PolarRegionModifier());
        }
    }
}
