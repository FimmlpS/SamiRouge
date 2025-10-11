package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import SamiRouge.powers.BurstPower;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HandOfBurst extends CustomRelic {

    public static final String ID = "samirg:HandOfBurst";
    private static final String IMG = "SamiRougeResources/img/relics/HandOfBurst.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HandOfBurst_O.png";

    public HandOfBurst(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atTurnStart() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
            if(!m.isDeadOrEscaped()){
                addToBot(new ApplyPowerAction(m,AbstractDungeon.player,new BurstPower(m)));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfBurst();
    }
}


