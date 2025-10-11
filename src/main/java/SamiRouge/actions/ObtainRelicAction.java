package SamiRouge.actions;

import SamiRouge.relics.LimitlessGift;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ObtainRelicAction extends AbstractGameAction {
    public ObtainRelicAction(AbstractRelic r){
        this.r = r;
    }

    @Override
    public void update() {
        if(!AbstractDungeon.player.hasRelic(r.relicId))
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH/2F,Settings.HEIGHT/2F,r);
        this.isDone = true;
    }

    AbstractRelic r;
}
