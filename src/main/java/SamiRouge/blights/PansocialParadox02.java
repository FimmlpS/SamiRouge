package SamiRouge.blights;

import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;

public class PansocialParadox02 extends AbstractSamiBlight {
    public static final String ID = "samirg:PansocialParadox02";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public PansocialParadox02(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/PansocialParadox02.png");
        this.counter = 2;
    }

    @Override
    public String getExclusionBlight() {
        return PansocialParadox01.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}


