package SamiRouge.blights;

import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;

public class PansocialParadox01 extends AbstractSamiBlight {
    public static final String ID = "samirg:PansocialParadox01";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public PansocialParadox01(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/PansocialParadox01.png");
        this.counter = 1;
    }

    @Override
    public String getUpgradeBlight() {
        return PansocialParadox02.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}

