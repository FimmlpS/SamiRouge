package SamiRouge.patches;

import SamiRouge.blights.AntiInterference;
import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.dungeons.TheSami;
import SamiRouge.helper.AltMonsterHelper;
import SamiRouge.helper.AltStringHelper;
import SamiRouge.monsters.Smkght;
import SamiRouge.rooms.THEventRoom;
import SamiRouge.rooms.TreeHoleOuterRoom;
import SamiRouge.samiMod.ModConfig;
import SamiRouge.samiMod.SamiRougeHelper;
import SamiRouge.save.SamiTreeHoleSave;
import TreeHole.mod.TreeHoleHelper;
import basemod.ReflectionHacks;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

public class SamiTreeHolePatch {

    private static boolean enableNoKill = false;

    private static final Logger logger = LogManager.getLogger(SamiTreeHolePatch.class.getName());

    public static ArrayList<Integer> treeHoleTypes;

    public static boolean whatBeginsFollowsWhatEnds;
    public static boolean toTalkWithMountains;
    public static boolean winterFall;
    public static boolean longIntoAnAbyss;
    public static boolean withHerTalk;
    public static boolean enterDoubleKing;
    public static boolean enteredDoubleKing;
    public static int treeHoleEntered;
    public static int bossKilled;
    public static int monsterRngCounterExtra;
    public static boolean getDimensionalFluidity;
    public static String currentSamiBoss = "";
    public static ArrayList<String> specialElites = new ArrayList<>();
    public static SamiTreeHoleSave samiSave = new SamiTreeHoleSave();

    private static final ArrayList<String> eyeNames;


    @SpirePatch(clz = ProceedButton.class,method = "update")
    public static class EndSamiPatch{
        @SpireInsertPatch(rloc = 40)
        public static void Insert(ProceedButton _inst){
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                if (AbstractDungeon.id.equals(TheSami.ID)) {
                    CardCrawlGame.music.fadeOutBGM();
                    MapRoomNode node = new MapRoomNode(3, 6);
                    if(TreeHoleHelper.getCurrentType()!=4){
                        TreasureRoomBoss r = new TreasureRoomBoss();
                        node.room = r;
                    }
                    else {
                        node.room = new TrueVictoryRoom();
                    }
                    AbstractDungeon.nextRoom = node;
                    AbstractDungeon.closeCurrentScreen();
                    AbstractDungeon.nextRoomTransitionStart();
                    _inst.hide();
                }
            }
        }
    }

    @SpirePatch(clz = ProceedButton.class,method = "goToNextDungeon")
    public static class ReturnToPatch{
        @SpireInsertPatch(rloc = 26)
        public static SpireReturn<Void> Insert(ProceedButton _inst,AbstractRoom room){
            if (AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss) {
                if (AbstractDungeon.id.equals(TheSami.ID)&&TreeHoleHelper.getCurrentType()!=4) {
                    CardCrawlGame.music.fadeOutBGM();
                    MapRoomNode node = new MapRoomNode(3, 7);
                    TreeHoleOuterRoom r = new TreeHoleOuterRoom();
                    r.renderThis = false;
                    node.room = r;
                    AbstractDungeon.nextRoom = node;
                    AbstractDungeon.closeCurrentScreen();
                    AbstractDungeon.nextRoomTransitionStart();
                    _inst.hide();
                    return SpireReturn.Return();
                }
            }

            return SpireReturn.Continue();
        }
    }


    @SpirePatch(clz = DungeonMapScreen.class,method = "updateImage")
    public static class RenderFinalNodePatch{
        @SpirePostfixPatch
        public static void Postfix(DungeonMapScreen _inst){
            if(!AbstractDungeon.id.equals(TheSami.ID))
                return;
            Iterator<ArrayList<MapRoomNode>> var1 = CardCrawlGame.dungeon.getMap().iterator();
            ArrayList<MapRoomNode> vis = ReflectionHacks.getPrivate(_inst,DungeonMapScreen.class,"visibleMapNodes");
            while(var1.hasNext()) {
                ArrayList<MapRoomNode> rows = var1.next();
                for (MapRoomNode node : rows) {
                    if (!node.hasEdges() && node.room instanceof TreeHoleOuterRoom) {
                        if(((TreeHoleOuterRoom) node.room).renderThis)
                            vis.add(node);
                    }
                }
            }
        }
    }

    //范式
    @SpirePatch(clz = BlightHelper.class,method = "getBlight")
    public static class GetBlightPatch {
        @SpirePostfixPatch
        public static AbstractBlight Postfix(AbstractBlight _ret, String id) {
            AbstractBlight tmp = SamiRougeHelper.getBlightByID(id);
            if (tmp != null)
                return tmp;
            return _ret;
        }
    }

    @SpirePatch(clz = AbstractMonster.class,method = "useUniversalPreBattleAction")
    public static class UniversalPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractMonster _inst){
            if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
                return SpireReturn.Continue();
            if(AllGlobalPatch.applyGlobalInSami.get(_inst))
                return SpireReturn.Return();
            return SpireReturn.Continue();
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst){
            AllGlobalPatch.applyGlobalInSami.set(_inst,true);
        }
    }

    //萨米树洞内所有敌人施加global判断
    @SpirePatch(clz = AbstractMonster.class,method = SpirePatch.CLASS)
    public static class AllGlobalPatch{
        public static SpireField<Boolean> applyGlobalInSami = new SpireField<Boolean>(()->false);
        public static SpireField<Boolean> damaged = new SpireField<Boolean>(()->false);
        public static SpireField<Integer> lastHealth = new SpireField<Integer>(()->0);
    }

    @SpirePatch(clz = MonsterRoomElite.class,method = SpirePatch.CLASS)
    public static class SpireEliteRoomPatch{
        public static SpireField<Boolean> specialElite = new SpireField<Boolean>(()->false);
    }

    @SpirePatch(clz = AbstractMonster.class,method = "calculateDamage")
    public static class CalculateDamagePatch{
        @SpireInsertPatch(rloc = 10,localvars = {"tmp"})
        public static void Insert(AbstractMonster _inst,int dmg,@ByRef float[] tmp){
            AbstractBlight irr = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
            if(irr!=null){
                tmp[0] *= irr.effectFloat();
            }
        }
    }

    @SpirePatch(clz = DamageInfo.class,method = "applyPowers")
    public static class ApplyDamagePatch{
        @SpireInsertPatch(rloc = 17,localvars = {"tmp"})
        public static void Insert(DamageInfo _inst, AbstractCreature owner,AbstractCreature target, @ByRef float[] tmp){
            AbstractBlight irr = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
            if(irr!=null){
                tmp[0] *= irr.effectFloat();
            }
        }
    }

    //进入双王关 以及清空掉落
    @SpirePatch(clz = AbstractDungeon.class,method = "nextRoomTransition",paramtypez = {SaveFile.class})
    public static class EnterEternalPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractDungeon _inst, SaveFile saveFile){
            //清理存档记录
            if(AbstractDungeon.floorNum<=1){
                samiSave.onReset();
            }

            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && AbstractDungeon.lastCombatMetricKey.equals(Smkght.BOSS_ID)) {
                AbstractDungeon.player.movePosition((float) Settings.WIDTH / 2.0F, AbstractDungeon.floorY);
                //2025/7/27更新，将双王移动到隐藏层，且在此标记已经进入过
                enteredDoubleKing = true;
            }
            else if(AbstractDungeon.getCurrRoom() instanceof MonsterRoom && AbstractDungeon.lastCombatMetricKey.equals(AltMonsterHelper.BANDITS_AND_SLAVERS)){
                AbstractDungeon.player.movePosition((float) Settings.WIDTH / 2.0F, AbstractDungeon.floorY);
            }
            //reset
            if(AbstractDungeon.floorNum<=1){
                getDimensionalFluidity = false;
            }
            //2025/6/1更新，新增抗干扰指数，若不拥有抗干扰指数，则获得一个对应blight
            AbstractBlight antiI = AbstractDungeon.player.getBlight(AntiInterference.ID);
            if(antiI == null){
                if(AbstractDungeon.getCurrMapNode()!=null && AbstractDungeon.getCurrRoom() != null){
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new AntiInterference());
                }
            }

            //2025/10/4更新，判断是否下雪
            if(CardCrawlGame.dungeon instanceof TheSami && TreeHoleHelper.getCurrentType()!=4){
                SnowPatch.isSnowing = true;
            }
            else
                SnowPatch.isSnowing = false;
        }
    }

    //捏奥房
    @SpirePatch(clz = NeowRoom.class,method = "onPlayerEntry")
    public static class NeowRoomPatch{
        @SpirePostfixPatch
        public static void Postfix(NeowRoom _inst){
            AbstractBlight antiI = AbstractDungeon.player.getBlight(AntiInterference.ID);
            if(antiI == null){
                _inst.spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new AntiInterference());
            }
        }
    }

    //特殊精英
    @SpirePatch(clz = MonsterRoomElite.class,method = "onPlayerEntry")
    public static class PlayerElitePatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(MonsterRoomElite _inst){
            if(SpireEliteRoomPatch.specialElite.get(_inst)){
                _inst.playBGM((String)null);
                if (_inst.monsters == null) {
                    _inst.monsters = CardCrawlGame.dungeon.getEliteMonsterForRoomCreation();
                    ArrayList<String> monsterList = new ArrayList<>(TheSami.specialElites);
                    monsterList.removeAll(specialElites);
                    if(monsterList.size() <= 0) {
                        specialElites.clear();
                        monsterList = new ArrayList<>(TheSami.specialElites);
                    }
                    String rollMonster = monsterList.get(AbstractDungeon.monsterRng.random(monsterList.size()-1));
                    specialElites.add(rollMonster);
                    _inst.monsters = MonsterHelper.getEncounter(rollMonster);
                    _inst.monsters.init();
                }

                AbstractRoom.waitTimer = 0.1F;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //特殊精英额外掉落一个普通遗物 2025/10/19 废除
//    @SpirePatch(clz = MonsterRoomElite.class,method = "dropReward")
//    public static class RelicElitePatch{
//        @SpirePostfixPatch
//        public static void Postfix(MonsterRoomElite _inst){
//            if(SpireEliteRoomPatch.specialElite.get(_inst)){
//                _inst.addRelicToRewards(AbstractRelic.RelicTier.COMMON);
//            }
//        }
//    }

    //树洞内敌人禁止被斩杀或平摊生命
    @SpirePatch(clz = MonsterGroup.class,method = "update")
    public static class HealthRecallPatch{
        @SpirePostfixPatch
        public static void Postfix(MonsterGroup _inst){
            if(!enableNoKill)
                return;
            if(!AbstractDungeon.id.equals(TheSami.ID))
                return;
            for(AbstractMonster m:_inst.monsters){
                int lastHealth = AllGlobalPatch.lastHealth.get(m);
                if(m.currentHealth>lastHealth){
                    AllGlobalPatch.lastHealth.set(m,m.currentHealth);
                }
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class,method = "damage")
    public static class DamageRecallPatch{
        @SpireInsertPatch(rloc = 0)
        public static void Insert(AbstractMonster _inst,DamageInfo info){
            if(!enableNoKill)
                return;
            if(!AbstractDungeon.id.equals(TheSami.ID))
                return;
            AllGlobalPatch.damaged.set(_inst,true);
        }
    }

    @SpirePatch(clz = AbstractCreature.class,method = "healthBarUpdatedEvent")
    public static class HealthBarRecallPatch{
        @SpirePrefixPatch
        public static void Prefix(AbstractCreature _inst){
            if(!enableNoKill)
                return;
            if(!AbstractDungeon.id.equals(TheSami.ID))
                return;
            if(_inst instanceof AbstractMonster){
                AbstractMonster m = (AbstractMonster) _inst;
                int lastHealth = AllGlobalPatch.lastHealth.get(m);
                if(AllGlobalPatch.damaged.get(m)){
                    AllGlobalPatch.damaged.set(m,false);
                    AllGlobalPatch.lastHealth.set(m,m.currentHealth);
                }
                else if(m.currentHealth<lastHealth){
                    AbstractDungeon.actionManager.addToTop(new TalkAction(m, AltStringHelper.errorHealthTrigger,0.5F,2.0F));
                    m.currentHealth = lastHealth;
                }
            }
        }
    }

    private static Bone getEyeBone(Skeleton skeleton){
        Bone bone = null;
        if(skeleton!=null){
            for (String eyeName : eyeNames) {
                bone = skeleton.findBone(eyeName);
                if (bone != null)
                    break;
            }
        }

        return bone;
    }



    public static void rollTreeHoleType(Random rng) {
        //树洞的actNum默认为当前actNum+1，离开树洞时actNum回归正常，因此一、二、三幕树洞在生成type时的actNum分别为为1，2，3（树洞内2，3，4）
        //树洞类型：0-非战斗为主的树洞，1-战斗为主的树洞，2-精英树洞，3-BOSS树洞；
        //2025/3/26更新
        //1、2、3幕分别随机出现1、2、3种树洞
        //2025/7/7更新
        //1幕会出现2种树洞
        treeHoleTypes = new ArrayList<>();
        if (AbstractDungeon.id.equals(TheEnding.ID)) {
            treeHoleTypes.add(4);
        }
        else {
            if (AbstractDungeon.actNum == 1) {
                //int random = rng.random(1);
                treeHoleTypes.add(0);
                treeHoleTypes.add(1);
            } else {
//                int noRandom = rng.random(2);
//                for (int i = 2; i >= 0; i--) {
//                    if (i != noRandom)
//                        treeHoleTypes.add(i);
//                }

                //必定精英洞
                treeHoleTypes.add(2);
                int random = rng.random(1);
                treeHoleTypes.add(random);
            }
        }
    }

    static {
        eyeNames = new ArrayList<>();
        eyeNames.add("eye");
        eyeNames.add("eyes");
        eyeNames.add("eye3");
        eyeNames.add("eye4");
        eyeNames.add("eye1");
        eyeNames.add("eye2");
        eyeNames.add("eyelid");
        eyeNames.add("Eye_L");
        eyeNames.add("Eye_R");
        eyeNames.add("head");
        eyeNames.add("face");
        eyeNames.add("upper_body");
        eyeNames.add("lower_body");
    }


}
