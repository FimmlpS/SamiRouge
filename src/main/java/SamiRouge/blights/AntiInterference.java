package SamiRouge.blights;

import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;

public class AntiInterference extends AbstractSamiBlight{
    public static final String ID = "samirg:AntiInterference";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public static Random antiInterferenceRandom;

    public static void loadRandom(int counter){
        if(Settings.seed!=null)
            antiInterferenceRandom = new Random(Settings.seed,counter);
    }

    public static int saveRandom(){
        if(antiInterferenceRandom!=null)
            return antiInterferenceRandom.counter;
        return 0;
    }

    public static void deleteRandom(){
        antiInterferenceRandom = null;
    }

    public AntiInterference() {
        super(ID,NAME,DESC[0]+1+DESC[1],"maze.png",true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/AntiInterference.png");
        this.counter = 1;
        updateDescription();
    }

    @Override
    public void incrementUp() {
        ++increment;
        ++counter;
        updateDescription();
    }

    public void buyOne(){
        this.flash();
        ++counter;
        updateDescription();
    }

    public void useOne(){
        this.flash();
        this.counter--;
        if(counter<0)
            counter = 0;
        updateDescription();
    }

    @Override
    public void onBossDefeat() {
        this.flash();
        this.counter++;
        updateDescription();
    }

    @Override
    public void onVictory() {
        if(AbstractDungeon.getCurrRoom()==null)
            return;
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)
            return;
        if(antiInterferenceRandom==null){
            antiInterferenceRandom = new Random(Settings.seed);
        }
        boolean isElite = AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite;
        boolean add = antiInterferenceRandom.randomBoolean(isElite?0.3F: (ModConfig.anti_fall_glv*0.05F));
        if(add){
            this.flash();
            this.counter++;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = DESC[0] + this.counter + DESC[1];
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}
