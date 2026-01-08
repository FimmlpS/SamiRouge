package SamiRouge.cards.ciphertext.reason;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.samiMod.SamiRougeHelper;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

public class C36 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C36";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C36() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Nature);
    }

    @Override
    public void declared(int theX, boolean together) {
        this.remainX = 0;
        this.together = together;
        for(int ind =0;ind<theX;ind++)
            SamiRougeHelper.removeRandomBlight(AbstractDungeon.eventRng);
        if(together){
            AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
            if(ir!=null&&ir.counter>0){
                ir.flash();
                ir.setCounter(ir.counter-1);
            }
        }
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}





