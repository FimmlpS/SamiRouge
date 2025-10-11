package SamiRouge.modifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ExtremelyColdModifier extends AbstractCardModifier {
    public static String ID = "samirg:ExtremelyColdModifier";
    private static final UIStrings uiStrings;

    public ExtremelyColdModifier(){}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return CardModifierManager.getModifiers(card,ID).size() <3;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {

    }

    @Override
    public void onRemove(AbstractCard card) {

    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new LoseEnergyAction(1));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ExtremelyColdModifier();
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


