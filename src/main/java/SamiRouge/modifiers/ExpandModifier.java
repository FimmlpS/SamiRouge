package SamiRouge.modifiers;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ExpandModifier extends AbstractCardModifier {
    public static String ID = "samirg:ExpandModifier";

    public ExpandModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringHelper.CARD_MODIFIERS.TEXT[7];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExpandModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}


