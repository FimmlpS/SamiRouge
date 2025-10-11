package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.powers.AbsorbFirePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Hologc extends AbstractMonster {
    public static final String ID = "samirg:Hologc";
    private static final MonsterStrings monsterStrings;

    private int baseDamage = 3;

    private float wait01 = 0.5F;


    public Hologc(float x,float y){
        super(monsterStrings.NAME,ID,70,0F,0F,210F,210F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(70*AbstractDungeon.actNum);
        }
        else {
            setHp(60*AbstractDungeon.actNum);
        }

        baseDamage = 8 + AbstractDungeon.actNum * 4;

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Hologc/enemy_1139_hologc.atlas","SamiRougeResources/img/monsters_SamiRouge/Hologc/enemy_1139_hologc.json",1.8F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
    }

    private void setFastMode(){
        state.setTimeScale(1);
        wait01 = 0.5F;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this, new AbsorbFirePower(this)));
        super.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        if(nextMove == (byte) 1){
            addToBot(new ChangeStateAction(this,"ATTACK"));
            addToBot(new LongWaitAction(wait01));
            addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
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
        this.setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
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


