package SamiRouge.modifiers;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class CycleModifier extends AbstractCardModifier {
    public static String ID = "samirg:CycleModifier";

    public CycleModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringHelper.CARD_MODIFIERS.TEXT[6];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CycleModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}


