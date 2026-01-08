package SamiRouge.cards.ciphertext.reason;

import SamiRouge.blights.AntiInterference;
import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class C33 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C33";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C33() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Soul);
    }

    @Override
    public void triggerOnce() {
        remainX--;
        addToBot(new DoubleEnergyAction());
        addToBot(new DrawCardAction(10));
        if(together){
            AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
            if(anti != null && anti.counter>0){
                addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,anti.counter),anti.counter));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new LoseDexterityPower(AbstractDungeon.player,anti.counter),anti.counter));
            }
            AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
            if(ir != null && ir.counter>0){
                addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,ir.counter),ir.counter));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new LoseStrengthPower(AbstractDungeon.player,ir.counter),ir.counter));
            }
        }
    }

    @Override
    public void declareAtTurnStart() {
        triggerOnce();
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}




