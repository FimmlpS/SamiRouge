package SamiRouge.cards.skill;

import SamiRouge.actions.ActivateMatrixAction;
import SamiRouge.actions.MarkAsForeverAction;
import SamiRouge.cards.AbstractSamiRougeCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PastWeaveFuture_SamiRouge extends AbstractSamiRougeCard {

    public static final String ID = "PastWeaveFuture_SamiRouge";
    private static final CardStrings cardStrings;

    public PastWeaveFuture_SamiRouge(){
        super(ID, cardStrings.NAME, 1, cardStrings.DESCRIPTION, CardType.SKILL,CardColor.COLORLESS,CardRarity.RARE,CardTarget.NONE);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            this.selfRetain = true;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new MarkAsForeverAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new PastWeaveFuture_SamiRouge();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}


