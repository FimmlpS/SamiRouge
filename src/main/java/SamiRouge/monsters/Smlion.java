package SamiRouge.monsters;

import SamiRouge.actions.*;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.powers.LionPower;
import SamiRouge.powers.VictoryPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Smlion extends AbstractSamiMonster {
    public static final String ID = "samirg:Smlion";
    private static final MonsterStrings monsterStrings;

    private int attackDamage;
    private int crashDamage;
    private int scarAmount;
    private int hugeDamage;
    private boolean mustHuge;
    private boolean singleTurn;
    private int moveTimes;

    private float waitTimes = 0.6F;
    private float waitTimesFirst = 0.8F;
    private float waitTimesSecond = 0.7F;
    private boolean summon;

    private void setFastMode(){

    }

    public Smlion(float x,float y){
        super(monsterStrings.NAME, ID,600,0F,0F,420F,400F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(800);
        }
        else {
            this.setHp(750);
        }
        if(AbstractDungeon.ascensionLevel>=19){
            scarAmount = 2;
            summon = true;
        }
        else{
            scarAmount = 1;
            summon = false;
        }

        if(AbstractDungeon.ascensionLevel>=4){
            attackDamage = 10;
            crashDamage = 15;
            hugeDamage = 36;
        }
        else{
            attackDamage = 8;
            crashDamage  =12;
            hugeDamage = 30;
        }
        moveTimes = 0;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smlion/enemy_2058_smlion.atlas","SamiRougeResources/img/monsters_SamiRouge/Smlion/enemy_2058_smlion.json",1.5F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.BOSS;
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.damage.add(new DamageInfo(this,attackDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,crashDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,hugeDamage, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new LionPower(this,0),0));
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
                AbstractMonster kght = null;
                for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                    if(m instanceof Smkght){
                        kght = m;
                        break;
                    }
                }
                addToBot(new DamageOtherAction(AbstractDungeon.player,kght,damage.get(2), AbstractGameAction.AttackEffect.FIRE));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                break;
            }
            case 3:{
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new LongWaitAction(waitTimesFirst));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new LongWaitAction(waitTimesSecond));
                addToBot(new ChangeStateAction(this,"ATTACK_2"));
                addToBot(new LongWaitAction(waitTimes));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if(enableExtraIntent){
                    addToBot(new LongWaitAction(1.2F));
                }
                break;
            }
            case 4:{
                addToBot(new ChangeStateAction(this, "CRASH"));
                addToBot(new LongWaitAction(waitTimes));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                addToBot(new ChangeStateAction(this, "CRASH_2"));
                addToBot(new MakeTempCardInDrawPileAction(new Wound(),2,true,true));
            }
        }


    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK":{
                this.state.setAnimation(0,"Combat",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
            case "ATTACK_2":{
                this.state.setAnimation(0,"Attack",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
            case "CRASH":{
                this.state.setAnimation(0,"Skill_1_Begin",false);
                break;
            }
            case "CRASH_2":{
                this.state.setAnimation(0,"Skill_1_End",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
            case "SKILL":{
                this.state.setAnimation(0,"Skill_2_Begin",false);
                this.state.addAnimation(0,"Skill_2_Loop",true,0.0F);
                break;
            }
            case "SKILL_END":{
                this.state.setAnimation(0,"Skill_2_End",false);
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
        if (!enableExtraIntent) {
            if (mustHuge) {
                mustHuge = false;
                setMove((byte) 2, Intent.ATTACK_BUFF, damage.get(2).base);
            } else {
                if (moveTimes % 4 == 0) {
                    setMove((byte) 1, Intent.UNKNOWN);
                } else if (moveTimes % 4 == 2) {
                    setMove((byte) 3, Intent.ATTACK, damage.get(0).base, 2, true);
                } else {
                    setMove((byte) 4, Intent.ATTACK_DEBUFF, damage.get(1).base);
                }
            }
        }
        else{
            MultiMoveBuilder builder = new MultiMoveBuilder(this);
            if(singleTurn){
                builder.addMove((byte) 1,Intent.UNKNOWN)
                        .addMove((byte) 2,Intent.ATTACK_BUFF,damage.get(2).base)
                        .build();
            }
            else{
                builder.addMove((byte) 3,Intent.ATTACK, damage.get(0).base, 2, true)
                        .addMove((byte) 4,Intent.ATTACK_DEBUFF, damage.get(1).base)
                        .build();
            }
            singleTurn = !singleTurn;
        }
    }

    public void onVictory(){
        //击退
        float redX = (float)Settings.WIDTH * 0.75F + 70F * Settings.xScale;
        for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
            if(m instanceof Smkght){
                redX = m.drawX;
            }
        }
        addToBot(new MoveTowardAction(this,redX));

        this.enableExtraIntent = true;
        if(nextMove == 1 || nextMove == 2){
            this.singleTurn = true;
        }
        addToBot(new RollMoveAction(this));

        addToBot(new SummonSmhondAction(this.drawX - 400F *Settings.scale,this.drawY));
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.state.setTimeScale(1);
            state.setAnimation(0,"Die",false);
            boolean ending = true;
            for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                if(!m.isDeadOrEscaped() && m instanceof Smkght){
                    ending = false;
                    AbstractPower p = getPower(LionPower.POWER_ID);
                    AbstractPower copy = new LionPower(m,p!=null?p.amount:0);
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


