package SamiRouge.blights;

import SamiRouge.actions.BlightAboveCreatureAction;
import SamiRouge.actions.DecreaseAttackAction;
import SamiRouge.actions.DelayActionAction;
import SamiRouge.actions.FastMakeTempCardInDrawPileAction;
import SamiRouge.dungeons.TheSami;
import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class SubstantialCollapse01 extends AbstractSamiBlight {
    public static final String ID = "samirg:SubstantialCollapse01";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    private int attackThisTurn = 0;

    public SubstantialCollapse01(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/SubstantialCollapse01.png");
        this.counter = 1;
    }

    @Override
    public void atBattleStart() {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,1),1));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        if(card.type != AbstractCard.CardType.ATTACK)
            return;
        AbstractDungeon.actionManager.addToBottom(new DecreaseAttackAction(card,1));
    }

    @Override
    public String getUpgradeBlight() {
        return SubstantialCollapse02.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}

