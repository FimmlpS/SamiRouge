package SamiRouge.events;

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

public class FarSee extends AbstractImageEvent {
    public static final String ID = "samirg:FarSee";
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

    public FarSee(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/FarSee.png");
        imageEventText.setDialogOption(OPTIONS[0],new IceGrindingStone());
        imageEventText.setDialogOption(OPTIONS[1],new FarScry());
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        if(!selected){
            imageEventText.clearAllDialogs();
            if(i==0){
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, new IceGrindingStone());
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
            }
            else if(i==1){
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,new FarScry());
                imageEventText.updateBodyText(DESCRIPTIONS[2]);
            }
            else if(i==2){
                //matrix spawn
                AbstractBlight matrix = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
                if(matrix!=null){
                    matrix.incrementUp();
                    matrix.flash();
                }
                else {
                    matrix = new IrreversibleMatrix();
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, matrix);
                }
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F - 100F*Settings.scale,(float) Settings.HEIGHT/2F, new IceGrindingStone());
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F + 100F*Settings.scale,(float) Settings.HEIGHT/2F, new FarScry());
                imageEventText.updateBodyText(DESCRIPTIONS[3]);
            }
            imageEventText.setDialogOption(OPTIONS[3]);
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


