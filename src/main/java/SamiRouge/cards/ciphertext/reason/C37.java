package SamiRouge.cards.ciphertext.reason;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Toolbox;

public class C37 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C37";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private boolean atOnce = false;

    public C37() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Nature);
    }

    @Override
    public void triggerOnce() {
        remainX--;
        addToBot(new GainBlockAction(AbstractDungeon.player,AbstractDungeon.player,15));
        if(together){
            AbstractRelic r = new Toolbox();
            DeclareHelper.battleRelicObtain.add(r);
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH/2F,Settings.HEIGHT/2F,r);
            if(atOnce){
                r.atBattleStartPreDraw();
            }
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
            atOnce = true;
            triggerOnce();
            atOnce = false;
        }
    }
}


