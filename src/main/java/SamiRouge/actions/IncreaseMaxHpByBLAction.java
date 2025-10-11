package SamiRouge.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;

public class IncreaseMaxHpByBLAction extends AbstractGameAction {
    private boolean showEffect;
    private float increasePercent;

    public IncreaseMaxHpByBLAction(AbstractMonster m, float increasePercent, boolean showEffect) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.duration = this.startDuration;
        this.showEffect = showEffect;
        this.increasePercent = increasePercent;
        this.target = m;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            int maxIn = MathUtils.round((float)this.target.maxHealth * this.increasePercent);
            int curIn = MathUtils.round((float)this.target.currentHealth * this.increasePercent);
            if (!Settings.isEndless || !AbstractDungeon.player.hasBlight("FullBelly")) {
                UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AbstractCreature");
                target.maxHealth += maxIn;
                AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(target.hb.cX - target.animX, target.hb.cY, uiStrings.TEXT[2] + Integer.toString(maxIn), Settings.GREEN_TEXT_COLOR));
                target.heal(curIn, true);
                target.healthBarUpdatedEvent();
            }
        }

        this.tickDuration();
    }
}
