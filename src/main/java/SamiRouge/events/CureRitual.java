package SamiRouge.events;

import SamiRouge.blights.AntiInterference;
import SamiRouge.patches.EventPatch;
import SamiRouge.samiMod.SamiRougeMod;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class CureRitual extends AbstractImageEvent {
    public static final String ID = "samirg:CureRitual";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    boolean selected = false;
    boolean exhaustAnti;

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        EventPatch.specialEvents.remove(ID);
    }

    public CureRitual() {
        super(NAME, DESCRIPTIONS[0], "SamiRougeResources/img/events/CureRitual.png");
        AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
        if (anti != null && anti.counter > 0) {
            exhaustAnti = true;
            imageEventText.setDialogOption(OPTIONS[0]);
        } else {
            exhaustAnti = false;
            imageEventText.setDialogOption(OPTIONS[1]);
        }
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        if(!selected){
            imageEventText.clearAllDialogs();
            if(i==0){
                if(exhaustAnti){
                    AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                    if(anti != null && anti.counter > 0) {
                        ((AntiInterference)anti).useOne();
                    }
                    AbstractDungeon.player.increaseMaxHp(9,true);
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                }
                else {
                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, false);
                    CardCrawlGame.sound.play("BLUNT_FAST");
                    AbstractDungeon.player.damage(new DamageInfo((AbstractCreature)null, 6));
                    try {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH/2F,Settings.HEIGHT/2F,AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier()));
                    }
                    catch (Exception e) {
                        SamiRougeMod.logSomething(e.getMessage());
                    }
                    imageEventText.updateBodyText(DESCRIPTIONS[2]);
                }
            }
            else if(i==1){
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




