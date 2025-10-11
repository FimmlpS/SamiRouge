package SamiRouge.monsters;

import SamiRouge.actions.*;
import SamiRouge.effects.FlkingEyeParticle;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.powers.KhanPower;
import SamiRouge.powers.VictoryPower;
import SamiRouge.samiMod.SamiRougeHelper;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;

public class Smkght extends AbstractSamiMonster {
    public static final String ID = "samirg:Smkght";
    public static final String BOSS_ID = "samirg:EnterEternal";
    public static final String ICON = "SamiRougeResources/img/monsters_SamiRouge/Smkght/EnterEternal_Icon.png";
    public static final String ICON_O = "SamiRougeResources/img/monsters_SamiRouge/Smkght/EnterEternal_Icon_O.png";
    private static final MonsterStrings monsterStrings;

    private int attackDamage;
    private int hugeDamage;
    private boolean mustHuge;
    private boolean singleTurn;
    private int moveTimes;

    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;

    private float waitTimes = 0.6F;
    private boolean summon;

    private void setFastMode(){

    }

    public Smkght(float x,float y){
        super(monsterStrings.NAME, ID,600,0F,0F,420F,400F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(800);
        }
        else {
            this.setHp(750);
        }
        if(AbstractDungeon.ascensionLevel>=19){
            summon = true;
        }
        else{
            summon = false;
        }

        if(AbstractDungeon.ascensionLevel>=4){
            attackDamage = 24;
            hugeDamage = 36;
        }
        else{
            attackDamage = 20;
            hugeDamage = 30;
        }
        moveTimes = 0;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smkght/enemy_2057_smkght.atlas","SamiRougeResources/img/monsters_SamiRouge/Smkght/enemy_2057_smkght.json",1.5F);
        this.flipHorizontal = false;
        setFastMode();
        this.eye = this.skeleton.findBone("C_Head");
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.BOSS;
        this.dialogX = 200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.damage.add(new DamageInfo(this,attackDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,hugeDamage, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        SamiRougeHelper.BOSS_KEY = "EnterEternal";
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        SamiRougeHelper.BOSS_KEY = "";

        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new SurroundedPower(AbstractDungeon.player)));
        addToBot(new ApplyPowerAction(this,this,new KhanPower(this,0),0));
        addToBot(new ApplyPowerAction(this,this,new VictoryPower(this,summon)));
    }

    @Override
    public void takeTurnForSingle(byte singleMove) {
        if(moveTimes>=0&&moveTimes<=3){
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, monsterStrings.DIALOG[moveTimes], 0.5F, 2.0F));
            addToBot(new LongWaitAction(1F));
        }

        moveTimes++;
        setFastMode();

        switch (singleMove){
            case 1:{
                addToBot(new ChangeStateAction(this, "SKILL"));
                addToBot(new ShakeScreenAction(0.3F, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.LOW));
                mustHuge = true;
                if(enableExtraIntent){
                    addToBot(new LongWaitAction(2.1F));
                }
                break;
            }
            case 2:{
                addToBot(new ChangeStateAction(this, "SKILL_END"));
                addToBot(new LongWaitAction(waitTimes));
                AbstractMonster lion = null;
                for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                    if(m instanceof Smlion){
                        lion = m;
                        break;
                    }
                }
                addToBot(new DamageOtherAction(AbstractDungeon.player,lion,damage.get(1), AbstractGameAction.AttackEffect.FIRE));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,8),8));
                break;
            }
            case 3:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new LongWaitAction(waitTimes));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if(enableExtraIntent){
                    addToBot(new LongWaitAction(0.4F));
                }
                break;
            }
            case 4:{
                for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                    if(!m.isDeadOrEscaped()){
                        addToBot(new GainBlockAction(m,20));
                        addToBot(new ApplyPowerAction(m,this,new FlameBarrierPower(m,5),5));
                    }
                }
                break;
            }
        }
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK":{
                this.state.setAnimation(0,"Attack",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
            case "SKILL":{
                this.state.setAnimation(0,"Skill_Begin",false);
                this.state.addAnimation(0,"Skill_Loop",true,0.0F);
                break;
            }
            case "SKILL_END":{
                this.state.setAnimation(0,"Skill_End",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
            case "VICTORY":{
                this.state.setAnimation(0,"Victory",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                onVictory();
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if(!enableExtraIntent){
            if(mustHuge){
                mustHuge = false;
                setMove((byte) 2,Intent.ATTACK_BUFF,damage.get(1).base);
            }
            else {
                if(moveTimes%4==2){
                    setMove((byte) 1,Intent.UNKNOWN);
                }
                else {
                    setMove((byte) 3,Intent.ATTACK,damage.get(0).base);
                }
            }
        }
        else{
            MultiMoveBuilder builder = new MultiMoveBuilder(this);
            if(singleTurn){
                builder.addMove((byte) 1,Intent.UNKNOWN)
                        .addMove((byte) 2,Intent.ATTACK_BUFF,damage.get(1).base)
                        .build();
            }
            else{
                builder.addMove((byte) 3,Intent.ATTACK, damage.get(0).base)
                        .addMove((byte) 4,Intent.BUFF)
                        .build();
            }
            singleTurn = !singleTurn;
        }
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && animateParticles){
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if(this.fireTimer < 0F){
                this.fireTimer = 0.1F;
                AbstractDungeon.effectList.add(new FlkingEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY() + 20F*Settings.scale));
            }
        }
    }

    public void onVictory(){
        //击退
        float yellowX = (float)Settings.WIDTH * 0.75F + 70F * Settings.xScale;
        for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
            if(m instanceof Smlion){
                yellowX = m.drawX;
            }
        }
        addToBot(new MoveTowardAction(this,yellowX));

        this.enableExtraIntent = true;
        if(nextMove == 1 || nextMove == 2){
            this.singleTurn = true;
        }
        addToBot(new RollMoveAction(this));

        addToBot(new SummonSmshdwAction(this.drawX+350F*Settings.scale,this.drawY));
        addToBot(new SummonSmshdwAction(this.drawX+600F*Settings.scale,this.drawY));
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.state.setTimeScale(1);
            state.setAnimation(0,"Die",false);
            boolean ending = true;
            for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                if(!m.isDeadOrEscaped() && m instanceof Smlion){
                    ending = false;
                    AbstractPower p = getPower(KhanPower.POWER_ID);
                    AbstractPower copy = new KhanPower(m,p!=null?p.amount:0);
                    addToTop(new VictoryAction(m,copy,summon));
                    break;
                }
            }
            super.die();
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDead && !m.isDying) {
                    if (AbstractDungeon.player.hasPower("Surrounded")) {
                        AbstractDungeon.player.flipHorizontal = m.drawX < AbstractDungeon.player.drawX;
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Surrounded"));
                    }

                    if (m.hasPower("BackAttack")) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, m, "BackAttack"));
                    }
                }
            }
            (AbstractDungeon.getCurrRoom()).rewardAllowed = false;
            if(ending){
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDead && !m.isDying) {
                        AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                        AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                    }
                }
                SamiTreeHolePatch.whatBeginsFollowsWhatEnds = true;
                this.useFastShakeAnimation(5.0F);
                CardCrawlGame.screenShake.rumble(4.0F);
                this.onBossVictoryLogic();
                this.onFinalBossVictoryLogic();
                CardCrawlGame.stopClock = true;
            }
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }

}

