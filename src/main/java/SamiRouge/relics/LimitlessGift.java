package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;


public class LimitlessGift extends CustomRelic {

    public static final String ID = "samirg:LimitlessGift";
    private static final String IMG = "SamiRougeResources/img/relics/LimitlessGift.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/LimitlessGift_O.png";

    public LimitlessGift(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.MAGICAL);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        this.flash();
        for(AbstractMonster m1: AbstractDungeon.getMonsters().monsters){
            if(!m1.isDeadOrEscaped()){
                addToBot(new RelicAboveCreatureAction(m1,this));
                addToBot(new ApplyPowerAction(m1,AbstractDungeon.player,new StrengthPower(m1,-1),-1));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LimitlessGift();
    }
}





