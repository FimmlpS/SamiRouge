package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.powers.RootagePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class Smgia extends AbstractMonster {
    public static final String ID = "samirg:Smgia";
    private static final MonsterStrings monsterStrings;

    boolean firstTurn = true;
    boolean singleTurn = true;
    private int baseDamage = 32;
    private int doubleDamage = 15;
    private int firstTurnDamage = 12;
    int thornLevels = 20;
    int doubleTimes = 0;
    float waitTime = 0.3F;

    public Smgia(float x,float y){
        super(monsterStrings.NAME, ID,240,0F,0F,280F,300F,null,x,y);

        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(AbstractDungeon.actNum<3?300:600);
        }
        else {
            this.setHp(AbstractDungeon.actNum<3?240:480);
        }

        if(AbstractDungeon.ascensionLevel>=4){
            baseDamage = 24;
            doubleDamage = 12;
            firstTurnDamage = 9;
        }
        else{
            baseDamage = 20;
            doubleDamage = 10;
            firstTurnDamage = 7;
        }

        if(AbstractDungeon.actNum>=3){
            baseDamage*=2;
            doubleDamage*=2;
            firstTurnDamage*=2;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smgia/enemy_2052_smgia.atlas","SamiRougeResources/img/monsters_SamiRouge/Smgia/enemy_2052_smgia.json",1.4F);
        this.flipHorizontal = true;
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,baseDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,doubleDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,firstTurnDamage, DamageInfo.DamageType.NORMAL));
        firstTurn = true;
        doubleTimes = 0;
        powers.add(new RootagePower(this,1));
    }


    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
            case "SKILL":
                this.state.setAnimation(0, "Skill", false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
        }
    }

    @Override
    public void takeTurn() {
        if(firstTurn)
            firstTurn = false;
        else
            singleTurn = !singleTurn;
        switch (this.nextMove){
            case 1:
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(this.waitTime));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new StrengthPower(AbstractDungeon.player,-1),-1));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DexterityPower(AbstractDungeon.player,-1),-1));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FocusPower(AbstractDungeon.player,-1),-1));
                break;
            case 2:
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(this.waitTime));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 3:
                doubleTimes++;
                addToBot(new ChangeStateAction(this,"SKILL"));
                addToBot(new LongWaitAction(this.waitTime));
                for(int i =0;i<2;i++){
                    addToBot(new DamageAction(AbstractDungeon.player,damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                if(doubleTimes==1){
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,99,true),99));
                }
                else if(doubleTimes==2){
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player,99,true),99));
                }
                else if(doubleTimes==3){
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new VulnerablePower(AbstractDungeon.player,99,true),99));
                }
                break;
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if(firstTurn){
            this.setMove((byte) 1,Intent.ATTACK_DEBUFF,damage.get(2).base);
        }
        else {
            if(singleTurn)
                this.setMove((byte) 2,Intent.ATTACK_BUFF,damage.get(0).base);
            else if(doubleTimes<=3)
                this.setMove((byte) 3,Intent.ATTACK_DEBUFF,damage.get(1).base,2,true);
            else
                this.setMove((byte) 3,Intent.ATTACK,damage.get(1).base,2,true);
        }
    }

    @Override
    public void die() {
        this.state.setTimeScale(1F);
        state.setAnimation(0,"Die",false);
        super.die();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }

}



