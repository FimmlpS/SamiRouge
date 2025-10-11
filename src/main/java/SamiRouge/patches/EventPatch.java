package SamiRouge.patches;

import SamiRouge.dungeons.TheSami;
import SamiRouge.events.*;
import SamiRouge.rooms.THEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

import java.util.ArrayList;

public class EventPatch {
    public static ArrayList<String> specialEvents = new ArrayList<>();

    public static void resetEvents(){
        specialEvents = new ArrayList<>();
        specialEvents.add(BeforeWoodCrack.ID);
        specialEvents.add(SadCage.ID);
        specialEvents.add(NoMountains.ID);
        specialEvents.add(Change.ID);
        specialEvents.add(FarSee.ID);
        specialEvents.add(DarkRoom.ID);
        specialEvents.add(Rain.ID);
        specialEvents.add(CureRitual.ID);
    }

    @SpirePatch(clz = AbstractDungeon.class,method = "generateRoom")
    public static class GenerateRoomPatch{
        @SpirePostfixPatch
        public static AbstractRoom Postfix(AbstractRoom _ret, AbstractDungeon _inst, EventHelper.RoomResult roomType){
            if(roomType== EventHelper.RoomResult.EVENT){
                if(AbstractDungeon.id.equals(TheSami.ID)&&TheSami.isTHEventRoom())
                    return new THEventRoom();
            }
            return _ret;
        }
    }

    @SpirePatch(clz = ProceedButton.class,method = "update")
    public static class ChangePatch{
        @SpireInsertPatch(rloc = 51)
        public static void Insert(ProceedButton _inst){
            if(AbstractDungeon.getCurrRoom().event instanceof Change){
                AbstractDungeon.dungeonMapScreen.open(false);
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
            }
        }
    }

    //确保树洞内问号节点只出现事件
    @SpirePatch(clz = EventHelper.class,method = "roll",paramtypez = {Random.class})
    public static class MustEventPatch{
        @SpirePostfixPatch
        public static EventHelper.RoomResult Postfix(EventHelper.RoomResult _ret, Random eventRng){
            if(AbstractDungeon.id !=null && AbstractDungeon.id.equals(TheSami.ID)){
                return EventHelper.RoomResult.EVENT;
            }
            return _ret;
        }
    }

    //确保不连续遭遇同一个普通精英敌人
    @SpirePatch(clz = AbstractDungeon.class,method = "getEliteMonsterForRoomCreation")
    public static class GetEliteMonsterForRoomCreationPatch{
        @SpirePostfixPatch
        public static MonsterGroup Postfix(MonsterGroup _ret, AbstractDungeon _inst){
            if(AbstractDungeon.id !=null && AbstractDungeon.id.equals(TheSami.ID)){
                TheSami.lastNormalElite = AbstractDungeon.lastCombatMetricKey;
            }
            return _ret;
        }

    }

    static {
        resetEvents();
    }
}
