package SamiRouge.events;

import SamiRouge.patches.EventPatch;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.relics.HatOfTreeScar;
import SamiRouge.samiMod.SamiRougeHelper;
import TreeHole.mod.TreeHoleHelper;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class BeforeWoodCrack extends AbstractImageEvent {
    public static final String ID = "samirg:BeforeWoodCrack";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    boolean selected = false;

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        EventPatch.specialEvents.remove(ID);
    }

    public BeforeWoodCrack(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/BeforeWoodCrack.png");
        imageEventText.setDialogOption(OPTIONS[0],new HatOfTreeScar());
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int i) {
        if(!selected){
            imageEventText.clearAllDialogs();
            if(i==0){
                AbstractBlight b1 = SamiRougeHelper.getRandomBlightToObtain(AbstractDungeon.eventRng);
                if(b1!=null)
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, b1);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, new HatOfTreeScar());
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
            }
            else if(i==1){
                SamiRougeHelper.removeRandomBlight(AbstractDungeon.eventRng);
                AbstractDungeon.player.heal(5);
                imageEventText.updateBodyText(DESCRIPTIONS[2]);
            }
            imageEventText.setDialogOption(OPTIONS[2]);
            selected = true;
        }
        else{
            openMap();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        }
    }


    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}


