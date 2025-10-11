package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.powers.AbsorbFirePower;
import SamiRouge.powers.OneLivePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Redman extends AbstractMonster {
    public static final String ID = "samirg:Redman";
    private static final MonsterStrings monsterStrings;

    private int baseDamage = 3;

    private float wait01 = 1.2F;

    private int movedTurns = 0;


    public Redman(float x,float y){
        super(monsterStrings.NAME,ID,30,0F,0F,210F,210F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(15+15*AbstractDungeon.actNum);
        }
        else {
            setHp(12+12*AbstractDungeon.actNum);
        }

        baseDamage = 15 + AbstractDungeon.actNum * 9;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Redman/enemy_1135_redman.atlas","SamiRougeResources/img/monsters_SamiRouge/Redman/enemy_1135_redman.json",1.8F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"C_Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
        movedTurns = 0;
        powers.add(new OneLivePower(this,15*AbstractDungeon.actNum));
    }

    private void setFastMode(){
        state.setTimeScale(1);
        wait01 = 1.2F;
    }


    @Override
    public void takeTurn() {
        movedTurns++;
        if(nextMove == (byte) 1){
            addToBot(new ChangeStateAction(this,"ATTACK"));
            addToBot(new LongWaitAction(wait01));
            addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK":{
                this.state.setAnimation(0,"C_Attack",false);
                this.state.addAnimation(0,"C_Idle",true,0);
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if(movedTurns<2){
            this.setMove((byte) 2,Intent.UNKNOWN);
        }
        else
            this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }

    @Override
    public void die() {
        this.state.setTimeScale(1F);
        state.setAnimation(0,"C_Die",false);
        super.die();

    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}



