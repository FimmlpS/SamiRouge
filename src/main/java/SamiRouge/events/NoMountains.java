package SamiRouge.events;

import SamiRouge.patches.EventPatch;
import SamiRouge.relics.EchoWood;
import SamiRouge.relics.EveryNight;
import SamiRouge.relics.HatOfTreeScar;
import SamiRouge.samiMod.SamiRougeHelper;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class NoMountains extends AbstractImageEvent {
    public static final String ID = "samirg:NoMountains";
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

    public NoMountains(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/NoMountains.png");
        imageEventText.setDialogOption(OPTIONS[0],new EveryNight());
        imageEventText.setDialogOption(OPTIONS[1],new EchoWood());
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        if(!selected){
            imageEventText.clearAllDialogs();
            if(i==0){
                for(int index =0;index<AbstractDungeon.player.potionSlots;index++){
                    AbstractDungeon.player.potions.set(index,new PotionSlot(index));
                }
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, new EveryNight());
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
            }
            else if(i==1){
                AbstractDungeon.player.decreaseMaxHealth((int)(0.25F*AbstractDungeon.player.maxHealth));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,new EchoWood());
                imageEventText.updateBodyText(DESCRIPTIONS[2]);
            }
            else if(i==2){
                if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).size() > 0) {
                    AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()), 1, OPTIONS[4], false, false, false, true);
                }
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

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = (AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2)));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.remove(c);
        }
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}

