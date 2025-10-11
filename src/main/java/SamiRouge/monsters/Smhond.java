package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.effects.FlkingEyeParticle;
import SamiRouge.powers.HondPower;
import SamiRouge.powers.UndyingPower;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class Smhond extends AbstractMonster {
    public static final String ID = "samirg:Smhond";
    private static final MonsterStrings monsterStrings;

    private int baseDamage;
    private int baseBlock;
    private int strength;

    private float wait = 0.5F;


    public Smhond(float x,float y){
        super(monsterStrings.NAME,ID,70,0F,0F,280F,230F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(400);
        }
        else {
            setHp(350);
        }
        if(AbstractDungeon.ascensionLevel>=18){
            strength = 5;
        }
        else {
            strength = 2;
        }
        if(AbstractDungeon.ascensionLevel>=3){
            baseDamage = 15;
            baseBlock = 12;
        }
        else {
            baseDamage = 12;
            baseBlock = 9;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smhond/enemy_2061_smhond.atlas","SamiRougeResources/img/monsters_SamiRouge/Smhond/enemy_2061_smhond.json",1.6F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
    }

    private void setFastMode(){
        state.setTimeScale(1);
        wait = 0.5F;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new HondPower(this)));
        super.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        setFastMode();
        switch (this.nextMove){
            case 1:{
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(wait));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,1,true),1));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player,1,true),1));
                break;
            }
            case 2:{
                for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
                    if(!m.isDeadOrEscaped()){
                        addToBot(new GainBlockAction(m,baseBlock));
                        addToBot(new ApplyPowerAction(m,this,new GainStrengthPower(m,strength),strength));
                    }
                }
                break;
            }
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK":{
                this.state.setAnimation(0,"Attack",false);
                this.state.addAnimation(0,"Idle",true,0F);
                break;
            }
            case "MOVE_BEGIN":{
                this.state.setAnimation(0,"Move",true);
            }
            case "MOVE_END":{
                this.state.setAnimation(0,"Idle",true);
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if(i<50)
            setMove((byte) 1,Intent.ATTACK_DEBUFF,damage.get(0).base);
        else{
            setMove((byte) 2,Intent.DEFEND_BUFF);
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

