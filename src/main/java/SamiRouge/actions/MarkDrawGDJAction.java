package SamiRouge.actions;

import SamiRouge.helper.CardHelper;
import SamiRouge.modifiers.HighColdModifier;
import SamiRouge.modifiers.IcyDirtModifier;
import SamiRouge.modifiers.PolarRegionModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Collections;

public class MarkDrawGDJAction extends AbstractGameAction {
    public MarkDrawGDJAction() {}

    public void update() {
        ArrayList<AbstractCard> cards = new ArrayList<>(AbstractDungeon.player.drawPile.group);
        Collections.shuffle(cards,AbstractDungeon.cardRandomRng.random);
        ArrayList<AbstractCard> privilege = CardHelper.getGDJPrivilegeCards(cards);
        for(int i =0;i<privilege.size();i++){
            AbstractCard c = privilege.get(i);
            if(i == 0){
                CardModifierManager.addModifier(c,new HighColdModifier());
            }
            else if (i == 1){
                CardModifierManager.addModifier(c,new IcyDirtModifier());
            }
            else if (i == 2){
                CardModifierManager.addModifier(c,new PolarRegionModifier());
                break;
            }
        }
        this.isDone = true;
    }
}
