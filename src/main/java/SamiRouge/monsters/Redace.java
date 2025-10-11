package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.powers.ProjectPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Redace extends AbstractMonster {
    public static final String ID = "samirg:Redace";
    private static final MonsterStrings monsterStrings;

    private int baseDamage = 3;
    private int strength = 1;
    private int baseBlock = 12;

    private float wait01 = 0.5F;


    public Redace(float x,float y){
        super(monsterStrings.NAME,ID,70,0F,0F,210F,210F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(35+35*AbstractDungeon.actNum);
        }
        else {
            setHp(30+30*AbstractDungeon.actNum);
        }
        if(AbstractDungeon.ascensionLevel>=18){
            strength = 2;
        }
        else {
            strength = 1;
        }

        baseDamage = 8 + AbstractDungeon.actNum * 4;
        baseBlock = 3 + AbstractDungeon.actNum * 3;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Redace/enemy_1136_redace.atlas","SamiRougeResources/img/monsters_SamiRouge/Redace/enemy_1136_redace.json",1.8F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
        powers.add(new ProjectPower(this));
    }

    private void setFastMode(){
        state.setTimeScale(1);
        wait01 = 0.5F;
    }

    @Override
    public void takeTurn() {
        setFastMode();
        switch (this.nextMove){
            case 1:{
                for(AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(!m.isDeadOrEscaped()){
                        addToBot(new ApplyPowerAction(m,this,new StrengthPower(m,strength),strength));
                        addToBot(new GainBlockAction(m,baseBlock));
                    }
                }
                break;
            }
            case 2:{
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,strength),strength));
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
                state.addAnimation(0,"Idle",true,0F);
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        boolean alive = false;
        for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
            if(!m.isDeadOrEscaped() && m instanceof Hologc){
                alive = true;
                break;
            }
        }

        if(alive){
            if(AbstractDungeon.ascensionLevel>=18){
                this.setMove((byte) 1,Intent.DEFEND_BUFF);
            }
            else {
                this.setMove((byte) 1,Intent.DEFEND);
            }
        }
        else {
            if(AbstractDungeon.ascensionLevel>=18){
                this.setMove((byte) 2,Intent.ATTACK_BUFF,this.damage.get(0).base);
            }
            else {
                this.setMove((byte) 2,Intent.ATTACK,this.damage.get(0).base);
            }

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

