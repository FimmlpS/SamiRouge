package SamiRouge.events;

import SamiRouge.blights.AbstractSamiBlight;
import SamiRouge.blights.AntiInterference;
import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.dungeons.TheSami;
import SamiRouge.monsters.Cresson;
import SamiRouge.monsters.WillOfSami;
import SamiRouge.monsters.WoodCrack;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.relics.HatOfTreeScar;
import SamiRouge.relics.LimitlessGift;
import SamiRouge.relics.MissHome;
import SamiRouge.relics.RitualBell;
import SamiRouge.samiMod.ModConfig;
import SamiRouge.samiMod.SamiRougeHelper;
import TreeHole.mod.TreeHoleHelper;
import TreeHole.patches.TreeHolePatch;
import basemod.BaseMod;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;


public class TreeHoleEvent extends AbstractImageEvent {
    public static final String ID = "samirg:TreeHole";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    public static final String MUELSYSE_ANMA = "muelsyse:Anma";

    boolean finalIt;

    Random rng;
    Phase roomPhase = Phase.DEFAULT;

    //如果是刚从树洞出来，就置为true
    int notChoice;
    AbstractRelic relic;
    int money;
    int live;

    ArrayList<String> bossSections = new ArrayList<>();

    public TreeHoleEvent(Random rng) {
        super(NAME, DESCRIPTIONS[0], "SamiRougeResources/img/events/TreeHole.png");

        roomPhase = Phase.ENTER;
        finalIt = AbstractDungeon.id.equals(TheEnding.ID);
        SamiTreeHolePatch.rollTreeHoleType(rng);
        imageEventText.clearAllDialogs();

        //boss sections add
        if (AbstractDungeon.actNum == 3) {
            if (!SamiTreeHolePatch.toTalkWithMountains) {
                bossSections.add(WillOfSami.ID);
            }
            if (!SamiTreeHolePatch.winterFall && AbstractDungeon.player.hasRelic(HatOfTreeScar.ID)) {
                bossSections.add(WoodCrack.ID);
            }
            if (!SamiTreeHolePatch.longIntoAnAbyss && AbstractDungeon.player.hasRelic(LimitlessGift.ID)) {
                bossSections.add(Cresson.ID);
            }
            if(BaseMod.hasModID("Muelsyse_FimmlpS:") && !AbstractDungeon.player.name.equals("Muelsyse") && !SamiTreeHolePatch.withHerTalk){
                bossSections.add(MUELSYSE_ANMA);
            }
        }

        boolean showAnti = false;
        AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
        if(anti!=null){
            if(anti.counter>0)
                showAnti = true;
        }
        imageEventText.setDialogOption(OPTIONS[16], !showAnti);
        imageEventText.setDialogOption(OPTIONS[5]);

        this.rng = rng;
    }

    @Override
    protected void buttonEffect(int i) {
        if (roomPhase == Phase.ENTER) {
            if (i==0) {
                AbstractBlight blight = AbstractDungeon.player.getBlight(AntiInterference.ID);
                if(blight!=null){
                    if(blight.counter>0)
                        ((AntiInterference)blight).useOne();
                }
                roomPhase = Phase.TREE_HOLE;
                body = DESCRIPTIONS[0];
                imageEventText.clearAllDialogs();
                for (int i1 = 0; i1 < bossSections.size(); i1++) {
                    int option = -1;
                    switch (bossSections.get(i1)) {
                        case WillOfSami.ID:
                            option = 13;
                            break;
                        case WoodCrack.ID:
                            option = 14;
                            break;
                        case Cresson.ID:
                            option = 15;
                            break;
                        case MUELSYSE_ANMA:
                            option = 18;
                            break;
                    }
                    if (option > 0) {
                        imageEventText.setDialogOption(OPTIONS[option]);
                    }
                }

                for (int i1 = 0; i1 < SamiTreeHolePatch.treeHoleTypes.size(); i1++) {
                    imageEventText.setDialogOption(OPTIONS[SamiTreeHolePatch.treeHoleTypes.get(i1)]);
                }
            }
            else {
                roomPhase = Phase.NOT_ENTER;
                body = DESCRIPTIONS[1];
                imageEventText.clearAllDialogs();
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
                notChoice = rng.random(2);
                switch (notChoice) {
                    case 0:
                        relic = AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
                        imageEventText.setDialogOption(OPTIONS[6]+relic.name+OPTIONS[7],relic);
                        break;
                    case 1:
                        money = 25*(AbstractDungeon.actNum+1);
                        imageEventText.setDialogOption(OPTIONS[8]+money+OPTIONS[9]);
                        break;
                    case 2:
                        live = 4*(AbstractDungeon.actNum+1);
                        imageEventText.setDialogOption(OPTIONS[10]+live+OPTIONS[11]);
                        break;
                }
            }
        }
        else if (roomPhase == Phase.TREE_HOLE) {
            if(i>=bossSections.size())
                TreeHoleHelper.enterTreeHole(TheSami.ID,SamiTreeHolePatch.treeHoleTypes.get(i-bossSections.size()));
            else{
                SamiTreeHolePatch.currentSamiBoss = bossSections.get(i);
                TreeHoleHelper.enterTreeHole(TheSami.ID,3);
            }

            //matrix spawn
            SamiRougeHelper.spawnIrreversibleOnce();

            //fans spawn
            AbstractBlight b1 = SamiRougeHelper.getRandomBlightToObtain(rng);
            if(b1!=null)
                AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, b1);
            if(ModConfig.extra_fans){
                AbstractBlight b2 = SamiRougeHelper.getRandomBlightToObtain(rng);
                if(b2!=null)
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, b2);
            }

            //relic trigger
            AbstractRelic rb = AbstractDungeon.player.getRelic(RitualBell.ID);
            if(rb!=null){
                rb.onTrigger();
            }
            AbstractRelic mh = AbstractDungeon.player.getRelic(MissHome.ID);
            if(mh!=null){
                mh.onTrigger();
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.getCurrRoom().clearEvent();
        }
        else if (roomPhase == Phase.NOT_ENTER) {
            switch (notChoice) {
                case 0:
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2),relic);
                    break;
                case 1:
                    CardCrawlGame.sound.play("GOLD_GAIN");
                    AbstractDungeon.player.gainGold(money);
                    break;
                case 2:
                    AbstractDungeon.player.heal(live);
                    break;

            }
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(OPTIONS[12]);
            roomPhase = Phase.GONE;
        }
        else if (roomPhase == Phase.GONE) {
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            openMap();
        }
        else {
            openMap();
        }
    }


    enum Phase{
        DEFAULT,
        ENTER,
        TREE_HOLE,
        NOT_ENTER,
        GONE
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
