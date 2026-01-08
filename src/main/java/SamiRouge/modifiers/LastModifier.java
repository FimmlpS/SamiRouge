package SamiRouge.modifiers;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class LastModifier extends AbstractCardModifier {
    public static String ID = "samirg:LastModifier";

    public LastModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringHelper.CARD_MODIFIERS.TEXT[4];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new LastModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}


