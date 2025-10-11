package SamiRouge.monsters;

import SamiRouge.actions.CressonMoveAction;
import SamiRouge.actions.CressonTotalMoveAction;
import SamiRouge.cards.curse.Coldness_SamiRouge;
import SamiRouge.cards.curse.Freeze_SamiRouge;
import SamiRouge.cards.curse.Hypothermia_SamiRouge;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.powers.*;
import SamiRouge.samiMod.SamiRougeHelper;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.AwakenedWingParticle;

import java.util.ArrayList;
import java.util.Iterator;

public class Cresson extends AbstractMonster {
    public static final String ID = "samirg:Cresson";
    //todo
    public static final String ICON = "SamiRougeResources/img/monsters_SamiRouge/Smedzi/Cresson_Icon.png";
    public static final String ICON_O = "SamiRougeResources/img/monsters_SamiRouge/Smedzi/Cresson_Icon_O.png";
    private static final MonsterStrings monsterStrings;

    private boolean firstTurn = true;
    private int baseDamage = 9;
    private int entityAmount = 1;
    private float wait01 = 1.6F;
    private float wait02 = 1.2F;
    public float move01 = 1.0F;
    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;
    private boolean hasEntity = false;
    public float[] posX;
    public float[] posY;
    public boolean[] entities;
    public int curX = 0;
    public int curY = 0;
    private float startX;
    private float startY;

    private int moveTimes = 0;

    private void setFastMode(){
        if(Settings.FAST_MODE){
            state.setTimeScale(2);
            wait01 = 0.8F;
            wait02 = 0.6F;
            move01 = 0.5F;
        }
        else{
            state.setTimeScale(1);
            wait01 = 1.6F;
            wait02 = 1.2F;
            move01 = 1.0F;
        }
    }

    public boolean canMove(){
        for(int i =0;i<entities.length;i++){
            if(!entities[i])
                return true;
        }
        return false;
    }

    //仅在Action中调用
    public void setPosition(int xIndex,int yIndex){
        this.curX = xIndex;
        this.curY = yIndex;
        this.entities[xIndex*3+yIndex] = true;
        float tmpX = (float)Settings.WIDTH * 0.75F;
        float tmpY = AbstractDungeon.floorY;
        this.drawX = tmpX+posX[xIndex*3+yIndex]*Settings.xScale;
        this.drawY = tmpY+(posY[xIndex*3+yIndex]-50F)*Settings.yScale;
        this.refreshHitboxLocation();
        this.refreshIntentHbLocation();
    }

    public Cresson(float x,float y){
        super(monsterStrings.NAME, ID,320,0F,0F,280F,280F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(450);
        }
        else {
            this.setHp(400);
        }
        if(AbstractDungeon.ascensionLevel>=19){
            entityAmount = 2;
        }
        else{
            entityAmount = 1;
        }

        if(AbstractDungeon.ascensionLevel>=4){
            baseDamage = 12;

        }
        else{
            baseDamage = 9;

        }
        firstTurn = true;
        this.curX = 1;
        this.curY = 1;
        this.startX = x;
        this.startY = y;
        posX = new float[9];
        posY = new float[9];
        entities = new boolean[9];
        for(int i =0;i<3;i++){
            for(int j=0;j<3;j++){
                posX[i*3+j] = x-200F + i*200F;
                posY[i*3+j] = y-200F + j*200F;
            }
        }
        for(int i =0;i<9;i++){
            entities[i] = false;
        }
        //自身
        entities[4] = true;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smedzi/enemy_2056_smedzi.atlas","SamiRougeResources/img/monsters_SamiRouge/Smedzi/enemy_2056_smedzi.json",2.0F);
        this.flipHorizontal = true;
        eye = this.skeleton.findBone("Body");
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.BOSS;
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        SamiRougeHelper.BOSS_KEY = "Cresson";
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        SamiRougeHelper.BOSS_KEY = "";

        firstTurn = true;
        hasEntity = false;
        addToBot(new ApplyPowerAction(this,this,new NonAndExistencePower(this)));
        addToBot(new ApplyPowerAction(this,this,new SpaceRefractionPower(this)));
        if(AbstractDungeon.ascensionLevel>=19)
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player, new SpaceStabilityPower(AbstractDungeon.player,2,false)));
        else{
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player, new SpaceStabilityPower(AbstractDungeon.player,1,true)));
        }
    }

    @Override
    public void takeTurn() {
        if(firstTurn){
            firstTurn = false;
        }
        setFastMode();
        switch (this.nextMove){
            case 1:{
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new StrengthPower(AbstractDungeon.player,-2),-2));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DexterityPower(AbstractDungeon.player,-2),-2));
                if(AbstractDungeon.ascensionLevel>=19)
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DimensionCollapsePower(AbstractDungeon.player,12),12));
                break;
            }
            case 2:{
                hasEntity = true;
                addToBot(new MakeTempCardInDrawPileAction(new VoidCard(),entityAmount,true,true,false));
                addToBot(new MakeTempCardInHandAction(new VoidCard(),entityAmount));
                addToBot(new MakeTempCardInDiscardAction(new VoidCard(),entityAmount));
                break;
            }
            case 3:{
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new StrengthPower(AbstractDungeon.player,-1),-1));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DexterityPower(AbstractDungeon.player,-1),-1));
                if(AbstractDungeon.ascensionLevel>=19)
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DimensionCollapsePower(AbstractDungeon.player,6),6));
                break;
            }
            case 4:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new WaitAction(wait02));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            }
        }

        moveTimes++;

        addToBot(new CressonTotalMoveAction(this));
        if(AbstractDungeon.ascensionLevel>=19){
            addToBot(new CressonTotalMoveAction(this));
        }
        if(moveTimes>=4){
            addToBot(new CressonTotalMoveAction(this));
        }
        if(moveTimes>=7){
            addToBot(new CressonTotalMoveAction(this));
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
            case "MOVE_START":{
                this.state.setAnimation(0,"Move_Begin",false);
                break;
            }
            case "MOVE_END":{
                this.state.setAnimation(0,"Move_End",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if(firstTurn){
            this.setMove((byte) 1,Intent.STRONG_DEBUFF);
        }
        else if(!hasEntity&&this.currentHealth<=0.5F*this.maxHealth){
            this.setMove((byte) 2,Intent.STRONG_DEBUFF);
        }
        else if(lastTwoMoves((byte) 4)){
            this.setMove((byte) 3,Intent.DEBUFF);
        }
        else{
            this.setMove((byte) 4,Intent.ATTACK,damage.get(0).base,2,true);
        }
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && animateParticles){
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if(this.fireTimer < 0F){
                this.fireTimer = 0.1F;
                AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY()));
            }
        }
    }


    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.state.setTimeScale(1);
            state.setAnimation(0,"Die",false);
            this.useFastShakeAnimation(6.5F);
            CardCrawlGame.screenShake.rumble(4.5F);
            super.die();
            Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

            while(var1.hasNext()) {
                AbstractMonster m = (AbstractMonster)var1.next();
                if (!m.isDead && !m.isDying) {
                    AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                    AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                }
            }
            SamiTreeHolePatch.longIntoAnAbyss = true;
            this.onBossVictoryLogic();
        }
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}
