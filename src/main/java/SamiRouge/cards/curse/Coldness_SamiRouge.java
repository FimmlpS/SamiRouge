package SamiRouge.cards.curse;

import SamiRouge.cards.AbstractSamiRougeCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Coldness_SamiRouge extends AbstractSamiRougeCard {

    public static final String ID = "Coldness_SamiRouge";
    private static final CardStrings cardStrings;

    public Coldness_SamiRouge(){
        super(ID, cardStrings.NAME, 2, cardStrings.DESCRIPTION, CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.exhaust = true;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if(card.type==CardType.SKILL){
            card.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canPlay(card);
    }


    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            upgradeBaseCost(3);
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public AbstractCard makeCopy() {
        return new Coldness_SamiRouge();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}
