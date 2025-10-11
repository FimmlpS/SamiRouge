package SamiRouge.modifiers;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;

public class HighColdModifier extends AbstractCardModifier {
    public static String ID = "samirg:HighColdModifier";

    public HighColdModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return true;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        int cost = card.costForTurn;
        if(card.cost == -1)
            cost = card.energyOnUse;
        if(cost>0)
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(2*cost));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringHelper.CARD_MODIFIERS.TEXT[0] + (Settings.lineBreakViaCharacter ? " " : "") + LocalizedStrings.PERIOD;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new HighColdModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
