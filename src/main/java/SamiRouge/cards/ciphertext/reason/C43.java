package SamiRouge.cards.ciphertext.reason;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.relics.HatOfTreeScar;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class C43 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C43";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C43() {
        super(ID, cardStrings.NAME, -2, cardStrings.DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME, cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Thefair);
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together ? 4 : 3];
    }

    @Override
    public void declared(int theX, boolean together) {
        this.remainX = theX;
        this.together = together;
        if (together) {
            AbstractRelic relic = new HatOfTreeScar();
            relic.spawn(Settings.WIDTH / 2F, Settings.WIDTH / 2F);
            relic.obtain();
            relic.isObtained = true;
            relic.isAnimating = false;
            relic.isDone = false;
            relic.flash();
        }
    }
}

