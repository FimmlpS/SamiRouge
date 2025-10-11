package SamiRouge.actions;

import SamiRouge.effects.BlightAboveCreatureEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BlightAboveCreatureAction extends AbstractGameAction {
    private boolean used = false;
    private AbstractBlight blight;

    public BlightAboveCreatureAction(AbstractCreature source, AbstractBlight blight) {
        this.setValues(source, source);
        this.blight = blight;
        this.actionType = ActionType.TEXT;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (!this.used) {
            AbstractDungeon.effectList.add(new BlightAboveCreatureEffect(this.source.hb.cX - this.source.animX, this.source.hb.cY + this.source.hb.height / 2.0F - this.source.animY, this.blight));
            this.used = true;
        }

        this.tickDuration();
    }
}

