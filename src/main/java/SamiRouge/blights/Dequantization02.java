package SamiRouge.blights;

import SamiRouge.actions.BlightAboveCreatureAction;
import SamiRouge.dungeons.TheSami;
import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Dequantization02 extends AbstractSamiBlight {
    public static final String ID = "samirg:Dequantization02";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public static boolean delete = true;

    public Dequantization02(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/Dequantization02.png");
        this.counter = 2;
    }

    @Override
    public void onCreateEnemy(AbstractMonster m) {
        if(delete)
            return;
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractDungeon.actionManager.addToBottom(new BlightAboveCreatureAction(m,this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new StrengthPower(m,-3),-3));
    }

    @Override
    public String getExclusionBlight() {
        return Dequantization01.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}

