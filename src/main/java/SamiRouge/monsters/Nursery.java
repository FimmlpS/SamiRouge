package SamiRouge.monsters;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.powers.NonAndExistencePower;
import SamiRouge.powers.SelfCollapsePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class Nursery extends AbstractMonster {
    public static final String ID = "samirg:Nursery";
    private static final MonsterStrings monsterStrings;

    private int strength = 1;
    private int baseBlock = 6;
    public int index = -1;

    Cresson cresson;

    public void setCresson(Cresson cresson,int index){
        this.cresson = cresson;
        this.index = index;
    }

    public Nursery(){
        this(0,0);
    }

    public Nursery(float x,float y){
        super(monsterStrings.NAME,ID,40,0F,0F,130F,130F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(36);
        }
        else {
            setHp(32);
        }
        if(AbstractDungeon.ascensionLevel>=19){
            strength = 3;
            baseBlock = 8;
        }
        else {
            strength = 2;
            baseBlock = 6;
        }
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!=null){
            baseBlock += ir.counter/2;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smedzi/enemy_2056_smedzi.atlas","SamiRougeResources/img/monsters_SamiRouge/Smedzi/enemy_2056_smedzi.json",3.6F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.NORMAL;

        this.powers.add(new ThornsPower(this,2));
    }

    private void setFastMode(){
        if(Settings.FAST_MODE){
            state.setTimeScale(2);
        }
        else{
            state.setTimeScale(1);
        }
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        setFastMode();
        switch (this.nextMove){
            case 1:{
                if(cresson!=null){
                    addToBot(new ApplyPowerAction(cresson,this,new StrengthPower(this,strength),strength));
                    addToBot(new GainBlockAction(cresson,this.baseBlock));
                }
                else{
                    addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,strength),strength));
                    addToBot(new GainBlockAction(this,this.baseBlock));
                }
                break;
            }
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        this.setMove((byte) 1,Intent.DEFEND_BUFF);
    }

    @Override
    public void die() {
        this.state.setTimeScale(1F);
        if(cresson!=null&&index>0)
            this.cresson.entities[this.index] = false;
        super.die();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}
