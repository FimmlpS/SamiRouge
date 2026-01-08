package SamiRouge.patches;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.reason.C42;
import SamiRouge.helper.DeclareHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PeacePipe;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.DigOption;
import com.megacrit.cardcrawl.ui.campfire.TokeOption;
import com.megacrit.cardcrawl.vfx.campfire.*;

import java.util.ArrayList;

public class RestPatch {
    public static boolean extraOption = false;
    public static boolean extraOnce = false;

    @SpirePatch(clz = CampfireRecallEffect.class,method = "update")
    public static class CampfireRecallPatch {
        @SpirePostfixPatch
        public static void Postfix(CampfireRecallEffect _inst) {
            if(_inst.isDone){
                triggerJudge();
            }
        }
    }

    @SpirePatch(clz = CampfireSmithEffect.class,method = "update")
    public static class CampfireSmithPatch {
        public static boolean hasSmith = false;

        @SpireInsertPatch(rloc = 8)
        public static void Insert(CampfireSmithEffect _inst) {
            hasSmith = true;
        }

        @SpireInsertPatch(rloc = 22)
        public static void Insert2(CampfireSmithEffect _inst) {
            hasSmith = false;
        }

        @SpirePostfixPatch
        public static void Postfix(CampfireSmithEffect _inst) {
            if(_inst.isDone){
                if(hasSmith)
                    triggerJudge();
                hasSmith = false;
            }
        }
    }

    @SpirePatch(clz = CampfireSleepEffect.class,method = "update")
    public static class CampfireSleepPatch {
        @SpirePostfixPatch
        public static void Postfix(CampfireSleepEffect _inst) {
            if(_inst.isDone){
                boolean hasHealed = ReflectionHacks.getPrivate(_inst, CampfireSleepEffect.class, "hasHealed");
                if(hasHealed)
                    triggerJudge();
            }
        }
    }

    @SpirePatch(clz = CampfireDigEffect.class,method = "update")
    public static class CampfireDigPatch {
        @SpirePostfixPatch
        public static void Postfix(CampfireDigEffect _inst) {
            if(_inst.isDone){
                boolean hasDug = ReflectionHacks.getPrivate(_inst, CampfireDigEffect.class, "hasDug");
                if(hasDug)
                    triggerJudge();
            }
        }
    }

    @SpirePatch(clz = CampfireLiftEffect.class,method = "update")
    public static class CampfireLiftPatch {
        @SpirePostfixPatch
        public static void Postfix(CampfireLiftEffect _inst) {
            if(_inst.isDone){
                triggerJudge();
            }
        }
    }

    @SpirePatch(clz = CampfireTokeEffect.class,method = "update")
    public static class CampfireTokePatch {
        public static boolean hasToke = false;

        @SpireInsertPatch(rloc = 8)
        public static void Insert(CampfireTokeEffect _inst) {
            hasToke = true;
        }

        @SpireInsertPatch(rloc = 18)
        public static void Insert2(CampfireTokeEffect _inst) {
            hasToke = false;
        }

        @SpirePostfixPatch
        public static void Postfix(CampfireTokeEffect _inst) {
            if(_inst.isDone){
                if(hasToke)
                    triggerJudge();
                hasToke = false;
            }
        }
    }

    @SpirePatch(clz = ProceedButton.class,method = "update")
    public static class ProceedButtonPatch {
        @SpireInsertPatch(rloc = 116)
        public static SpireReturn<Void> Insert(ProceedButton _inst) {
            if(extraOnce && AbstractDungeon.getCurrRoom() instanceof RestRoom){
                AbstractDungeon.combatRewardScreen.clear();
                AbstractDungeon.getCurrRoom().rewardTime = false;
                if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
                    AbstractDungeon.closeCurrentScreen();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    public static void triggerJudge(){
        if(AbstractDungeon.getCurrRoom() instanceof RestRoom){
            extraOption = false;
            extraOnce = false;
            boolean once = triggerOnce();
            if(once){
                extraOnce = true;
                RestRoom r = (RestRoom)AbstractDungeon.getCurrRoom();
                r.phase = AbstractRoom.RoomPhase.INCOMPLETE;

                r.campfireUI = new CampfireUI();
            }
        }
    }

    @SpirePatch(clz = CampfireUI.class, method = "initializeButtons")
    public static class AddTearChargePatch {
        @SpireInsertPatch(rloc = 33,localvars = {"buttons"})
        public static void Insert(CampfireUI _inst, ArrayList<AbstractCampfireOption> buttons) {
            if(extraOption){
                extraOption = false;
                boolean addPipe = !AbstractDungeon.player.hasRelic(PeacePipe.ID);
                boolean addShovel = !AbstractDungeon.player.hasRelic(Shovel.ID);
                if(addPipe){
                    buttons.add(new TokeOption(!CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards()).isEmpty()));
                }
                if(addShovel){
                    buttons.add(new DigOption());
                }
            }
        }
    }

    public static boolean triggerOnce(){
        boolean hasGo = false;
        ArrayList<AbstractCipherTextCard> tmp = new ArrayList<>(DeclareHelper.buffed);
        for(AbstractCipherTextCard c : tmp){
            if(c instanceof C42){
                hasGo = true;
                DeclareHelper.otherTrigger(c);
                if(!extraOption && c.together){
                    extraOption = true;
                }
                break;
            }
        }
        return hasGo;
    }
}
