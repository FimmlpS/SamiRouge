package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class GoldenCup extends CustomRelic {

    public static final String ID = "samirg:GoldenCup";
    private static final String IMG = "SamiRougeResources/img/relics/GoldenCup.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/GoldenCup_O.png";


    public GoldenCup(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.SOLID);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        int amt = 0;
        if(AbstractDungeon.player!=null){
            amt = AbstractDungeon.player.gold/100;
        }
        if(amt>0&&info.type== DamageInfo.DamageType.NORMAL && target instanceof AbstractMonster){
            this.flash();
            addToTop(new DamageAction(target,new DamageInfo(AbstractDungeon.player,amt, DamageInfo.DamageType.THORNS),true));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GoldenCup();
    }
}


