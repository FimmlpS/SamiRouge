package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class StoneRelic extends CustomRelic {

    public static final String ID = "samirg:StoneRelic";
    private static final String IMG = "SamiRougeResources/img/relics/StoneRelic.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/StoneRelic_O.png";

    Random duplicated;

    public StoneRelic(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.COMMON,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
        this.counter = 10;
        this.description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(this.name,this.description));
        initializeTips();
    }

    @Override
    public void atPreBattle() {
        duplicated = new Random(Settings.seed,AbstractDungeon.cardRandomRng.counter);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(duplicated==null){
            duplicated = new Random(Settings.seed,AbstractDungeon.cardRandomRng.counter);
        }
        if(drawnCard.baseDamage>=0||drawnCard.baseBlock>=0){
            boolean triggered = duplicated.random(0,99)<this.counter;
            if(triggered){
                this.flash();
                drawnCard.flash();
                if(drawnCard.baseDamage>=0){
                    drawnCard.baseDamage+=1;
                }
                if(drawnCard.baseBlock>=0){
                    drawnCard.baseBlock+=1;
                }
                drawnCard.applyPowers();
            }
        }
    }

    @Override
    public void onVictory() {
        this.counter+=3;
        if(counter>100)
            counter = 100;
        updateDescription(AbstractDungeon.player.chosenClass);
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(this.name,this.description));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]+counter+DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StoneRelic();
    }
}



