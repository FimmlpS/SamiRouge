package SamiRouge.cards.ciphertext;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

public class CipherText {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Declare");
    public static final String extraTitle = uiStrings.TEXT[0];

    public CipherText(String name, String declareWord, String extraWord) {
        this.name = name;
        this.declareWord = declareWord;
        this.extraDescription = extraWord;
    }

    public CipherText setData(AbstractCipherTextCard card,CipherType type, CipherColor color){
        this.card = card;
        this.type = type;
        this.color = color;
        return this;
    }

    public CipherText setTheX(int x){
        this.theX = x;
        return this;
    }

    public static boolean isTogether(CipherText layout, CipherText reason){
        if(layout.color == CipherColor.Visual)
            return reason.color != CipherColor.Thefair;
        return layout.color == reason.color;
    }

    public AbstractCipherTextCard card;
    public CipherType type;
    public CipherColor color;

    public String name;
    public String declareWord;
    public String extraDescription;

    public int theX = 1;


    public enum CipherType {
        Layout,
        Reason
    }

    public enum CipherColor {
        Group,
        Soul,
        Nature,
        Visual,
        Thefair
    }
}
