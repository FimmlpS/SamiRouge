package SamiRouge.cards.power;

import SamiRouge.cards.AbstractSamiRougeCard;
import SamiRouge.effects.DeclareEffect;
import SamiRouge.powers.ColdProtectionPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ColdProtection_SamiRouge extends AbstractSamiRougeCard {
    public static final String ID = "ColdProtection_SamiRouge";
    private static final CardStrings cardStrings;

    public ColdProtection_SamiRouge(){
        super(ID, cardStrings.NAME,1, cardStrings.DESCRIPTION, AbstractCard.CardType.POWER, CardColor.COLORLESS, AbstractCard.CardRarity.UNCOMMON, CardTarget.SELF);

    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        //addToBot(new VFXAction(abstractPlayer,new DeclareEffect(),2F,true));
        addToBot(new ApplyPowerAction(abstractPlayer,abstractPlayer,new ColdProtectionPower(abstractPlayer,1),1));

    }

    @Override
    public void upgrade() {
        if(!upgraded){
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
            this.isInnate = true;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ColdProtection_SamiRouge();
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}

