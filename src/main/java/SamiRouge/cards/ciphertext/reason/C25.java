package SamiRouge.cards.ciphertext.reason;

import SamiRouge.actions.ObtainRelicAction;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MedicalKit;

public class C25 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C25";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C25() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Group);
    }

    @Override
    public void triggerOnce() {
        remainX--;
        addToBot(new DrawCardAction(2));
        addToBot(new GainEnergyAction(2));
        if(together){
            AbstractRelic r = new MedicalKit();
            DeclareHelper.battleRelicObtain.add(r);
            addToBot(new ObtainRelicAction(r));
        }
    }

    @Override
    public void declareAtBattleStart() {
        triggerOnce();
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }

    @Override
    public void declared(int theX, boolean together) {
        this.remainX = theX;
        this.together = together;
        if(DeclareHelper.isBattle()){
            triggerOnce();
        }
    }
}

