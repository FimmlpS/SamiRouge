package SamiRouge.blights;

import SamiRouge.actions.BlightAboveCreatureAction;
import SamiRouge.actions.IncreaseMaxHpByBLAction;
import SamiRouge.dungeons.TheSami;
import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FlightPower;

public class NonlinearMovement01 extends AbstractSamiBlight {
    public static final String ID = "samirg:NonlinearMovement01";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public NonlinearMovement01(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/NonlinearMovement01.png");
        this.counter = 1;
    }

    @Override
    public void onCreateEnemy(AbstractMonster m) {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractDungeon.actionManager.addToBottom(new BlightAboveCreatureAction(m,this));
        AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpByBLAction(m,0.2F,true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new FlightPower(m,1),1));
    }

    @Override
    public void atBattleStart() {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new DexterityPower(AbstractDungeon.player,2),2));
    }

    @Override
    public String getUpgradeBlight() {
        return NonlinearMovement02.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}

