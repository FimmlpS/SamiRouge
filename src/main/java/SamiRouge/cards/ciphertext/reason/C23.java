package SamiRouge.cards.ciphertext.reason;

import SamiRouge.actions.IncreaseMaxHpAction;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;

public class C23 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C23";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C23() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Group);
    }

    @Override
    public void triggerOnce() {
        remainX--;
        int hp = (int) (0.5F* AbstractDungeon.player.maxHealth);
        DeclareHelper.battleMaxHPIncreased+=hp;
        addToBot(new IncreaseMaxHpAction(true,hp));
        if(together){
            //tOdo
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
