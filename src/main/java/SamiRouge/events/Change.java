package SamiRouge.events;

import SamiRouge.patches.EventPatch;
import SamiRouge.patches.SamiTreeHolePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Change extends AbstractImageEvent {
    public static final String ID = "samirg:Change";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    private CurScreen screen;

    @Override
    public void onEnterRoom() {
        super.onEnterRoom();
        EventPatch.specialEvents.remove(ID);
    }

    public Change(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/Change.png" );
        screen = CurScreen.INTRO;
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screen){
            case INTRO:
                if(i==0){
                    AbstractDungeon.player.loseGold(1);
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[2]);
                    screen = CurScreen.FIGHT;
                }
                else {
                    imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[3]);
                    screen = CurScreen.LEAVE;
                }
                return;

            case FIGHT:
                AbstractDungeon.getCurrRoom().rewardAllowed = true;
                this.screen = CurScreen.LEAVE_AFTER_FIGHT;
                //monsters
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("samirg:SandIllusion");
                AbstractDungeon.getCurrRoom().rewards.clear();
                //获取 维度流质 需要改save
                SamiTreeHolePatch.getDimensionalFluidity = true;
                AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.RARE);
                AbstractDungeon.getCurrRoom().addGoldToRewards(150);
                AbstractDungeon.getCurrRoom().eliteTrigger = true;
                this.enterCombatFromImage();
                AbstractDungeon.lastCombatMetricKey = "samirg:SandIllusion";
                return;

            case LEAVE:
                this.openMap();
                return;

            case LEAVE_AFTER_FIGHT:
                this.openMap();
                return;
        }
    }

    @Override
    public void reopen() {
        super.reopen();
    }

    private static enum CurScreen{
        INTRO,
        FIGHT,
        LEAVE,
        LEAVE_AFTER_FIGHT
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
