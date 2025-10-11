package SamiRouge.helper;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class AltStringHelper {
    public static final UIStrings theSami;
    public static final String errorHealthTrigger;


    static {
        theSami = CardCrawlGame.languagePack.getUIString("samirg:TheSami");
        errorHealthTrigger = theSami.TEXT[1];
    }
}
