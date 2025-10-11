package SamiRouge.helper;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.cards.ciphertext.layout.*;
import SamiRouge.cards.ciphertext.reason.*;
import SamiRouge.patches.DeclarePatch;
import SamiRouge.save.SamiTreeHoleSave;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Collections;

public class DeclareHelper {
    public static ArrayList<AbstractCipherTextCard> allCards;

    public static ArrayList<AbstractCipherTextCard> layout = new ArrayList<>();
    public static ArrayList<AbstractCipherTextCard> reason = new ArrayList<>();
    //正在生效的密文板
    public static ArrayList<AbstractCipherTextCard> buffed = new ArrayList<>();

    //密文板临时增益
    public static int battleMaxHPIncreased = 0;
    public static ArrayList<AbstractRelic> battleRelicObtain = new ArrayList<>();

    public static void test(SamiTreeHoleSave save){
        for (AbstractCipherTextCard card : allCards) {
            save.layoutsAndReasons.add(card.cardID);
            save.rhetoric.add(0);
        }
    }

    public static ArrayList<AbstractCipherTextCard> getRandomCipher(int amount){
        if(amount>allCards.size()){
            amount=allCards.size();
        }
        ArrayList<AbstractCipherTextCard> retCards = new ArrayList<>();
        ArrayList<AbstractCipherTextCard> cards = new ArrayList<>(allCards);
        Collections.shuffle(cards,AbstractDungeon.cardRng.random);
        for(int i =0;i<amount;i++){
            AbstractCard c = cards.get(i).makeCopy();
            if(c instanceof AbstractCipherTextCard){
                retCards.add((AbstractCipherTextCard)c);
            }
        }
        return retCards;
    }

    public static ArrayList<AbstractCipherTextCard> getCardsToAdd(){
        ArrayList<AbstractCipherTextCard> cardsToAdd = new ArrayList<>();
        cardsToAdd.add(new C01());
        cardsToAdd.add(new C02());
        cardsToAdd.add(new C03());
        cardsToAdd.add(new C04());
        cardsToAdd.add(new C05());
        cardsToAdd.add(new C06());

        cardsToAdd.add(new C22());
        cardsToAdd.add(new C23());
        cardsToAdd.add(new C24());
        cardsToAdd.add(new C25());
        cardsToAdd.add(new C26());
        cardsToAdd.add(new C27());

        if(allCards==null){
            allCards = new ArrayList<>();
            for(AbstractCipherTextCard card : cardsToAdd){
                AbstractCard c = card.makeCopy();
                if(c instanceof AbstractCipherTextCard && ((AbstractCipherTextCard) c).cipherText.color!= CipherText.CipherColor.Thefair)
                    allCards.add((AbstractCipherTextCard) c);
            }
        }
        return cardsToAdd;
    }


    public static void declare(AbstractCipherTextCard layoutC,AbstractCipherTextCard reasonC) {
        boolean together = CipherText.isTogether(layoutC.cipherText,reasonC.cipherText);
        int x = layoutC.declare(together);
        if(x>0){
            reasonC.declared(x,together);
            if(reasonC.remainX>0){
                buffed.add(reasonC);
                DeclarePatch.getInstance().onBuffedChanged();
            }
        }
        layout.remove(layoutC);
        reason.remove(reasonC);
    }

    public static void atBattleStart(){
        battleMaxHPIncreased = 0;
        battleRelicObtain.clear();

        ArrayList<AbstractCipherTextCard> cardsToRemove = new ArrayList<>();
        for(AbstractCipherTextCard c:buffed){
            c.declareAtBattleStart();
            if(c.remainX<=0)
                cardsToRemove.add(c);
        }

        for(AbstractCipherTextCard c:cardsToRemove){
            onBuffUsed(c);
        }
        if(!cardsToRemove.isEmpty())
            DeclarePatch.getInstance().onBuffedChanged();
    }

    public static void atTurnStart(){
        ArrayList<AbstractCipherTextCard> cardsToRemove = new ArrayList<>();
        for(AbstractCipherTextCard c:buffed){
            c.declareAtTurnStart();
            if(c.remainX<=0)
                cardsToRemove.add(c);
        }

        for(AbstractCipherTextCard c:cardsToRemove){
            onBuffUsed(c);
        }

        if(!cardsToRemove.isEmpty())
            DeclarePatch.getInstance().onBuffedChanged();
    }

    public static void atBattleEnd(){
        if(battleMaxHPIncreased >0){
            AbstractDungeon.player.decreaseMaxHealth(battleMaxHPIncreased);
        }
        for(AbstractRelic r:battleRelicObtain){
            AbstractDungeon.player.relics.remove(r);
        }
        AbstractDungeon.player.reorganizeRelics();
        battleMaxHPIncreased = 0;
        battleRelicObtain.clear();

        ArrayList<AbstractCipherTextCard> cardsToRemove = new ArrayList<>();
        for(AbstractCipherTextCard c:buffed){
            c.declareAtBattleEnd();
            if(c.remainX<=0)
                cardsToRemove.add(c);
        }

        for(AbstractCipherTextCard c:cardsToRemove){
            onBuffUsed(c);
        }

        if(!cardsToRemove.isEmpty())
            DeclarePatch.getInstance().onBuffedChanged();
    }

    public static void onBuffUsed(AbstractCipherTextCard c){
        buffed.remove(c);
        //effect
    }

    public static void onSave(SamiTreeHoleSave save){
        save.layoutsAndReasons = new ArrayList<>();
        save.rhetoric = new ArrayList<>();
        for(AbstractCipherTextCard c:layout){
            save.layoutsAndReasons.add(c.cardID);
            save.rhetoric.add(0);
        }
        for(AbstractCipherTextCard c:reason){
            save.layoutsAndReasons.add(c.cardID);
            save.rhetoric.add(0);
        }
        save.buffed = new ArrayList<>();
        save.remainX = new ArrayList<>();
        save.together = new ArrayList<>();
        for(AbstractCipherTextCard c:buffed){
            save.buffed.add(c.cardID);
            save.remainX.add(c.remainX);
            save.together.add(c.together);
        }
    }

    public static void onLoad(SamiTreeHoleSave save){
        boolean test = false;
        if(save.layoutsAndReasons == null){
            save.layoutsAndReasons = new ArrayList<>();
        }
        if(save.rhetoric == null){
            save.rhetoric = new ArrayList<>();
        }
        if(save.buffed == null){
            save.buffed = new ArrayList<>();
        }
        if(save.remainX == null){
            save.remainX = new ArrayList<>();
        }
        if(save.together == null){
            save.together = new ArrayList<>();
            test = true;
        }
        if(test)
            test(save);
        layout.clear();
        reason.clear();
        buffed.clear();
        int index = 0;
        for(String id:save.layoutsAndReasons){
            AbstractCard c = CardLibrary.getCard(id);
            if(c instanceof AbstractCipherTextCard){
                AbstractCipherTextCard cc = (AbstractCipherTextCard)c.makeCopy();
                if(cc.cipherText != null){
                    if(cc.cipherText.type == CipherText.CipherType.Layout)
                        layout.add(cc);
                    else
                        reason.add(cc);
                }
                if(save.rhetoric.get(index) != 0){}
            }
            index++;
        }
        index = 0;
        for(String id:save.buffed){
            AbstractCard c = CardLibrary.getCard(id);
            if(c instanceof AbstractCipherTextCard){
                AbstractCipherTextCard cc = (AbstractCipherTextCard)c.makeCopy();
                if(cc.cipherText != null){
                    cc.remainX = save.remainX.get(index);
                    cc.together = save.together.get(index);
                    buffed.add(cc);
                }
            }
            index++;
        }

        DeclarePatch.getInstance().onBuffedChanged();
    }

    public static void onDelete(){
        layout.clear();
        reason.clear();
        buffed.clear();
    }

    public static boolean isBattle(){
        if(AbstractDungeon.getCurrMapNode() == null || AbstractDungeon.getCurrRoom() == null){
            return false;
        }
        return AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.actionManager != null && !AbstractDungeon.actionManager.turnHasEnded;
    }
}
