package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import SamiRouge.powers.BurstPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HandOfDogfight extends CustomRelic {

    public static final String ID = "samirg:HandOfDogfight";
    private static final String IMG = "SamiRougeResources/img/relics/HandOfDogfight.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HandOfDogfight_O.png";

    public HandOfDogfight(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(targetCard.type== AbstractCard.CardType.ATTACK){
            this.flash();
            for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                if(!m.isDeadOrEscaped()){
                    addToBot(new ApplyPowerAction(m,AbstractDungeon.player,new ConstrictedPower(m,AbstractDungeon.player,2),2));
                }
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfDogfight();
    }
}


