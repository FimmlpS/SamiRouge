package SamiRouge.patches;

import SamiRouge.blights.AntiInterference;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.reason.C38;
import SamiRouge.cards.ciphertext.reason.C39;
import SamiRouge.cards.ciphertext.reason.C40;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.samiMod.ModConfig;
import SamiRouge.samiMod.SamiRougeMod;
import TreeHole.mod.TreeHoleHelper;
import TreeHole.patches.TreeHolePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;

import java.util.ArrayList;

public class MapPatch {
    //用抗来creep
    public static boolean shouldRespawnCrossRoad = false;
    public static ArrayList<Boolean> walked;
    public static ArrayList<Integer> spawnX;
    public static ArrayList<Integer> spawnY;
    //间距，最小为1
    public static ArrayList<Integer> spawnDistance;
    private static final float iconXOffset = Settings.scale * -10F;
    private static final float iconYOffset = Settings.scale * 168F;

    private static final TextureAtlas.AtlasRegion ICON_IMG = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("SamiRougeResources/img/icons/Anti.png"),0,0,128,128);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("samirg:MapPatch");

    //不用保存，每次加载时生成
    public static ArrayList<MapRoomNode> leftNodes = new ArrayList<>();
    public static ArrayList<MapRoomNode> rightNodes = new ArrayList<>();
    public static ArrayList<MapEdge> samiEdges = new ArrayList<>();
    public static void setSpawn(){
        leftNodes.clear();
        rightNodes.clear();
        samiEdges.clear();
        if (spawnX != null && spawnY != null && spawnDistance != null) {
            for(int i = 0; i < spawnX.size(); i++){
                MapRoomNode src = AbstractDungeon.map.get(spawnY.get(i)).get(spawnX.get(i));
                MapRoomNode dst = AbstractDungeon.map.get(spawnY.get(i)).get(spawnX.get(i) + spawnDistance.get(i));
                MapEdge edge = new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false);
                if(walked.get(i))
                    edge.markAsTaken();
                leftNodes.add(src);
                rightNodes.add(dst);
                samiEdges.add(edge);
            }
        }
    }

    public static void resetSpawn() {
        walked = null;
        spawnX = null;
        spawnY = null;
        spawnDistance = null;
        leftNodes.clear();
        rightNodes.clear();
        samiEdges.clear();
        shouldRespawnCrossRoad = false;
    }

    //都用连线左边的节点记录，walked代表这条路线已经走过

    @SpirePatch(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = {String.class, AbstractPlayer.class})
    public static class GetDungeonPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(CardCrawlGame _inst, String key, AbstractPlayer p) {
            //不是加载老地图且不是树洞内，需要重新生成地图的连线
            if (!TreeHoleHelper.contains(key) && !TreeHolePatch.exitTreeHole) {
                shouldRespawnCrossRoad = true;
            } else if (!TreeHoleHelper.contains(key)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    private static Random duplicate;

    @SpirePatch(clz = AbstractDungeon.class, method = "generateMap")
    public static class GenerateMapPatch {
        @SpireInsertPatch(rloc = 32)
        public static void Insert(){
            duplicate = AbstractDungeon.mapRng.copy();
        }

        @SpirePostfixPatch
        public static void Postfix() {
            try {
                //生成逻辑，行数要求：6~map.size()-2，每2个相邻节点间都有20％概率生成横向连线
                if (shouldRespawnCrossRoad) {
                    spawnX = new ArrayList<>();
                    spawnY = new ArrayList<>();
                    walked = new ArrayList<>();
                    spawnDistance = new ArrayList<>();
                    if(duplicate==null){
                        duplicate = AbstractDungeon.mapRng.copy();
                    }
                    float glv = ModConfig.road_spawn_glv * 0.05F;
                    if(glv<=0)
                        glv = 0.2F;
                    for (int i = 6; i < AbstractDungeon.map.size() - 1; i++) {
                        int j = 0;
                        while (j < AbstractDungeon.map.get(i).size() - 1) {
                            MapRoomNode left = AbstractDungeon.map.get(i).get(j);
                            if (left.hasEdges()) {
                                int k = j + 1;
                                while (k < AbstractDungeon.map.get(i).size()) {
                                    MapRoomNode right = AbstractDungeon.map.get(i).get(k);
                                    if (right.hasEdges()) {
                                        boolean spawnHere = duplicate.randomBoolean(glv);
                                        if (spawnHere) {
                                            spawnX.add(j);
                                            spawnY.add(i);
                                            walked.add(false);
                                            spawnDistance.add(k - j);
                                        }
                                        j = k;
                                        break;
                                    }
                                    k++;
                                }
                                if (k == AbstractDungeon.map.get(i).size()) {
                                    break;
                                }
                            } else {
                                j++;
                            }
                        }
                    }
                    shouldRespawnCrossRoad = false;
                }
                if (spawnX != null && spawnY != null) {
                    //整理完所有后或再次加载时，连线
                    setSpawn();
                }
            } catch (Exception e) {
                SamiRougeMod.logSomething(e.getMessage());
            }
        }
    }

    private static float getX(int x){
        return (float)x * (Settings.isMobile ? 140.8F * Settings.xScale : 128.0F * Settings.xScale) + MapRoomNode.OFFSET_X;
    }

    //渲染 Edge只需要左节点渲染即可 同时渲染抗的图标以提示
    @SpirePatch(clz = MapRoomNode.class,method = "render")
    public static class RenderNodePatch {
        @SpirePrefixPatch
        public static void Prefix(MapRoomNode _inst, SpriteBatch sb) {
            if(leftNodes.contains(_inst)) {
                int index = leftNodes.indexOf(_inst);
                MapEdge edge = samiEdges.get(index);
                MapRoomNode right = rightNodes.get(index);
                edge.render(sb);
                if(!walked.get(index)) {
                    float midX = (_inst.offsetX + getX(_inst.x) + right.offsetX + getX(right.x))/2F + iconXOffset;
                    float midY = (_inst.offsetY + Settings.MAP_DST_Y * _inst.y + right.offsetY + Settings.MAP_DST_Y * right.y)/2F + iconYOffset + DungeonMapScreen.offsetY;
                    sb.draw(ICON_IMG,midX,midY,0,0,ICON_IMG.packedWidth,ICON_IMG.packedHeight,0.6F,0.6F,0F);
                }
            }
            //渲染描述 （仅当位于横走其中一格时才能触发）
            boolean renderTip = false;
            if(leftNodes.contains(AbstractDungeon.getCurrMapNode())) {
                int index = leftNodes.indexOf(AbstractDungeon.getCurrMapNode());
                if (_inst == rightNodes.get(index)) {
                    if (!walked.get(index)) {
                        renderTip = true;
                    }
                }
            }
            if(rightNodes.contains(AbstractDungeon.getCurrMapNode())) {
                int index = rightNodes.indexOf(AbstractDungeon.getCurrMapNode());
                if (_inst == leftNodes.get(index)) {
                    if (!walked.get(index)) {
                        renderTip = true;
                    }
                }
            }
            if(renderTip && _inst.hb.hovered){
                TipHelper.renderGenericTip(InputHelper.mX + 12F*Settings.scale, InputHelper.mY,uiStrings.TEXT[0],uiStrings.TEXT[1]);
            }
        }
    }

    //相遇兼容
    private static boolean notCallCome = false;

    //判断是否链接
    //条件：1.有抗 2.未踏足の
    @SpirePatch(clz = MapRoomNode.class,method = "isConnectedTo")
    public static class IsConnectedToPatch {
        @SpirePostfixPatch
        public static boolean Postfix(boolean _ret, MapRoomNode _inst, MapRoomNode node) {
            notCallCome = _ret;
            if(!_ret && AbstractDungeon.player !=null){
                AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                if(anti != null && anti.counter>0){
                    boolean canGo = false;
                    if(leftNodes.contains(node)) {
                        int index = leftNodes.indexOf(node);
                        if (_inst == rightNodes.get(index)) {
                            if (!walked.get(index)) {
                                canGo = true;
                            }
                        }
                    }
                    if(rightNodes.contains(node)) {
                        int index = rightNodes.indexOf(node);
                        if (_inst == leftNodes.get(index)) {
                            if (!walked.get(index)) {
                                canGo = true;
                            }
                        }
                    }
                    return canGo;
                }
            }
            return _ret;
        }
    }

    //设置walked
    @SpirePatch(clz = MapRoomNode.class,method = "update")
    public static class NodeUpdatePatch {
        @SpireInsertPatch(rloc = 13)
        public static void Insert(MapRoomNode _inst) {
            if(rightNodes.contains(AbstractDungeon.getCurrMapNode())) {
                int index = rightNodes.indexOf(AbstractDungeon.getCurrMapNode());
                if(_inst == leftNodes.get(index)) {
                    //判断是否扣抗
                    AbstractDungeon.getCurrMapNode().isConnectedTo(_inst);
                    walked.set(index, true);
                    samiEdges.get(index).markAsTaken();
                    AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                    if(!notCallCome && anti instanceof AntiInterference){
                        ((AntiInterference) anti).useOne();
                    }
                    ArrayList<AbstractCipherTextCard> tmp = new ArrayList<>(DeclareHelper.buffed);
                    for(AbstractCipherTextCard c : tmp){
                        if(c instanceof C39 || c instanceof C40){
                            DeclareHelper.otherTrigger(c);
                        }
                    }
                }
            }
            if(leftNodes.contains(AbstractDungeon.getCurrMapNode())) {
                int index = leftNodes.indexOf(AbstractDungeon.getCurrMapNode());
                if (_inst == rightNodes.get(index)) {
                    //判断是否扣抗
                    AbstractDungeon.getCurrMapNode().isConnectedTo(_inst);
                    walked.set(index, true);
                    samiEdges.get(index).markAsTaken();
                    AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                    if (!notCallCome && anti instanceof AntiInterference) {
                        ((AntiInterference) anti).useOne();
                    }
                    ArrayList<AbstractCipherTextCard> tmp = new ArrayList<>(DeclareHelper.buffed);
                    for(AbstractCipherTextCard c : tmp){
                        if(c instanceof C39 || c instanceof C40){
                            DeclareHelper.otherTrigger(c);
                        }
                    }
                }
            }
        }
    }

    //C38修改房间
    @SpirePatch(clz = AbstractDungeon.class,method = "populatePathTaken")
    public static class PathTakenPatch {
        @SpireInsertPatch(rloc = 25)
        public static void Insert(AbstractDungeon _inst, SaveFile saveFile) {
            C38.enabled = false;
            if (AbstractDungeon.nextRoom != null) {
                AbstractRoom room = AbstractDungeon.nextRoom.room;
                if (room instanceof MonsterRoom && !(room instanceof MonsterRoomElite) && !(room instanceof MonsterRoomBoss)) {
                    boolean hasGo = false;
                    ArrayList<AbstractCipherTextCard> tmp = new ArrayList<>(DeclareHelper.buffed);
                    for (AbstractCipherTextCard c : tmp) {
                        if (c instanceof C38) {
                            hasGo = true;
                        }
                    }
                    if (hasGo) {
                        C38.enabled = true;
                        AbstractDungeon.nextRoom.room = new MonsterRoomElite();
                    }
                }
            }
        }
    }
}
