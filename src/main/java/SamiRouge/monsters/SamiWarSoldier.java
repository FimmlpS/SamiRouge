package SamiRouge.monsters;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.cards.curse.Coldness_SamiRouge;
import SamiRouge.powers.SelfCollapsePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
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

public class SamiWarSoldier extends AbstractMonster {
    public static final String ID = "samirg:SamiWarSoldier";
    private static final MonsterStrings monsterStrings;

    private boolean firstQH = true;

    private int baseDamage = 3;
    private int strength = 1;
    private int metal = 2;
    private int baseBlock = 12;

    private float wait01 = 0.9F;
    private float wait02 = 0.8F;

    AbstractCard status;

    public SamiWarSoldier(float x,float y){
        this(x,y,false,new Coldness_SamiRouge());
    }

    public SamiWarSoldier(float x,float y,boolean firstQH,AbstractCard status){
        super(monsterStrings.NAME,ID,70,0F,0F,240F,230F,null,x,y);
        this.firstQH = firstQH;
        this.status = status;
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(80);
        }
        else {
            setHp(70);
        }
        if(AbstractDungeon.ascensionLevel>=18){
            strength = 4;
            metal = 4;
        }
        else {
            strength = 2;
            metal = 2;
        }
        if(AbstractDungeon.ascensionLevel>=3){
            baseDamage = 5;
            baseBlock = 15;
        }
        else {
            baseDamage = 4;
            baseBlock = 12;
        }

        if(status!=null){
            if(AbstractDungeon.ascensionLevel>=18)
                status.upgrade();
            float amt = this.maxHealth*0.5F;
            AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
            if(ir instanceof IrreversibleMatrix){
                amt *= (1F + ((IrreversibleMatrix) ir).getBlvHP());
            }
            this.powers.add(new SelfCollapsePower(this,(int)amt,this.baseBlock,-1,status));
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smwar/enemy_2046_smwar.atlas","SamiRougeResources/img/monsters_SamiRouge/Smwar/enemy_2046_smwar.json",1.6F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
    }

    private void setFastMode(){
        if(Settings.FAST_MODE){
            state.setTimeScale(2);
            wait01 = 0.45F;
            wait02 = 0.4F;
        }
        else{
            state.setTimeScale(1);
            wait01 = 0.9F;
            wait02 = 0.8F;
        }
    }

    @Override
    public void usePreBattleAction() {

        super.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        //交替行动
        firstQH = !firstQH;
        setFastMode();
        switch (this.nextMove){
            case 1:{
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,strength),strength));
                addToBot(new ApplyPowerAction(this,this,new MetallicizePower(this,metal),metal));
                break;
            }
            case 2:{
                addToBot(new ChangeStateAction(this,"ATTACK01"));
                addToBot(new WaitAction(wait01));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new ChangeStateAction(this,"ATTACK02"));
                addToBot(new WaitAction(wait02));
                addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            }
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK01":{
                this.state.setAnimation(0,"Attack_A",false);
                break;
            }
            case "ATTACK02":{
                this.state.setAnimation(0,"Attack_B",false);
                this.state.addAnimation(0,"Idle",true,0F);
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if(firstQH){
            this.setMove((byte) 1,Intent.BUFF);
        }
        else {
            this.setMove((byte) 2,Intent.ATTACK,this.damage.get(0).base,2,true);
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
