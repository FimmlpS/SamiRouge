package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class NorthWind extends CustomRelic {

    public static final String ID = "samirg:NorthWind";
    private static final String IMG = "SamiRougeResources/img/relics/NorthWind.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/NorthWind_O.png";

    public NorthWind(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.MAGICAL);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(info.type == DamageInfo.DamageType.NORMAL){
            if(info.owner instanceof AbstractMonster){
                AbstractPower st = info.owner.getPower(StrengthPower.POWER_ID);
                if(info.owner.hasPower(ArtifactPower.POWER_ID ) || (st!=null&&st.amount>0)){
                    this.flash();
                    addToBot(new ApplyPowerAction(info.owner,AbstractDungeon.player,new StrengthPower(info.owner,-1),-1));
                }
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NorthWind();
    }
}




