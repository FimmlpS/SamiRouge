package SamiRouge.relics;

import SamiRouge.actions.SummonSmdrnAction;
import SamiRouge.effects.ObtainRelicEffect;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class RouteMap extends CustomRelic implements CustomSavable<ArrayList<String>> {

    public static final String ID = "samirg:RouteMap";
    private static final String IMG = "SamiRougeResources/img/relics/RouteMap.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/RouteMap_O.png";
    ArrayList<String> monsterKilled = new ArrayList<>();

    public RouteMap(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public ArrayList<String> onSave() {
        return monsterKilled;
    }

    @Override
    public void onLoad(ArrayList<String> strings) {
        this.monsterKilled = strings;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if(!containMonster(m.id)){
            this.flash();
            monsterKilled.add(m.id);
            this.counter++;
            if(counter>=10&&!AbstractDungeon.player.hasRelic(LimitlessGift.ID)){
                AbstractDungeon.effectsQueue.add(new ObtainRelicEffect(new LimitlessGift()));
            }
        }
    }

    @Override
    public void atBattleStart() {
        //2025/12/18更新：幕数>=4时不再生成
        if(AbstractDungeon.actNum>=4)
            return;
        this.flash();
        addToBot(new SummonSmdrnAction((float) Settings.WIDTH * 0.5F + -100F * Settings.xScale,AbstractDungeon.floorY + 380F * Settings.yScale));
    }

    private boolean containMonster(String id){
        for(String s:monsterKilled){
            if(s.equals(id))
                return true;
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RouteMap();
    }
}




