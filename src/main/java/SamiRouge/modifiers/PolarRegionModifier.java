package SamiRouge.modifiers;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PolarRegionModifier extends AbstractCardModifier {
    public static String ID = "samirg:PolarRegionModifier";

    public PolarRegionModifier() {}

    @Override
    public boolean shouldApply(AbstractCard card) {
        return true;
    }

    @Override
    public boolean removeOnCardPlayed(AbstractCard card) {
        return true;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return 1.5F * damage;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        return 1.5F * block;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + StringHelper.CARD_MODIFIERS.TEXT[2] + (Settings.lineBreakViaCharacter ? " " : "") + LocalizedStrings.PERIOD;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new PolarRegionModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}


