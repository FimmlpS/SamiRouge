package SamiRouge.cards.ciphertext.reason;

import SamiRouge.actions.IncreaseMaxHpAction;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

public class C28 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C28";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C28() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Group);
    }

    @Override
    public void declared(int theX, boolean together) {
        this.remainX = 0;
        this.together = together;
        for(int i =0;i<theX;i++){
            addToBot(new HealAction(AbstractDungeon.player,AbstractDungeon.player,6));
        }
        if(together){
            addToBot(new IncreaseMaxHpAction(true,6));
        }
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}




