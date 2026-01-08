package SamiRouge.events;

import SamiRouge.blights.AntiInterference;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.reason.C32;
import SamiRouge.cards.special.RelicPreviewCard_SamiRouge;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.relics.WarnWood;
import SamiRouge.samiMod.SamiRougeHelper;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

public class LostAndFound extends AbstractImageEvent {
    public static final String ID = "samirg:LostAndFound";
    private static final EventStrings eventStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;

    Phase currentPhase;
    Random duplicatedRng;

    private int tradeTimes = 0;

    boolean trading;
    boolean obtainRelic;
    ArrayList<AbstractRelic> relicsToObtain = new ArrayList<>();
    ArrayList<AbstractCard> cardsToObtain = new ArrayList<>();

    public LostAndFound(Random duplicatedRng) {
        super(NAME, DESCRIPTIONS[0], "SamiRougeResources/img/events/LostAndFound.png");
        initializeOptions();
    }

    private void initializeOptions(){
        trading = false;
        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        imageEventText.clearAllDialogs();
        if(tradeTimes==0){
            imageEventText.updateBodyText(DESCRIPTIONS[0]);
            boolean hasCome = false;
            AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
            if(anti!=null){
                if(anti.counter>0)
                    hasCome = true;
            }
            if(hasCome){
                imageEventText.setDialogOption(OPTIONS[12]);
            }
            else {
                imageEventText.setDialogOption(OPTIONS[8],true);
            }
            imageEventText.setDialogOption(OPTIONS[9]);
            boolean hasCipher = false;
            for(AbstractCipherTextCard c: DeclareHelper.buffed){
                if(c instanceof C32){
                    hasCipher = true;
                    break;
                }
            }
            if(hasCipher){
                imageEventText.setDialogOption(OPTIONS[11]);
            }
            currentPhase = Phase.LEAVE;
        }
        else {
            imageEventText.updateBodyText(DESCRIPTIONS[0]);
            if(canTradeRelic())
                imageEventText.setDialogOption(OPTIONS[0]);
            else
                imageEventText.setDialogOption(OPTIONS[6],true);
            if(canTradeCard())
                imageEventText.setDialogOption(OPTIONS[1]);
            else
                imageEventText.setDialogOption(OPTIONS[7],true);
            imageEventText.setDialogOption(OPTIONS[4]);
            currentPhase = Phase.TRADE;
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch (currentPhase) {
            case TRADE:
                if(i==0){
                    obtainRelic = true;
                    trading = true;
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    ArrayList<AbstractRelic> relicsToTrade = getRelicsToTrade();
                    CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    tmp.group = new ArrayList<>();
                    for(AbstractRelic r : relicsToTrade){
                        RelicPreviewCard_SamiRouge previewCard = new RelicPreviewCard_SamiRouge();
                        previewCard.setRelic(r);
                        tmp.group.add(previewCard);
                    }
                    AbstractDungeon.gridSelectScreen.open(tmp, 1, OPTIONS[10], false);
                    currentPhase = Phase.OBTAIN;
                }
                else if(i==1){
                    obtainRelic = false;
                    trading = true;
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    ArrayList<AbstractCard> cardsToTrade = getCardsToTrade();
                    for(AbstractCard c : cardsToTrade){
                        UnlockTracker.markCardAsSeen(c.cardID);
                    }
                    CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    tmp.group = cardsToTrade;
                    AbstractDungeon.gridSelectScreen.open(tmp, 1, OPTIONS[10], false);
                    currentPhase = Phase.OBTAIN;
                }
                else if(i==2){
                    AbstractDungeon.player.heal(5,true);
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    imageEventText.setDialogOption(OPTIONS[5]);
                    currentPhase = Phase.END;
                }
                break;
            case OBTAIN:
                if(obtainRelic){
                    if(i<relicsToObtain.size()){
                        AbstractRelic r = relicsToObtain.remove(i);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH/2F - 100F*Settings.scale,(float) Settings.HEIGHT/2F, r.makeCopy());
                        for(AbstractRelic r2 : relicsToObtain){
                            if(r2.tier == AbstractRelic.RelicTier.COMMON){
                                AbstractDungeon.commonRelicPool.add(r2.relicId);
                            }
                            else if(r2.tier == AbstractRelic.RelicTier.UNCOMMON){
                                AbstractDungeon.uncommonRelicPool.add(r2.relicId);
                            }
                            else if(r2.tier == AbstractRelic.RelicTier.RARE){
                                AbstractDungeon.rareRelicPool.add(r2.relicId);
                            }
                            else if(r2.tier == AbstractRelic.RelicTier.BOSS){
                                AbstractDungeon.bossRelicPool.add(r2.relicId);
                            }
                        }
                    }
                }
                else {
                    if(i<cardsToObtain.size()){
                        AbstractCard c = cardsToObtain.get(i).makeCopy();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                    }
                }
                imageEventText.clearAllDialogs();
                imageEventText.updateBodyText(DESCRIPTIONS[2]);
                boolean hasCome = false;
                AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                if(anti!=null){
                    int needCome = tradeTimes>=2?2:1;
                    if(anti.counter>=needCome)
                        hasCome = true;
                }
                if(hasCome){
                    imageEventText.setDialogOption(OPTIONS[tradeTimes>=2?13:3]);
                }
                else {
                    imageEventText.setDialogOption(OPTIONS[tradeTimes>=2?14:8],true);
                }
                imageEventText.setDialogOption(OPTIONS[9]);
                boolean hasCipher = false;
                for(AbstractCipherTextCard c: DeclareHelper.buffed){
                    if(c instanceof C32){
                        hasCipher = true;
                        break;
                    }
                }
                if(hasCipher){
                    imageEventText.setDialogOption(OPTIONS[11]);
                }
                currentPhase = Phase.LEAVE;
                break;
            case LEAVE:
                if(i==0){
                    AbstractBlight blight = AbstractDungeon.player.getBlight(AntiInterference.ID);
                    if(blight!=null){
                        if(blight.counter>0)
                            ((AntiInterference)blight).useOne();
                        if(tradeTimes>=2){
                            if(blight.counter>0)
                                ((AntiInterference)blight).useOne();
                        }
                    }
                    tradeTimes++;
                    initializeOptions();
                }
                else if(i==1){
                    imageEventText.clearAllDialogs();
                    imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    imageEventText.setDialogOption(OPTIONS[5]);
                    currentPhase = Phase.END;
                }
                else if(i==2){
                    for(AbstractCipherTextCard c: DeclareHelper.buffed){
                        if(c instanceof C32){
                            DeclareHelper.otherTrigger(c);
                            break;
                        }
                    }
                    tradeTimes++;
                    initializeOptions();
                }
                break;
            case END:
                openMap();
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if (this.trading && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            imageEventText.clearAllDialogs();
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            if(obtainRelic && c instanceof RelicPreviewCard_SamiRouge) {
                RelicPreviewCard_SamiRouge tmp = (RelicPreviewCard_SamiRouge)c;
                AbstractRelic r = tmp.getRelicCopy();
                if(r!=null){
                    AbstractDungeon.player.loseRelic(r.relicId);
                    ArrayList<AbstractRelic> relics = SamiRougeHelper.getRebuildRelics(r,3);
                    this.relicsToObtain = relics;
                    for(AbstractRelic r2 : relics){
                        imageEventText.setDialogOption(OPTIONS[2] + r2.name,r2);
                    }
                }
            }
            else {
                AbstractDungeon.player.masterDeck.removeCard(c);
                ArrayList<AbstractCard> cards = SamiRougeHelper.getRebuildCards(c,3);
                this.cardsToObtain = cards;
                for(AbstractCard c2 : cards){
                    imageEventText.setDialogOption(OPTIONS[2] + c2.name,c2);
                }
            }
            this.trading = false;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            //bug de
            if(imageEventText.optionList.isEmpty()){
                imageEventText.clearAllDialogs();
                imageEventText.updateBodyText(DESCRIPTIONS[3]);
                imageEventText.setDialogOption(OPTIONS[5]);
                currentPhase = Phase.END;
            }
        }
    }

    private boolean canTradeRelic(){
        for(AbstractRelic r : AbstractDungeon.player.relics){
            if(r.tier == AbstractRelic.RelicTier.COMMON || r.tier == AbstractRelic.RelicTier.UNCOMMON || r.tier == AbstractRelic.RelicTier.RARE){
                return true;
            }
        }
        return false;
    }

    private boolean canTradeCard(){
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group){
            if(c.rarity == AbstractCard.CardRarity.BASIC || c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE){
                return true;
            }
        }
        return false;
    }

    private ArrayList<AbstractRelic> getRelicsToTrade(){
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        for(AbstractRelic r : AbstractDungeon.player.relics){
            if(r.tier == AbstractRelic.RelicTier.COMMON || r.tier == AbstractRelic.RelicTier.UNCOMMON || r.tier == AbstractRelic.RelicTier.RARE){
                relics.add(r);
            }
        }
        return relics;
    }

    private ArrayList<AbstractCard> getCardsToTrade(){
        ArrayList<AbstractCard> cards = new ArrayList<>();
        for(AbstractCard c : AbstractDungeon.player.masterDeck.group){
            if(c.rarity == AbstractCard.CardRarity.BASIC || c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE){
                cards.add(c);
            }
        }
        return cards;
    }


    public enum Phase{
        TRADE,
        OBTAIN,
        LEAVE,
        END
    }

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        NAME = eventStrings.NAME;;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
    }
}
