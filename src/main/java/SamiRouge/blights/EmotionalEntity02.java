package SamiRouge.blights;

import SamiRouge.actions.BlightAboveCreatureAction;
import SamiRouge.dungeons.TheSami;
import SamiRouge.samiMod.ModConfig;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import java.util.ArrayList;
import java.util.Collections;

public class EmotionalEntity02 extends AbstractSamiBlight {
    public static final String ID = "samirg:EmotionalEntity02";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public static boolean deleteSelf = true;

    public EmotionalEntity02(){
        super(ID, NAME, (ModConfig.all_fans?DESC[1] : DESC[0])+DESC[2], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/EmotionalEntity02.png");
        this.counter = 2;
        initializeTips();
    }

    @Override
    public void atBattleStart() {
        if(deleteSelf)
            return;
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractPlayer m = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new BlightAboveCreatureAction(m,this));
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        integers.add(2);
        integers.add(3);
        Collections.shuffle(integers,AbstractDungeon.monsterRng.random);
        for(int i = 0;i<2;i++){
            int random = integers.get(i);
            if(random==0){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new ArtifactPower(m,1),1));
            }
            else if(random==1){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new RegenPower(m,2),2));
            }
            else if(random==2){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new MetallicizePower(m,4),4));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new PlatedArmorPower(m,8),8));
            }
        }
    }

    @Override
    public void onCreateEnemy(AbstractMonster m) {
        if(!AbstractDungeon.id.equals(TheSami.ID)&&!ModConfig.all_fans)
            return;
        AbstractDungeon.actionManager.addToBottom(new BlightAboveCreatureAction(m,this));
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        integers.add(2);
        integers.add(3);
        Collections.shuffle(integers,AbstractDungeon.monsterRng.random);
        for(int i = 0;i<2;i++){
            int random = integers.get(i);
            if(random==0){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new ArtifactPower(m,1),1));
            }
            else if(random==1){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new RegenerateMonsterPower(m,2),2));
            }
            else if(random==2){
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new MetallicizePower(m,4),4));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m,m,new PlatedArmorPower(m,8),8));
            }
        }
    }

    @Override
    public String getExclusionBlight() {
        return EmotionalEntity01.ID;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}


