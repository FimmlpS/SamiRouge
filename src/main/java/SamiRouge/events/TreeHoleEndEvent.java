package SamiRouge.events;

import SamiRouge.patches.SamiTreeHolePatch;
import TreeHole.mod.TreeHoleHelper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class TreeHoleEndEvent extends AbstractImageEvent {
    public static final String ID = "samirg:TreeHoleEnd";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    Random rng;

    public TreeHoleEndEvent(Random rng){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/TreeHole.png");
        imageEventText.setDialogOption(OPTIONS[0]);
        this.rng = rng;
    }

    @Override
    protected void buttonEffect(int i) {
        SamiTreeHolePatch.treeHoleEntered++;
        if(TreeHoleHelper.getCurrentType()==3){
            SamiTreeHolePatch.bossKilled++;
        }
        TreeHoleHelper.outerTreeHole();
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.getCurrRoom().clearEvent();
    }


    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}

