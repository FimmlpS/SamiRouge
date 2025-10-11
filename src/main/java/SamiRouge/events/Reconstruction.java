package SamiRouge.events;

import SamiRouge.relics.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;
import java.util.Collections;

public class Reconstruction extends AbstractImageEvent {
    public static final String ID = "samirg:Reconstruction";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    AbstractCard cardToLose;

    Phase currentPhase;

    public Reconstruction(){
        super(NAME,DESCRIPTIONS[0],"SamiRougeResources/img/events/Reconstruction.png");
        cardToLose = null;
        ArrayList<AbstractCard> rareCards = new ArrayList<>();
        for(AbstractCard c: AbstractDungeon.player.masterDeck.group){
            if(c.rarity == AbstractCard.CardRarity.RARE){
                rareCards.add(c);
            }
        }
        if(rareCards.size()>0){
            Collections.shuffle(rareCards,AbstractDungeon.eventRng.random);
            cardToLose = rareCards.get(0);
        }
        imageEventText.setDialogOption(OPTIONS[0],new CollapseSeed());
        if(cardToLose!=null)
            imageEventText.setDialogOption(OPTIONS[1] +cardToLose.name,cardToLose);
        else
            imageEventText.setDialogOption(OPTIONS[12],true);
        currentPhase = Phase.ONE;
    }

    @Override
    protected void buttonEffect(int i) {
        switch (currentPhase){
            case ONE:
                if(i==0){
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,new CollapseSeed());
                }
                else if(i==1){
                    AbstractDungeon.effectList.add(new PurgeCardEffect(this.cardToLose));
                    AbstractDungeon.player.masterDeck.removeCard(this.cardToLose);
                }
                imageEventText.clearAllDialogs();
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.setDialogOption(OPTIONS[3],new SpaceFragment());
                imageEventText.setDialogOption(OPTIONS[4],AbstractDungeon.player.gold<400);
                imageEventText.setDialogOption(OPTIONS[5],!AbstractDungeon.player.hasRelic(HatOfTreeScar.ID),new HatOfTreeScar());
                currentPhase = Phase.TWO;
                break;
            case TWO:
                if(i==0){
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,new SpaceFragment());
                }
                else if(i==1){
                    AbstractDungeon.player.loseGold(400);
                }
                else if(i==2){
                    AbstractDungeon.player.loseRelic(HatOfTreeScar.ID);
                }
                imageEventText.clearAllDialogs();
                imageEventText.updateBodyText(DESCRIPTIONS[2]);
                imageEventText.setDialogOption(OPTIONS[7],new DeepBurn());
                imageEventText.setDialogOption(OPTIONS[8],!hasTwoPotion());
                imageEventText.setDialogOption(OPTIONS[9],!AbstractDungeon.player.hasRelic(LimitlessGift.ID),new LimitlessGift());
                currentPhase = Phase.THREE;
                break;
            case THREE:
                if(i==0){
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F,new DeepBurn());
                }
                else if(i==1){
                    for(int index =0;index<AbstractDungeon.player.potionSlots;index++){
                        AbstractDungeon.player.potions.set(index,new PotionSlot(index));
                    }
                }
                else if(i==2){
                    AbstractDungeon.player.loseRelic(LimitlessGift.ID);
                }
                imageEventText.clearAllDialogs();
                imageEventText.updateBodyText(DESCRIPTIONS[3]);
                imageEventText.setDialogOption(OPTIONS[11]);
                currentPhase = Phase.LEAVE;
                break;
            case LEAVE:
                openMap();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                break;
        }
    }

    private boolean hasTwoPotion(){
        int count = 0;
        for(AbstractPotion p :AbstractDungeon.player.potions){
            if(!(p instanceof PotionSlot)){
                count++;
            }
        }
        return count>=2;
    }

    public enum Phase{
        ONE,
        TWO,
        THREE,
        LEAVE
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
