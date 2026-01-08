package SamiRouge.cards.ciphertext.reason;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class C34 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C34";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C34() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Soul);
    }


    @Override
    public void declared(int theX, boolean together) {
        super.declared(theX, together);
        if(together){
            AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON);
            if (relic.relicId == "Circlet" && AbstractDungeon.player.hasRelic("Circlet")) {
                AbstractRelic circ = AbstractDungeon.player.getRelic("Circlet");
                ++circ.counter;
                circ.flash();
            } else {
                relic.spawn(Settings.WIDTH/2F, Settings.WIDTH/2F);
                relic.obtain();
                relic.isObtained = true;
                relic.isAnimating = false;
                relic.isDone = false;
                relic.flash();
            }
        }
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}




