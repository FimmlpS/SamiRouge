package SamiRouge.modifiers;

import SamiRouge.actions.HuntAction;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ForeverModifier extends AbstractCardModifier {
    public static String ID = "samirg:ForeverModifier";
    private static final UIStrings uiStrings;

    public ForeverModifier(){}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card,ID);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {

    }

    @Override
    public void onRemove(AbstractCard card) {

    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ForeverModifier();
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return uiStrings.TEXT[0] + (Settings.lineBreakViaCharacter ? " " : "") + LocalizedStrings.PERIOD + " NL " + rawDescription;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    }
}

