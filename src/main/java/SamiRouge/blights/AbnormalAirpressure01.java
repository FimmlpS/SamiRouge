package SamiRouge.blights;

import SamiRouge.actions.BlightAboveCreatureAction;
import SamiRouge.dungeons.TheSami;
import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.DrawPower;

public class AbnormalAirpressure01 extends AbstractSamiBlight {
    public static final String ID = "samirg:AbnormalAirpressure01";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public AbnormalAirpressure01(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/AbnormalAirpressure01.png");
        this.counter = 1;
    }

    @Override
    public void atBattleStart() {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractDungeon.actionManager.addToBottom(new BlightAboveCreatureAction(AbstractDungeon.player,this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(),1,true,true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new DrawCardNextTurnPower(AbstractDungeon.player,2),2));
    }

    @Override
    public String getUpgradeBlight() {
        return AbnormalAirpressure02.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}

