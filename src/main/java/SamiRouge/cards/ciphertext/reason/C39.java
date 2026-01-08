package SamiRouge.cards.ciphertext.reason;

import SamiRouge.blights.AntiInterference;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;

public class C39 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C39";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C39() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Nature);
    }

    @Override
    public void triggerOnce() {
        remainX--;
        AbstractDungeon.effectsQueue.add(new GainGoldTextEffect(50));
        AbstractDungeon.player.gainGold(50);
        AbstractDungeon.player.heal(4,true);
        if(together){
            AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
            if(anti instanceof AntiInterference){
                ((AntiInterference) anti).buyOne();
            }
            else{
                AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new AntiInterference());
            }
        }
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}




