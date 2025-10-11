package SamiRouge.monsters;

import SamiRouge.actions.DelayActionAction;
import SamiRouge.actions.WoodCrackAction;
import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.cards.curse.Coldness_SamiRouge;
import SamiRouge.cards.curse.Freeze_SamiRouge;
import SamiRouge.cards.curse.Hypothermia_SamiRouge;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.powers.SelfCollapsePower;
import SamiRouge.samiMod.SamiRougeHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.ArrayList;

public class WoodCrack extends AbstractMonster {
    public static final String ID = "samirg:WoodCrack";
    public static final String ICON = "SamiRougeResources/img/monsters_SamiRouge/Smlead/WoodCrack_Icon.png";
    public static final String ICON_O = "SamiRougeResources/img/monsters_SamiRouge/Smlead/WoodCrack_Icon_O.png";
    private static final MonsterStrings monsterStrings;

    private boolean firstTurn = true;
    private int baseDamage = 18;
    private int baseBlock = 18;
    private int addDamage = 6;
    private int xrAmount = 1;
    private int zqAmount = 2;
    private float wait01 = 1.6F;
    public boolean additionalBlock = false;
    private boolean upgradeCard =false;
    boolean more;

    private void addDamage(){
        this.baseDamage += addDamage;
    }

    public void talk(int times){
        if(times<monsterStrings.DIALOG.length){
            addToTop(new TalkAction(this,monsterStrings.DIALOG[times],0.5F,2F));
        }
    }

    private void setFastMode(){
        if(Settings.FAST_MODE){
            state.setTimeScale(2);
            wait01 = 0.8F;
        }
        else{
            state.setTimeScale(1);
            wait01 = 1.6F;
        }
    }

    public WoodCrack(float x,float y){
        super(monsterStrings.NAME, ID,600,0F,0F,420F,400F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(700);
        }
        else {
            this.setHp(600);
        }
        if(AbstractDungeon.ascensionLevel>=19){
            xrAmount = 2;
            zqAmount = 4;
            upgradeCard = true;
            more = true;
        }
        else{
            xrAmount = 1;
            zqAmount = 3;
            upgradeCard = false;
            more = false;
        }

        if(AbstractDungeon.ascensionLevel>=4){
            baseDamage = 21;
            addDamage = 7;
            baseBlock = 21;
            additionalBlock = true;
        }
        else{
            baseDamage = 18;
            addDamage = 6;
            baseBlock = 18;
            additionalBlock =false;
        }
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!=null){
            baseBlock += 2*ir.counter;
            baseDamage += ir.counter;
        }
        firstTurn = true;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smlead/enemy_2055_smlead.atlas","SamiRougeResources/img/monsters_SamiRouge/Smlead/enemy_2055_smlead.json",1.5F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.BOSS;
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
        for(int i =0;i<3;i++){
            addDamage();
            this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        SamiRougeHelper.BOSS_KEY = "WoodCrack";
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        SamiRougeHelper.BOSS_KEY = "";
        addToBot(new DelayActionAction(new WoodCrackAction(upgradeCard,this)));
    }

    @Override
    public void takeTurn() {
        setFastMode();
        if(firstTurn){
            firstTurn =false;
            if(AbstractDungeon.player.name == "Typhon")
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, monsterStrings.DIALOG[4], 0.5F, 2.0F));
            else{
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, monsterStrings.DIALOG[0], 0.5F, 2.0F));
            }
        }

        switch (this.nextMove){
            case 1:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                break;
            }
            case 2:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player,this.xrAmount,true),this.xrAmount));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new VulnerablePower(AbstractDungeon.player,this.xrAmount,true),this.xrAmount));
                break;
            }
            case 3:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,this.zqAmount),this.zqAmount));
                addToBot(new ApplyPowerAction(this,this,new MetallicizePower(this,this.zqAmount),this.zqAmount));
                break;
            }
            case 4:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot(new GainBlockAction(this,this.baseBlock));
                break;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK":{
                this.state.setAnimation(0,"Attack",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if((firstTurn||i<40)&&!lastTwoMoves((byte) 1)&&this.currentHealth>=this.maxHealth*0.8F){
            this.setMove((byte) 1,Intent.ATTACK,this.damage.get(0).base,1,false);
        }
        else if(i<70&&!lastTwoMoves((byte) 2)&&this.currentHealth>=this.maxHealth*0.5F){
            this.setMove((byte) 2,Intent.ATTACK_DEBUFF,this.damage.get(1).base,1,false);
        }
        else if(i<90&&!lastTwoMoves((byte) 3)&&this.currentHealth>=this.maxHealth*0.2F){
            this.setMove((byte) 3,Intent.ATTACK_BUFF,this.damage.get(2).base,1,false);
        }
        else {
            this.setMove((byte) 4,Intent.ATTACK_DEFEND,this.damage.get(3).base,1,false);
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.state.setTimeScale(1);
            state.setAnimation(0,"Die",false);
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            super.die();
            SamiTreeHolePatch.winterFall = true;
            this.onBossVictoryLogic();
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }

}
