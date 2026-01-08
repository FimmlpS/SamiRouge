package SamiRouge.cards.ciphertext.layout;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class C17 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C17";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C17() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],"").setData(this, CipherText.CipherType.Layout, CipherText.CipherColor.Nature).setTheX(2);
    }

    @Override
    public int declare(boolean together) {
        if(together){
            return super.declare(together)+1;
        }
        return super.declare(together);
    }
}



