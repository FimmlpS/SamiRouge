package SamiRouge.helper;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.cards.ciphertext.layout.*;
import SamiRouge.cards.ciphertext.reason.*;
import SamiRouge.modifiers.CycleModifier;
import SamiRouge.modifiers.ExpandModifier;
import SamiRouge.modifiers.LastModifier;
import SamiRouge.modifiers.SustainModifier;
import SamiRouge.patches.DeclarePatch;
import SamiRouge.save.SamiTreeHoleSave;
import basemod.BaseMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.GainGoldTextEffect;

import java.util.ArrayList;
import java.util.Collections;

public class DeclareHelper {
    public static ArrayList<AbstractCipherTextCard> allCards;
    public static ArrayList<String> noInTogether;

    public static ArrayList<AbstractCipherTextCard> layout = new ArrayList<>();
    public static ArrayList<AbstractCipherTextCard> reason = new ArrayList<>();
    //正在生效的密文板
    public static ArrayList<AbstractCipherTextCard> buffed = new ArrayList<>();

    //密文板临时增益
    public static int battleMaxHPIncreased = 0;
    public static ArrayList<AbstractRelic> battleRelicObtain = new ArrayList<>();

    public static void test(SamiTreeHoleSave save){
        save.layoutsAndReasons.clear();
        save.rhetoric.clear();
        for (AbstractCipherTextCard card : allCards) {
            save.layoutsAndReasons.add(card.cardID);
            save.rhetoric.add(0);
        }
    }

    public static ArrayList<AbstractCipherTextCard> getRandomCipher(int amount, Random random, CipherText.CipherType type) {
        if(amount>allCards.size()){
            amount=allCards.size();
        }
        ArrayList<AbstractCipherTextCard> retCards = new ArrayList<>();
        ArrayList<AbstractCipherTextCard> cards = new ArrayList<>(allCards);
        if(type!=null){
            cards.removeIf(c->c.cipherText.type!=type);
        }
        Collections.shuffle(cards,random.random);
        for(int i =0;i<amount&&i<cards.size();i++){
            if(BaseMod.hasModID("spireTogether:") && noInTogether.contains(cards.get(i).cardID)){
                continue;
            }
            AbstractCard c = cards.get(i).makeCopy();
            if(c instanceof AbstractCipherTextCard){
                AbstractCipherTextCard cc = (AbstractCipherTextCard)c;
                //random 32%
                int randomOne = random.random(0,99);
                if(randomOne<8){
                    CardModifierManager.addModifier(cc,new LastModifier());
                }
                else if(randomOne<16){
                    CardModifierManager.addModifier(cc,new SustainModifier());
                }
                else if(randomOne<24) {
                    CardModifierManager.addModifier(cc, new CycleModifier());
                }
                else if(randomOne<32){
                    CardModifierManager.addModifier(cc, new ExpandModifier());
                }

                retCards.add(cc);
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
        cardsToAdd.add(new C07());
        cardsToAdd.add(new C08());
        cardsToAdd.add(new C09());
        cardsToAdd.add(new C10());
        cardsToAdd.add(new C11());
        cardsToAdd.add(new C12());
        cardsToAdd.add(new C13());
        cardsToAdd.add(new C14());
        cardsToAdd.add(new C15());
        cardsToAdd.add(new C16());
        cardsToAdd.add(new C17());
        cardsToAdd.add(new C18());
        cardsToAdd.add(new C19());
        cardsToAdd.add(new C20());
        cardsToAdd.add(new C21());

        cardsToAdd.add(new C22());
        cardsToAdd.add(new C23());
        cardsToAdd.add(new C24());
        cardsToAdd.add(new C25());
        cardsToAdd.add(new C26());
        cardsToAdd.add(new C27());
        cardsToAdd.add(new C28());
        cardsToAdd.add(new C29());
        cardsToAdd.add(new C30());
        cardsToAdd.add(new C31());
        cardsToAdd.add(new C32());
        cardsToAdd.add(new C33());
        cardsToAdd.add(new C34());
        cardsToAdd.add(new C35());
        cardsToAdd.add(new C36());
        cardsToAdd.add(new C37());
        cardsToAdd.add(new C38());
        cardsToAdd.add(new C39());
        cardsToAdd.add(new C40());
        cardsToAdd.add(new C41());
        cardsToAdd.add(new C42());
        cardsToAdd.add(new C43());

        if(allCards==null){
            allCards = new ArrayList<>();
            for(AbstractCipherTextCard card : cardsToAdd){
                AbstractCard c = card.makeCopy();
                if(c instanceof AbstractCipherTextCard && ((AbstractCipherTextCard) c).cipherText.color!= CipherText.CipherColor.Thefair)
                    allCards.add((AbstractCipherTextCard) c);
            }
        }
        if(noInTogether==null){
            noInTogether = new ArrayList<>();
            noInTogether.add(C38.ID);
            noInTogether.add(C41.ID);
        }
        return cardsToAdd;
    }

    public static void onInitialize(){
        AbstractCipherTextCard l = getRandomCipher(1,AbstractDungeon.cardRng, CipherText.CipherType.Layout).get(0);
        AbstractCipherTextCard r = getRandomCipher(1,AbstractDungeon.cardRng, CipherText.CipherType.Reason).get(0);
        layout.add(l);
        reason.add(r);
    }


    public static void declare(AbstractCipherTextCard layoutC,AbstractCipherTextCard reasonC) {
        boolean together = CipherText.isTogether(layoutC.cipherText,reasonC.cipherText);
        int rhyLayout = getRhetoric(layoutC);
        int rhyReason = getRhetoric(reasonC);
        int x = layoutC.declare(together);
        if(rhyLayout==4)
            x++;
        if(rhyReason==4)
            x++;
        if(x>0){
            reasonC.declared(x,together);
            if(reasonC.remainX>0){
                buffed.add(reasonC);
                DeclarePatch.getInstance().onBuffedChanged();
            }
            reasonC.triggerAfterDeclareAtOnce();
        }
        if(rhyLayout!=1)
            layout.remove(layoutC);
        else {
            CardModifierManager.removeModifiersById(layoutC,LastModifier.ID,true);
        }
        if(rhyReason!=1)
            reason.remove(reasonC);
        else {
            CardModifierManager.removeModifiersById(reasonC,LastModifier.ID,true);
        }
        if(together && rhyLayout==2 && AbstractDungeon.player!=null){
            int heal = (int)(0.1F*(float) AbstractDungeon.player.maxHealth);
            AbstractDungeon.player.heal(heal,true);
        }
        if(together && rhyReason==2 && AbstractDungeon.player!=null){
            int heal = (int)(0.1F*(float) AbstractDungeon.player.maxHealth);
            AbstractDungeon.player.heal(heal,true);
        }
        if(together && rhyLayout==3 && AbstractDungeon.player!=null){
            int gold = 25;
            AbstractDungeon.effectsQueue.add(new GainGoldTextEffect(gold));
            AbstractDungeon.player.gainGold(gold);
        }
        if(together && rhyReason==3 && AbstractDungeon.player!=null){
            int gold = 25;
            AbstractDungeon.effectsQueue.add(new GainGoldTextEffect(gold));
            AbstractDungeon.player.gainGold(gold);
        }
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

    public static void otherTrigger(AbstractCipherTextCard c){
        if(buffed.contains(c)){
            c.triggerOnce();
            if(c.remainX<=0){
                onBuffUsed(c);
                DeclarePatch.getInstance().onBuffedChanged();
            }
        }
    }

    public static void onBuffUsed(AbstractCipherTextCard c){
        buffed.remove(c);
        //effect
    }

    public static int getRhetoric(AbstractCard c){
        if(CardModifierManager.hasModifier(c,LastModifier.ID))
            return 1;
        if(CardModifierManager.hasModifier(c,SustainModifier.ID))
            return 2;
        if(CardModifierManager.hasModifier(c,CycleModifier.ID))
            return 3;
        if(CardModifierManager.hasModifier(c,ExpandModifier.ID))
            return 4;
        return 0;
    }

    public static void onSave(SamiTreeHoleSave save){
        save.layoutsAndReasons = new ArrayList<>();
        save.rhetoric = new ArrayList<>();
        for(AbstractCipherTextCard c:layout){
            save.layoutsAndReasons.add(c.cardID);
            save.rhetoric.add(getRhetoric(c));
        }
        for(AbstractCipherTextCard c:reason){
            save.layoutsAndReasons.add(c.cardID);
            save.rhetoric.add(getRhetoric(c));
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
            //test = true;
        }
        layout.clear();
        reason.clear();
        buffed.clear();
        if(test)
            test(save);
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
                int rhetoric = save.rhetoric.get(index);
                if(rhetoric == 1){
                    CardModifierManager.addModifier(cc,new LastModifier());
                }
                else if(rhetoric == 2){
                    CardModifierManager.addModifier(cc,new SustainModifier());
                }
                else if(rhetoric == 3){
                    CardModifierManager.addModifier(cc,new CycleModifier());
                }
                else if(rhetoric == 4){
                    CardModifierManager.addModifier(cc,new ExpandModifier());
                }
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
        DeclarePatch.getInstance().onBuffedChanged();
    }

    public static boolean isBattle(){
        if(AbstractDungeon.getCurrMapNode() == null || AbstractDungeon.getCurrRoom() == null){
            return false;
        }
        return AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.actionManager != null && !AbstractDungeon.actionManager.turnHasEnded;
    }
}
