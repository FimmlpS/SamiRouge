package SamiRouge.events;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.patches.EventPatch;
import SamiRouge.relics.ToBeAHunter;
import SamiRouge.samiMod.SamiRougeHelper;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;


public class DarkRoom extends AbstractImageEvent {
    public static final String ID = "samirg:DarkRoom";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    AbstractRelic relicToLose;

    Phase currentPhase;
    int money;

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        EventPatch.specialEvents.remove(ID);
    }

    public DarkRoom(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/DarkRoom.png");
        this.money = 50 + 50* AbstractDungeon.actNum;
        relicToLose = null;
        if(AbstractDungeon.player.relics.size()>0){
            relicToLose = AbstractDungeon.player.relics.get(AbstractDungeon.player.relics.size()-1);
        }
        imageEventText.setDialogOption(OPTIONS[0] + money + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
        imageEventText.setDialogOption(OPTIONS[3]);
        if(AbstractDungeon.player.name == "Typhon" || AbstractDungeon.player.hasRelic(ToBeAHunter.ID)){
            imageEventText.setDialogOption(OPTIONS[4]);
        }
        currentPhase = Phase.INTRO;
    }

    @Override
    protected void buttonEffect(int i) {
        switch (currentPhase){
            case INTRO:
                if(i==0){
                    AbstractBlight b = SamiRougeHelper.getRandomBlightToObtain(AbstractDungeon.eventRng);
                    if(b!=null){
                        AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,b);
                    }
                    AbstractDungeon.effectList.add(new RainingGoldEffect(this.money));
                    AbstractDungeon.player.gainGold(this.money);
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    imageEventText.setDialogOption(OPTIONS[8]);
                    currentPhase = Phase.LEAVE;
                }
                else if(i==1){
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    imageEventText.setDialogOption(OPTIONS[5]);
                    if(relicToLose==null){
                        imageEventText.setDialogOption(OPTIONS[6],true);
                    }
                    else{
                        imageEventText.setDialogOption(OPTIONS[6],relicToLose.makeCopy());
                    }

                    imageEventText.setDialogOption(OPTIONS[7]);
                    currentPhase = Phase.RELIC;
                }
                else if(i==2){
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    imageEventText.setDialogOption(OPTIONS[8]);
                    currentPhase = Phase.LEAVE;
                }
                else if(i==3){
                    for(int ind =0;ind<3;ind++)
                        SamiRougeHelper.removeRandomBlight(AbstractDungeon.eventRng);
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[6]);
                    imageEventText.setDialogOption(OPTIONS[8]);
                    currentPhase = Phase.LEAVE;
                }
                break;
            case RELIC:
                if(i==0){
                    AbstractBlight matrix = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
                    if(matrix!=null){
                        matrix.incrementUp();
                        matrix.flash();
                    }
                    else {
                        matrix = new IrreversibleMatrix();
                        AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, matrix);
                    }
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F - 100F*Settings.scale,(float) Settings.HEIGHT/2F,AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE));
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F + 100F*Settings.scale,(float) Settings.HEIGHT/2F,AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.RARE));
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[4]);
                    imageEventText.setDialogOption(OPTIONS[8]);
                    currentPhase = Phase.LEAVE;
                }
                else if(i==1){
                    if(relicToLose!=null){
                        AbstractDungeon.player.loseRelic(relicToLose.relicId);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,AbstractDungeon.returnRandomRelic(relicToLose.tier));
                    }
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[5]);
                    imageEventText.setDialogOption(OPTIONS[8]);
                    currentPhase = Phase.LEAVE;
                }
                else if(i==2){
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    imageEventText.setDialogOption(OPTIONS[8]);
                    currentPhase = Phase.LEAVE;
                }
                break;
            case LEAVE:
                openMap();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                break;
        }
    }

    public enum Phase{
        INTRO,
        RELIC,
        LEAVE
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
