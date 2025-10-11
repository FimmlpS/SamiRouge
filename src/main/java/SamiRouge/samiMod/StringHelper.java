package SamiRouge.samiMod;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class StringHelper {

    public static final UIStrings CARD_MODIFIERS;

    public static String getCardIMGPATH(String ID, AbstractCard.CardType type) {
        switch (type) {
            case ATTACK: {
                ID += "_attack";
                break;
            }
            case CURSE:
            case STATUS:
            case SKILL: {
                ID += "_skill";
                break;
            }
            case POWER: {
                ID += "_power";
                break;
            }
        }
        return "SamiRougeResources/img/cards_SamiRouge/" + ID + ".png";
    }

    public static String getCipherIMGPATH(String ID){
        ID = ID.replace("samirg:C","");
        return "SamiRougeResources/img/cards_SamiRouge/ciphertext/" + ID + ".png";
    }

    static {
        CARD_MODIFIERS = CardCrawlGame.languagePack.getUIString("samirg:CardModifiers");
    }

}
