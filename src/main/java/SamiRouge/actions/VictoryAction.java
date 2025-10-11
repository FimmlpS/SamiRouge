package SamiRouge.actions;

import SamiRouge.monsters.Smkght;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class VictoryAction extends AbstractGameAction {

    private final static UIStrings uiStrings;

    public VictoryAction(AbstractMonster winner, AbstractPower losePower,boolean extra){
        this.winner = winner;
        this.losePower = losePower;
        this.extra = extra;
    }

    boolean extra;

    AbstractPower losePower;

    @Override
    public void update() {
        addToTop(new ApplyPowerAction(winner,winner,losePower));
        addToTop(new IncreaseMaxHpAction(winner,extra?0.8F:0.5F,true));
        addToBot(new TalkAction(winner, uiStrings.TEXT[(winner instanceof Smkght)?0:1], 0.5F, 2.0F));
        addToTop(new ChangeStateAction(winner,"VICTORY"));
        this.isDone = true;
    }

    AbstractMonster winner;

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Talk");
    }
}
