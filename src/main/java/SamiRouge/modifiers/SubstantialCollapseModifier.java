package SamiRouge.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SubstantialCollapseModifier extends AbstractCardModifier {
    public static String ID = "samirg:SubstantialCollapseModifier";

    public SubstantialCollapseModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return CardModifierManager.getModifiers(card,ID).size()<4;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if(card.baseDamage>0)
            card.baseDamage -= 1;
        card.damage = card.baseDamage;
    }

    @Override
    public void onRemove(AbstractCard card) {
        if(card.baseDamage>=0)
            card.baseDamage += 1;
        card.damage = card.baseDamage;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SubstantialCollapseModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}
