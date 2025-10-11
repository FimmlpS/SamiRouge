package SamiRouge.actions;

import SamiRouge.cards.curse.Coldness_SamiRouge;
import SamiRouge.cards.curse.Freeze_SamiRouge;
import SamiRouge.cards.curse.Hypothermia_SamiRouge;
import SamiRouge.monsters.WoodCrack;
import SamiRouge.powers.SelfCollapsePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class WoodCrackAction extends AbstractGameAction {
    public WoodCrackAction(boolean up,WoodCrack wc){
        this.wc = wc;
        this.upgradeCard = up;
    }

    @Override
    public void update() {
        AbstractCard curse = new Coldness_SamiRouge();
        if(upgradeCard)
            curse.upgrade();
        addToBot(new ApplyPowerAction(wc,wc,new SelfCollapsePower(wc,(int)(wc.maxHealth*0.8F),wc.additionalBlock?15:10,1,curse)));
        curse = new Hypothermia_SamiRouge();
        if(upgradeCard)
            curse.upgrade();
        addToBot(new ApplyPowerAction(wc,wc,new SelfCollapsePower(wc,(int)(wc.maxHealth*0.5F),wc.additionalBlock?30:20,2,curse)));
        curse = new Freeze_SamiRouge();
        if(upgradeCard)
            curse.upgrade();
        addToBot(new ApplyPowerAction(wc,wc,new SelfCollapsePower(wc,(int)(wc.maxHealth*0.2F),wc.additionalBlock?45:30,3,curse)));
        this.isDone = true;
    }

    boolean upgradeCard;
    WoodCrack wc;
}
