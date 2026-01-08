package SamiRouge.events;

import SamiRouge.relics.RoadVine;
import SamiRouge.samiMod.SamiRougeHelper;
import TreeHole.mod.TreeHoleHelper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class TreeHoleOutEvent extends AbstractImageEvent {
    public static final String ID = "samirg:TreeHoleOut";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    Random rng;

    AbstractRelic relic;
    int money;
    int live;
    boolean selected = false;

    public TreeHoleOutEvent(Random rng){
        super(NAME,DESCRIPTIONS[2],"SamiRougeResources/img/events/TreeHole.png");
        this.rng = rng;
        boolean more = false;
        Random tmpRandom = new Random(Settings.seed+200,AbstractDungeon.floorNum);
        int mim = 25;
        if(TreeHoleHelper.getCurrentType()==2)
            mim = 50;
        else if(TreeHoleHelper.getCurrentType()==3)
            mim = 100;
        if(mim>tmpRandom.random(99))
            more = true;
        AbstractRelic rv = AbstractDungeon.player.getRelic(RoadVine.ID);
        if(!more && rv!=null){
            more = true;
            rv.onTrigger();
        }
        if(more){
            AbstractRelic.RelicTier tier = AbstractDungeon.returnRandomRelicTier();
            relic = SamiRougeHelper.returnTreeHoleRelic(tier);
            imageEventText.setDialogOption(OPTIONS[6]+relic.name+OPTIONS[7],relic);
        }
        else {
            imageEventText.setDialogOption(OPTIONS[17],true);
        }
        int countAmt = AbstractDungeon.actNum+1;
        money = (more?20:15)*countAmt;
        imageEventText.setDialogOption(OPTIONS[8]+money+OPTIONS[9]);
        live = (more?4:3)*countAmt;
        imageEventText.setDialogOption(OPTIONS[10]+live+OPTIONS[11]);
        selected = false;
    }

    @Override
    protected void buttonEffect(int i) {
        if (!selected) {
            selected = true;
            if (i == 0) {
                if(relic!=null)
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), relic);
            } else if (i == 1) {
                CardCrawlGame.sound.play("GOLD_GAIN");
                AbstractDungeon.player.gainGold(money);
            } else {
                AbstractDungeon.player.heal(live);
            }
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[12]);
        } else {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;

            openMap();
        }

    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(TreeHoleEvent.ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
