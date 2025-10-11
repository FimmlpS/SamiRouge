package SamiRouge.events;

import SamiRouge.blights.AntiInterference;
import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.patches.EventPatch;
import SamiRouge.relics.FarScry;
import SamiRouge.relics.IceGrindingStone;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Rain extends AbstractImageEvent {
    public static final String ID = "samirg:Rain";
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

    public Rain(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/Rain.png");
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int i) {
        if(!selected){
            imageEventText.clearAllDialogs();
            if(i==0){
                AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                if(anti instanceof AntiInterference){
                    ((AntiInterference) anti).buyOne();
                }
                else{
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new AntiInterference());
                }
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
            }
            else if(i==1){
                AbstractDungeon.player.heal(12);
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



