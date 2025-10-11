package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class HandOfExplosion extends CustomRelic {

    public static final String ID = "samirg:HandOfExplosion";
    private static final String IMG = "SamiRougeResources/img/relics/HandOfExplosion.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HandOfExplosion_O.png";

    private ArrayList<AbstractMonster> damagedThisCombat = new ArrayList<>();

    public HandOfExplosion(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atBattleStart() {
        damagedThisCombat.clear();
    }

    @Override
    public void onVictory() {
        damagedThisCombat.clear();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(!info.type.equals(DamageInfo.DamageType.NORMAL))
            return;
        if(target instanceof AbstractMonster){
            AbstractMonster m = (AbstractMonster) target;
            if(!damagedThisCombat.contains(m)){
                damagedThisCombat.add(m);
                this.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
                addToBot(new GainEnergyAction(2));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfExplosion();
    }
}



