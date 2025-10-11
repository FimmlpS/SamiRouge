package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class IcyBody extends CustomRelic {

    public static final String ID = "samirg:IcyBody";
    private static final String IMG = "SamiRougeResources/img/relics/IcyBody.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/IcyBody_O.png";

    public IcyBody(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.CLINK);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onLoseHp(int damageAmount) {
        if(damageAmount<=0)
            return;
        if(AbstractDungeon.getCurrRoom()!=null && AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.COMBAT){
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new VigorPower(AbstractDungeon.player,damageAmount),damageAmount));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new IcyBody();
    }
}



