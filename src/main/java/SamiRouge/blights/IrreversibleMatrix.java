package SamiRouge.blights;

import SamiRouge.actions.*;
import SamiRouge.samiMod.ModConfig;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class IrreversibleMatrix extends AbstractSamiBlight {
    public static final String ID = "samirg:IrreversibleMatrix";
    private static final BlightStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public IrreversibleMatrix(){
        super(ID, NAME, DESC[0], "maze.png", true);
        this.img = ImageMaster.loadImage("SamiRougeResources/img/blights/IrreversibleMatrix.png");
        this.counter = 1;
        updateDescription();
    }

    @Override
    public void onCreateEnemy(AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new BlightAboveCreatureAction(m,this));
        AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpByBLAction(m,getBlvHP(),true));
        AbstractDungeon.actionManager.addToBottom(new ChangeInvincibleAction(m,getBlvHP()));
    }

    @Override
    public void atBattleStart() {
        if(this.counter>=6){
            //2025/12/18更新：幕数>=时不再生成且仅限不低于6时召唤
            if(AbstractDungeon.actNum>=4)
                return;
            this.flash();
            int enemy = 0;
            if(!BaseMod.hasModID("spireTogether:")){
                enemy = AbstractDungeon.monsterRng.random(2);
            }
            else {
                enemy = 1 + AbstractDungeon.monsterRng.random(1);
            }
            if(enemy==0){
                AbstractDungeon.actionManager.addToBottom(new SummonSmdrnAction((float) Settings.WIDTH * 0.75F + 0F * Settings.xScale,AbstractDungeon.floorY + 380F * Settings.yScale));
            }
            else if(enemy==1){
                float targetX = (float) Settings.WIDTH * 0.4F + 0F * Settings.xScale;
                if(!AbstractDungeon.getMonsters().monsters.isEmpty()){
                    AbstractMonster first = AbstractDungeon.getMonsters().monsters.get(0);
                    if(first.drawX > 0.5F * Settings.WIDTH)
                        targetX = first.drawX - first.hb_w/2F - 100F * Settings.scale;
                    else if(AbstractDungeon.getMonsters().monsters.size()>=2){
                        AbstractMonster second = AbstractDungeon.getMonsters().monsters.get(1);
                        if(second.drawX > 0.5F * Settings.WIDTH)
                            targetX = second.drawX - second.hb_w/2F - 100F * Settings.scale;
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new SummonSmsbrAction(targetX,AbstractDungeon.floorY - 20F * Settings.yScale));
            }
            else if(enemy==2){
                float targetX = (float) Settings.WIDTH * 0.45F + 0F * Settings.xScale;
                if(!AbstractDungeon.getMonsters().monsters.isEmpty()){
                    AbstractMonster first = AbstractDungeon.getMonsters().monsters.get(0);
                    if(first.drawX > 0.5F * Settings.WIDTH)
                        targetX = first.drawX - first.hb_w/2F - 100F * Settings.scale;
                    else if(AbstractDungeon.getMonsters().monsters.size()>=2){
                        AbstractMonster second = AbstractDungeon.getMonsters().monsters.get(1);
                        if(second.drawX > 0.5F * Settings.WIDTH)
                            targetX = second.drawX - second.hb_w/2F - 100F * Settings.scale;
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new SummonSmwizAction(targetX,AbstractDungeon.floorY - 20F * Settings.yScale));
            }
        }
    }

    @Override
    public void incrementUp() {
        ++increment;
        ++counter;
        updateDescription();
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if(this.counter<0){
            this.counter = 0;
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESC[0] + (int)(getBlvHP()*100) + DESC[1] + (int)(getBlvDMG()*100) + DESC[2];
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public float effectFloat() {
        return 1F + getBlvDMG();
    }

    public float getBlvHP(){
        return (0.05F* ModConfig.ir_blv)*counter;
    }

    public float getBlvDMG(){
        return (0.05F* ModConfig.ir_blv_attack)*counter;
    }

    static {
        blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTION;
    }
}
