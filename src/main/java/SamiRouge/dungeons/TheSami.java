package SamiRouge.dungeons;

import SamiRouge.helper.AltMonsterHelper;
import SamiRouge.monsters.Smkght;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.rooms.LostAndFoundRoom;
import SamiRouge.rooms.THEventRoom;
import SamiRouge.rooms.TreeHoleOuterRoom;
import TreeHole.mod.TreeHoleHelper;
import TreeHole.patches.TreeHolePatch;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.gikk.twirk.types.mode.Mode;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

public class TheSami extends AbstractDungeon {
    private static final Logger logger = LogManager.getLogger(TheSami.class.getName());
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    public static final String ID = "samirg:TheSami";
    public static final ArrayList<String> specialElites = new ArrayList<>();

    public static String lastNormalElite = "";

    public void initializeSamiWhenJR(){
        generateMonsters();
        initializeBoss();
        setBoss(AbstractDungeon.bossList.get(0));
        initializeEventList();
        initializeEventImg();
        initializeShrineList();
    }

    @SpireOverride
    protected void setBoss(String key){
        SpireSuper.call(key);
    }

    public TheSami(AbstractPlayer p, ArrayList<String> theList){
        super(TreeHoleHelper.getCurrentType()==4?TEXT[2]:TEXT[0], ID, p, theList);
        if(TreeHoleHelper.getCurrentType()==3){
            TreeHolePatch.bossSpawned.add(bossKey);
        }
        if (scene != null) {
            scene.dispose();
        }

        scene = new TheSamiScene();
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");
        this.initializeLevelSpecificChances();
        mapRng = new Random(Settings.seed + (long)(AbstractDungeon.actNum * 300 + (long)(AbstractDungeon.floorNum * 400)));
        this.generateSpecialMap();
        if(TreeHoleHelper.getCurrentType()!=0 && TreeHoleHelper.getCurrentType()!=4)
            setEmeraldElite();
        CardCrawlGame.music.changeBGM(id);
        java.util.logging.Logger.getGlobal().info(AbstractDungeon.specialOneTimeEventList.toString());
    }

    public TheSami(AbstractPlayer p, SaveFile saveFile){
        super(TreeHoleHelper.getCurrentType()==4?TEXT[2]:TEXT[0], p, saveFile);
        CardCrawlGame.dungeon = this;
        if (scene != null) {
            scene.dispose();
        }

        scene = new TheSamiScene();
        fadeColor = Color.valueOf("140a1eff");
        sourceFadeColor = Color.valueOf("140a1eff");
        this.initializeLevelSpecificChances();
        miscRng = new Random(Settings.seed + (long)saveFile.floor_num);
        CardCrawlGame.music.changeBGM(id);
        mapRng = new Random(Settings.seed + (long)(saveFile.act_num * 300 + (long)(AbstractDungeon.floorNum * 400)));
        this.generateSpecialMap();
        if(TreeHoleHelper.getCurrentType()!=0 && TreeHoleHelper.getCurrentType()!=4)
            setEmeraldElite();
        firstRoomChosen = true;
        this.populatePathTaken(saveFile);
        java.util.logging.Logger.getGlobal().info(AbstractDungeon.specialOneTimeEventList.toString());
    }

    public static boolean isTHEventRoom(){
        if(TreeHoleHelper.getCurrentType() == 0||TreeHoleHelper.getCurrentType()==1||TreeHoleHelper.getCurrentType()==2||TreeHoleHelper.getCurrentType()==3){

            return true;
        }
        if(TreeHoleHelper.getCurrentType() == 4){
            return nextRoom.x == 3 && (nextRoom.y == 3||nextRoom.y == 4);
        }
        return false;
    }

    private void generateSpecialMap() {
        switch (TreeHoleHelper.getCurrentType()) {
            case 0:
                generateUncombatMap();
                break;
            case 1:
                generateCombatMap();
                break;
            case 2:
                generateEliteMap();
                break;
            case 3:
                generateBossMap();
                break;
            case 4:
                generateFinalMap();
                break;
            default:
                generateBossMap();
                break;
        }
    }

    private void generateUncombatMap(){
        long startTime = System.currentTimeMillis();
        map = new ArrayList();
        MapRoomNode e0 = new MapRoomNode(3, 0);
        e0.room = new THEventRoom();
        MapRoomNode e1 = new MapRoomNode(2, 1);
        e1.room = new THEventRoom();
        MapRoomNode s1 = new MapRoomNode(4, 1);
        s1.room = new ShopRoom();
        MapRoomNode e2 = new MapRoomNode(3,2);
        e2.room = new THEventRoom();
        MapRoomNode e3 = new MapRoomNode(2,3);
        e3.room = new THEventRoom();
        MapRoomNode s3 = new MapRoomNode(4,3);
        s3.room = new ShopRoom();
        MapRoomNode e4 = new MapRoomNode(3,4);
        e4.room = new THEventRoom();
        MapRoomNode rest = new MapRoomNode(3,5);
        rest.room = new RestRoom();
        MapRoomNode lost = new MapRoomNode(3,6);
        lost.room = new LostAndFoundRoom();
        MapRoomNode endNode = new MapRoomNode(3, 7);
        endNode.room = new TreeHoleOuterRoom();

        connectNode(e0,e1);
        connectNode(e0,s1);
        connectNode(e1,e2);
        connectNode(s1,e2);
        connectNode(e2,e3);
        connectNode(e2,s3);
        connectNode(e3,e4);
        connectNode(s3,e4);
        connectNode(e4,rest);
        connectNode(rest,lost);
        connectNode(lost,endNode);

        map.add(createRow(0,e0));
        map.add(createRow(1,e1,s1));
        map.add(createRow(2,e2));
        map.add(createRow(3,e3,s3));
        map.add(createRow(4,e4));
        map.add(createRow(5,rest));
        map.add(createRow(6,lost));
        map.add(createRow(7,endNode));

        logger.info("===生成非战斗地图如下===");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
    }

    private void generateCombatMap(){
        long startTime = System.currentTimeMillis();
        map = new ArrayList();

        MapRoomNode r0 = new MapRoomNode(3, 0);
        r0.room = new RestRoom();
        MapRoomNode m1 = new MapRoomNode(3, 1);
        m1.room = new MonsterRoom();
        MapRoomNode e2 = new MapRoomNode(3,2);
        e2.room = new MonsterRoom();
        MapRoomNode r3 = new MapRoomNode(3,3);
        r3.room = new MonsterRoomElite();
        MapRoomNode m4 = new MapRoomNode(3,4);
        m4.room = new THEventRoom();
        MapRoomNode e5 = new MapRoomNode(3,5);
        e5.room = new TreasureRoom();
        MapRoomNode lost = new MapRoomNode(3,6);
        lost.room = new LostAndFoundRoom();
        MapRoomNode endNode2 = new MapRoomNode(3, 7);
        endNode2.room = new TreeHoleOuterRoom();

        connectNode(r0,m1);
        connectNode(m1,e2);
        connectNode(e2,r3);
        connectNode(r3,m4);
        connectNode(m4,e5);
        connectNode(e5,lost);
        connectNode(lost,endNode2);

        map.add(createRow(0,r0));
        map.add(createRow(1,m1));
        map.add(createRow(2,e2));
        map.add(createRow(3,r3));
        map.add(createRow(4,m4));
        map.add(createRow(5,e5));
        map.add(createRow(6,lost));
        map.add(createRow(7,endNode2));

        logger.info("===生成战斗地图如下===");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
    }

    private void generateEliteMap(){
        long startTime = System.currentTimeMillis();
        map = new ArrayList();

        MapRoomNode r0 = new MapRoomNode(3, 0);
        r0.room = new RestRoom();
        MapRoomNode m1 = new MapRoomNode(3, 1);
        m1.room = new MonsterRoom();
        MapRoomNode e2 = new MapRoomNode(3,2);
        e2.room = new MonsterRoomElite();
        MapRoomNode r3 = new MapRoomNode(3,3);
        r3.room = new THEventRoom();
        MapRoomNode m4 = new MapRoomNode(3,4);
        m4.room = new THEventRoom();
        MapRoomNode e5 = new MapRoomNode(3,5);
        MonsterRoomElite selite = new MonsterRoomElite();
        SamiTreeHolePatch.SpireEliteRoomPatch.specialElite.set(selite,true);
        e5.room = selite;
        MapRoomNode r6 = new MapRoomNode(3,6);
        r6.room = new ShopRoom();
        MapRoomNode lost = new MapRoomNode(3,7);
        lost.room = new LostAndFoundRoom();
        MapRoomNode endNode1 = new MapRoomNode(1, 4);
        endNode1.room = new TreeHoleOuterRoom();
        MapRoomNode endNode2 = new MapRoomNode(3, 8);
        endNode2.room = new TreeHoleOuterRoom();

        connectNode(r0,m1);
        connectNode(m1,e2);
        connectNode(e2,r3);
        connectNode(r3,m4);
        connectNode(r3,endNode1);
        connectNode(m4,e5);
        connectNode(e5,r6);
        connectNode(r6,lost);
        connectNode(lost,endNode2);

        map.add(createRow(0,r0));
        map.add(createRow(1,m1));
        map.add(createRow(2,e2));
        map.add(createRow(3,r3));
        map.add(createRow(4,endNode1,m4));
        map.add(createRow(5,e5));
        map.add(createRow(6,r6));
        map.add(createRow(7,lost));
        map.add(createRow(8,endNode2));

        logger.info("===生成精英地图如下===");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
    }

    private void generateBossMap(){
        long startTime = System.currentTimeMillis();
        map = new ArrayList();
        MapRoomNode restNode = new MapRoomNode(3, 0);
        restNode.room = new RestRoom();
        MapRoomNode elite = new MapRoomNode(2, 1);
        elite.room = new MonsterRoomElite();
        MapRoomNode monster = new MapRoomNode(4, 1);
        monster.room = new MonsterRoom();
        MapRoomNode event = new MapRoomNode(3,2);
        event.room = new TreasureRoom();
        MapRoomNode shop = new MapRoomNode(2,3);
        shop.room = new ShopRoom();
        MapRoomNode chest = new MapRoomNode(4,3);
        chest.room = new LostAndFoundRoom();
        MapRoomNode event2 = new MapRoomNode(3,4);
        event2.room = new THEventRoom();
        MapRoomNode boss = new MapRoomNode(3,6);
        boss.room = new MonsterRoomBoss();
        MapRoomNode endNode = new MapRoomNode(3, 7);
        TreeHoleOuterRoom t = new TreeHoleOuterRoom();
        t.renderThis = false;
        endNode.room = t;

        connectNode(restNode,elite);
        connectNode(restNode,monster);
        connectNode(elite,event);
        connectNode(monster,event);
        connectNode(event,shop);
        connectNode(event,chest);
        connectNode(shop,event2);
        connectNode(chest,event2);
        connectNode(event2,boss);

        map.add(createRow(0,restNode));
        map.add(createRow(1,elite,monster));
        map.add(createRow(2,event));
        map.add(createRow(3,shop,chest));
        map.add(createRow(4,event2));
        map.add(createRow(5,new ArrayList<>()));
        map.add(createRow(6,boss));
        map.add(createRow(7,endNode));

        logger.info("===生成BOSS地图如下===");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
    }

    private void generateFinalMap(){
        long startTime = System.currentTimeMillis();
        map = new ArrayList();
        MapRoomNode treasure1 = new MapRoomNode(3, 0);
        treasure1.room = new TreasureRoom();
        MapRoomNode treasure2 = new MapRoomNode(3, 1);
        treasure2.room = new TreasureRoom();
        MapRoomNode treasure3 = new MapRoomNode(3,2);
        treasure3.room = new TreasureRoom();
        MapRoomNode elite = new MapRoomNode(3,3);
        elite.room = new THEventRoom();
        MapRoomNode event2 = new MapRoomNode(3,4);
        event2.room = new THEventRoom();
        MapRoomNode boss = new MapRoomNode(3,5);
        boss.room = new MonsterRoomBoss();
        MapRoomNode endNode = new MapRoomNode(3, 6);
        endNode.room = new TrueVictoryRoom();

        connectNode(treasure1,treasure2);
        connectNode(treasure2,treasure3);
        connectNode(treasure3,elite);
        connectNode(elite,event2);
        connectNode(event2,boss);

        map.add(createRow(0,treasure1));
        map.add(createRow(1,treasure2));
        map.add(createRow(2,treasure3));
        map.add(createRow(3,elite));
        map.add(createRow(4,event2));
        map.add(createRow(5,boss));
        map.add(createRow(6,endNode));

        logger.info("===生成BOSS地图如下===");
        logger.info(MapGenerator.toString(map, true));
        logger.info("Game Seed: " + Settings.seed);
        logger.info("Map generation time: " + (System.currentTimeMillis() - startTime) + "ms");
        firstRoomChosen = false;
        fadeIn();
    }

    public static void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    private ArrayList<MapRoomNode> createRow(int row,MapRoomNode node){
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        tmp.add(node);
        return createRow(row,tmp);
    }

    private ArrayList<MapRoomNode> createRow(int row,MapRoomNode node1,MapRoomNode node2){
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        tmp.add(node1);
        tmp.add(node2);
        return createRow(row,tmp);
    }

    private ArrayList<MapRoomNode> createRow(int row,ArrayList<MapRoomNode> nodes){
        ArrayList<MapRoomNode> tmp = new ArrayList<>();
        for(int i =0;i<7;i++) {
            tmp.add(new MapRoomNode(i,row));
        }
        for(int i =0;i<nodes.size();i++){
            tmp.set(nodes.get(i).x,nodes.get(i));
        }
        return tmp;
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 0;
        mediumChestChance = 100;
        largeChestChance = 0;
        commonRelicChance = 30;
        uncommonRelicChance = 40;
        rareRelicChance = 30;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25F;
        } else {
            cardUpgradedChance = 0.5F;
        }
    }

    @Override
    protected void generateMonsters() {
        //初始化
        monsterRng.random();
        this.generateWeakEnemies(0);
        this.generateStrongEnemies(10);
        this.generateElites(10);
    }

    @Override
    protected void generateWeakEnemies(int i) {
        ArrayList<MonsterInfo> monsters = new ArrayList();
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, i, false);
    }

    @Override
    protected void generateStrongEnemies(int i) {
        String lastID = null;
        if(TreeHolePatch.foreSaveFile !=null)
            lastID = TreeHolePatch.foreSaveFile.level_name;
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        if(actNum==1){
            if(lastID != null && !lastID.isEmpty())
                AbstractDungeon.id = lastID;
            else
                AbstractDungeon.id = Exordium.ID;
            monsters.add(new MonsterInfo("Blue Slaver",2F));
            monsters.add(new MonsterInfo("Gremlin Gang",2F));
            monsters.add(new MonsterInfo("Looter",2F));
            monsters.add(new MonsterInfo("Large Slime",2F));
            monsters.add(new MonsterInfo("Lots of Slimes",2F));
            monsters.add(new MonsterInfo("Exordium Thugs",2F));
            monsters.add(new MonsterInfo("Exordium Wildlife",2F));
            monsters.add(new MonsterInfo("Red Slaver",2F));
            monsters.add(new MonsterInfo("3 Louse",2F));
            monsters.add(new MonsterInfo("2 Fungi Beasts",2F));
            monsters.add(new MonsterInfo(AltMonsterHelper.ONE_SLAVER,3F));
            monsters.add(new MonsterInfo(AltMonsterHelper.ONE_BEAR,3F));
            monsters.add(new MonsterInfo(AltMonsterHelper.ONE_DAGGERS,3F));
        }
        else if(actNum==2){
            if(lastID != null && !lastID.isEmpty())
                AbstractDungeon.id = lastID;
            else
                AbstractDungeon.id = TheCity.ID;
            monsters.add(new MonsterInfo("Chosen and Byrds",2F));
            monsters.add(new MonsterInfo("Sentry and Sphere",2F));
            monsters.add(new MonsterInfo("Snake Plant",2F));
            monsters.add(new MonsterInfo("Snecko",2F));
            monsters.add(new MonsterInfo("Centurion and Healer",2F));
            monsters.add(new MonsterInfo("Cultist and Chosen",2F));
            monsters.add(new MonsterInfo("3 Cultists",2F));
            monsters.add(new MonsterInfo("Shelled Parasite and Fungi",2F));
            monsters.add(new MonsterInfo(AltMonsterHelper.TWO_SOLDIER,3F));
            monsters.add(new MonsterInfo(AltMonsterHelper.TWO_S_AND_S,3F));
            monsters.add(new MonsterInfo(AltMonsterHelper.TWO_STRONG_BUG,3F));
        }
        else {
            if(lastID != null && !lastID.isEmpty())
                AbstractDungeon.id = lastID;
            else
                AbstractDungeon.id = TheBeyond.ID;
            monsters.add(new MonsterInfo("Spire Growth",2F));
            monsters.add(new MonsterInfo("Transient",2F));
            monsters.add(new MonsterInfo("4 Shapes",2F));
            monsters.add(new MonsterInfo("Maw",2F));
            monsters.add(new MonsterInfo("Sphere and 2 Shapes",2F));
            monsters.add(new MonsterInfo("Jaw Worm Horde",2F));
            monsters.add(new MonsterInfo("3 Darklings",2F));
            monsters.add(new MonsterInfo("Writhing Mass",2F));
            monsters.add(new MonsterInfo("Snecko and Mystics",3F));
            monsters.add(new MonsterInfo(AltMonsterHelper.THREE_B_C_T_C_B,3F));
        }

        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, i, false);
        AbstractDungeon.id = TheSami.ID;
    }

    @Override
    protected void generateElites(int i) {
        String lastID = null;
        if(TreeHolePatch.foreSaveFile !=null)
            lastID = TreeHolePatch.foreSaveFile.level_name;
        ArrayList<MonsterInfo> elitemonsters = new ArrayList<>();
        if(actNum==1){
            if(lastID != null && !lastID.isEmpty())
                AbstractDungeon.id = lastID;
            else
                AbstractDungeon.id = Exordium.ID;
            elitemonsters.add(new MonsterInfo("Shell Parasite",3F));
            elitemonsters.add(new MonsterInfo("samirg:TwoDarklings",3F));
            elitemonsters.add(new MonsterInfo("samirg:ExordiumElites",3F));
            elitemonsters.add(new MonsterInfo("samirg:FourGremlinWizard",3F));
            elitemonsters.add(new MonsterInfo("samirg:TwoChosen",3F));
        }
        else if(actNum==2){
            if(lastID != null && !lastID.isEmpty())
                AbstractDungeon.id = lastID;
            else
                AbstractDungeon.id = TheCity.ID;
            elitemonsters.add(new MonsterInfo("samirg:ThreeGremlinLeader",3F));
            elitemonsters.add(new MonsterInfo("samirg:SmallBigJawWorm",3F));
            elitemonsters.add(new MonsterInfo("samirg:TheCityElites",3F));
            elitemonsters.add(new MonsterInfo("samirg:TheCityStrong",3F));
        }
        else {
            if(lastID != null && !lastID.isEmpty())
                AbstractDungeon.id = lastID;
            else
                AbstractDungeon.id = TheBeyond.ID;
            elitemonsters.add(new MonsterInfo("samirg:ThreeWarSoldier",2F));
            //elitemonsters.add(new MonsterInfo("samirg:TheBeyondElites",3F));
            elitemonsters.add(new MonsterInfo("samirg:CurseNemesis",2F));
            elitemonsters.add(new MonsterInfo("samirg:DoubleTimeEater",3F));
            elitemonsters.add(new MonsterInfo("samirg:HeroNoName",4F));
            elitemonsters.add(new MonsterInfo("samirg:SpecialSlime",4F));
            if(!BaseMod.hasModID("spireTogether:"))
                elitemonsters.add(new MonsterInfo(AltMonsterHelper.BANDITS_AND_SLAVERS,5F));
        }
        if(TreeHoleHelper.getCurrentType()==4){
            elitemonsters.add(new MonsterInfo("Shield and Spear",3F));
//            if(BaseMod.hasModID("encountermod"))
//                elitemonsters.add(new MonsterInfo("samirg:DoubleTimeEater",3F));
        }

        MonsterInfo.normalizeWeights(elitemonsters);
        this.populateMonsterList(elitemonsters, i, true);
        //去除重复战斗
        while (eliteMonsterList.size()>1 && eliteMonsterList.get(0).equals(lastNormalElite)){
            eliteMonsterList.remove(0);
        }
        AbstractDungeon.id = TheSami.ID;
    }

    @Override
    protected ArrayList<String> generateExclusions() {
        return new ArrayList<>();
    }

    @Override
    protected void initializeBoss() {
        bossList = new ArrayList<>();
        if(TreeHoleHelper.getCurrentType()!=4){
            for(int i =0;i<3;i++){
                bossList.add(SamiTreeHolePatch.currentSamiBoss);
            }
        }
        else {
            bossList.add(Smkght.BOSS_ID);
            bossList.add(Smkght.BOSS_ID);
            bossList.add(Smkght.BOSS_ID);
        }
    }

    private boolean containCard(String id,ArrayList<String> IDS){
        for(String ID : IDS){
            if(ID.equals(id))
                return true;
        }
        return false;
    }

    private static boolean containBoss(String id){
        for(String st: TreeHolePatch.bossSpawned){
            if(st.equals(id))
                return true;
        }
        return false;
    }

    @Override
    protected void initializeEventList() {
        boolean noMoreUsed = true;
        if(noMoreUsed)
            return;

        eventList.add("Big Fish");
        eventList.add("The Cleric");
        eventList.add("Dead Adventurer");
        eventList.add("Golden Idol");
        eventList.add("Golden Wing");
        eventList.add("World of Goop");
        eventList.add("Liars Game");
        eventList.add("Living Wall");
        eventList.add("Mushrooms");
        eventList.add("Scrap Ooze");
        eventList.add("Shining Light");
        eventList.add("Addict");
        eventList.add("Back to Basics");
        eventList.add("Beggar");
        eventList.add("Colosseum");
        eventList.add("Cursed Tome");
        eventList.add("Drug Dealer");
        eventList.add("Forgotten Altar");
        eventList.add("Ghosts");
        eventList.add("Masked Bandits");
        eventList.add("Nest");
        eventList.add("The Library");
        eventList.add("The Mausoleum");
        eventList.add("Vampires");
        eventList.add("Falling");
        eventList.add("MindBloom");
        eventList.add("The Moai Head");
        eventList.add("Mysterious Sphere");
        eventList.add("SensoryStone");
        eventList.add("Tomb of Lord Red Mask");
        eventList.add("Winding Halls");
    }

    @Override
    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }

        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    @Override
    protected void initializeShrineList() {
        shrineList.add("Match and Keep!");
        shrineList.add("Golden Shrine");
        shrineList.add("Transmorgrifier");
        shrineList.add("Purifier");
        shrineList.add("Upgrade Shrine");
        shrineList.add("Wheel of Change");
    }



    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
        //特殊敌人
        specialElites.add("samirg:Flking");
        specialElites.add("samirg:Smgia");
        specialElites.add("samirg:Smgrd");
        specialElites.add("samirg:Smsha");
    }
}

