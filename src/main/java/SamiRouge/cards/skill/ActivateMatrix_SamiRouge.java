package SamiRouge.cards.skill;

import SamiRouge.actions.ActivateMatrixAction;
import SamiRouge.cards.AbstractSamiRougeCard;
import SamiRouge.cards.curse.Coldness_SamiRouge;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ActivateMatrix_SamiRouge extends AbstractSamiRougeCard {

    public static final String ID = "ActivateMatrix_SamiRouge";
    private static final CardStrings cardStrings;

    public ActivateMatrix_SamiRouge(){
        super(ID, cardStrings.NAME, 0, cardStrings.DESCRIPTION, CardType.SKILL,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.ALL_ENEMY);
        this.exhaust = true;
        this.baseMagicNumber = 2;
        this.magicNumber = 2;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeMagicNumber(3);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new ActivateMatrixAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ActivateMatrix_SamiRouge();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}

