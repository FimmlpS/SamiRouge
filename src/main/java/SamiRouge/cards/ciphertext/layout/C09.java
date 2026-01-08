package SamiRouge.cards.ciphertext.layout;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class C09 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C09";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C09() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],"").setData(this, CipherText.CipherType.Layout, CipherText.CipherColor.Soul).setTheX(1);
    }

    @Override
    public int declare(boolean together) {
        boolean extra = true;
        for(AbstractCipherTextCard c: DeclareHelper.layout){
            if(c != this){
                extra = false;
                break;
            }
        }
        return super.declare(together) + (extra?2:0);
    }
}


