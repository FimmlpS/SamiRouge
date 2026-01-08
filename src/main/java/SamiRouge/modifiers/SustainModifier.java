package SamiRouge.modifiers;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SustainModifier extends AbstractCardModifier {
    public static String ID = "samirg:SustainModifier";

    public SustainModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringHelper.CARD_MODIFIERS.TEXT[5];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SustainModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}


