package SamiRouge.blights;

import SamiRouge.dungeons.TheSami;
import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;

public class ConvergentConsumption01 extends AbstractSamiBlight {
    public static final String ID = "samirg:ConvergentConsumption01";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public ConvergentConsumption01(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/ConvergentConsumption01.png");
        this.counter = 1;
    }

    @Override
    public void onVictory() {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        this.flash();
        int decreaseAmt = Math.max(0,Math.min(AbstractDungeon.player.maxHealth-20,1));
        if(decreaseAmt>0)
            AbstractDungeon.player.decreaseMaxHealth(decreaseAmt);
        AbstractDungeon.player.heal(4);
    }

    @Override
    public String getUpgradeBlight() {
        return ConvergentConsumption02.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}

