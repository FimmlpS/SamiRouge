package SamiRouge.save;

import SamiRouge.blights.AntiInterference;
import SamiRouge.dungeons.TheSami;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.patches.EventPatch;
import SamiRouge.patches.MapPatch;
import SamiRouge.patches.SamiTreeHolePatch;
import TreeHole.save.TreeHoleSave;

import java.util.ArrayList;

public class SamiTreeHoleSave extends TreeHoleSave {

    public int treeHoleEntered;

    public int bossKilled;

    public boolean whatBeginsFollowsWhatEnds;
    public boolean toTalkWithMountains;
    public boolean winterFall;
    public boolean longIntoAnAbyss;
    public boolean withHerTalk;

    public boolean enterDoubleKing;
    public boolean enteredDoubleKing;

    public boolean getDimensionalFluidity;

    public int monsterRngCountExtra;

    public ArrayList<String> specialElites;
    public ArrayList<String> specialEvents;

    public String lastNormalElite;
    public String currentSamiBoss;

    public int antiInterferenceCounter;

    public ArrayList<Integer> spawnX;
    public ArrayList<Integer> spawnY;
    public ArrayList<Boolean> walked;
    public ArrayList<Integer> spawnDistance;

    //密文板
    public ArrayList<String> layoutsAndReasons;
    public ArrayList<Integer> rhetoric;
    public ArrayList<String> buffed;
    public ArrayList<Integer> remainX;
    public ArrayList<Boolean> together;

    @Override
    public void onSave() {
        treeHoleEntered = SamiTreeHolePatch.treeHoleEntered;
        bossKilled = SamiTreeHolePatch.bossKilled;
        whatBeginsFollowsWhatEnds = SamiTreeHolePatch.whatBeginsFollowsWhatEnds;
        toTalkWithMountains = SamiTreeHolePatch.toTalkWithMountains;
        winterFall = SamiTreeHolePatch.winterFall;
        longIntoAnAbyss = SamiTreeHolePatch.longIntoAnAbyss;
        winterFall = SamiTreeHolePatch.withHerTalk;
        enterDoubleKing = SamiTreeHolePatch.enterDoubleKing;
        enteredDoubleKing = SamiTreeHolePatch.enteredDoubleKing;
        getDimensionalFluidity = SamiTreeHolePatch.getDimensionalFluidity;
        monsterRngCountExtra = SamiTreeHolePatch.monsterRngCounterExtra;
        currentSamiBoss = SamiTreeHolePatch.currentSamiBoss;
        lastNormalElite = TheSami.lastNormalElite;
        specialEvents = new ArrayList<>(EventPatch.specialEvents);
        specialElites = new ArrayList<>(SamiTreeHolePatch.specialElites);
        antiInterferenceCounter = AntiInterference.saveRandom();
        if(MapPatch.spawnX != null) {
            spawnX = new ArrayList<>(MapPatch.spawnX);
        }
        if(MapPatch.spawnY != null) {
            spawnY = new ArrayList<>(MapPatch.spawnY);
        }
        if(MapPatch.walked != null) {
            walked = new ArrayList<>(MapPatch.walked);
        }
        if(MapPatch.spawnDistance != null) {
            spawnDistance = new ArrayList<>(MapPatch.spawnDistance);
        }
        DeclareHelper.onSave(this);
    }

    @Override
    public void onLoad() {
        SamiTreeHolePatch.treeHoleEntered = treeHoleEntered;
        SamiTreeHolePatch.bossKilled = bossKilled;
        SamiTreeHolePatch.toTalkWithMountains = toTalkWithMountains;
        SamiTreeHolePatch.winterFall = winterFall;
        SamiTreeHolePatch.whatBeginsFollowsWhatEnds = whatBeginsFollowsWhatEnds;
        SamiTreeHolePatch.longIntoAnAbyss = longIntoAnAbyss;
        SamiTreeHolePatch.withHerTalk = withHerTalk;
        SamiTreeHolePatch.enterDoubleKing = enterDoubleKing;
        SamiTreeHolePatch.enteredDoubleKing = enteredDoubleKing;
        SamiTreeHolePatch.getDimensionalFluidity = getDimensionalFluidity;
        SamiTreeHolePatch.monsterRngCounterExtra = monsterRngCountExtra;
        SamiTreeHolePatch.currentSamiBoss = currentSamiBoss;
        TheSami.lastNormalElite = lastNormalElite;
        EventPatch.specialEvents = new ArrayList<>();
        if(specialEvents!=null)
            EventPatch.specialEvents.addAll(specialEvents);
        SamiTreeHolePatch.specialElites = new ArrayList<>();
        if(specialElites!=null)
            SamiTreeHolePatch.specialElites.addAll(specialElites);
        AntiInterference.loadRandom(antiInterferenceCounter);
        if(spawnX!=null){
            MapPatch.spawnX = new ArrayList<>(spawnX);
        }
        if(spawnY!=null){
            MapPatch.spawnY = new ArrayList<>(spawnY);
        }
        if(walked!=null){
            MapPatch.walked = new ArrayList<>(walked);
        }
        if(spawnDistance!=null){
            MapPatch.spawnDistance = new ArrayList<>(spawnDistance);
        }
        //MapPatch.setSpawn();
        DeclareHelper.onLoad(this);
    }

    @Override
    public void onDelete() {
        SamiTreeHolePatch.enterDoubleKing = false;
        SamiTreeHolePatch.enteredDoubleKing = false;
        SamiTreeHolePatch.getDimensionalFluidity = false;
        SamiTreeHolePatch.monsterRngCounterExtra = 0;
        SamiTreeHolePatch.currentSamiBoss = "";
        TheSami.lastNormalElite = "";
        EventPatch.resetEvents();
        SamiTreeHolePatch.specialElites = new ArrayList<>();
        AntiInterference.deleteRandom();
        MapPatch.resetSpawn();
        DeclareHelper.onDelete();
    }

    @Override
    public void onReset() {
        SamiTreeHolePatch.treeHoleEntered = 0;
        SamiTreeHolePatch.bossKilled = 0;
        SamiTreeHolePatch.toTalkWithMountains = false;
        SamiTreeHolePatch.winterFall = false;
        SamiTreeHolePatch.whatBeginsFollowsWhatEnds = false;
        SamiTreeHolePatch.longIntoAnAbyss = false;
        SamiTreeHolePatch.withHerTalk = false;
    }
}
