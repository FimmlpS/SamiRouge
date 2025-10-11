package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.actions.RemoveLastDebuffAction;
import SamiRouge.powers.CountryPower;
import SamiRouge.powers.RootagePower;
import SamiRouge.powers.ScarePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

public class Smgrd extends AbstractMonster {
    public static final String ID = "samirg:Smgrd";
    private static final MonsterStrings monsterStrings;

    private int baseDamage = 32;
    private int doubleDamage = 15;
    private int block;
    float waitTime = 0.6F;
    boolean special;

    public Smgrd(float x,float y){
        super(monsterStrings.NAME, ID,240,0F,0F,280F,300F,null,x,y);

        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(AbstractDungeon.actNum<3?200:400);
        }
        else {
            this.setHp(AbstractDungeon.actNum<3?160:320);
        }

        if(AbstractDungeon.ascensionLevel>=4){
            baseDamage = 9;
            doubleDamage = 15;
            block = 8;
        }
        else{
            baseDamage = 7;
            doubleDamage = 12;
            block = 6;
        }

        if(AbstractDungeon.actNum>=3){
            baseDamage*=2;
            doubleDamage*=2;
            block*=2;
        }

        special = AbstractDungeon.ascensionLevel>=18;

        this.powers.add(new CountryPower(this,special));

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smgrd/enemy_2048_smgrd.atlas","SamiRougeResources/img/monsters_SamiRouge/Smgrd/enemy_2048_smgrd.json",1.4F);
        this.flipHorizontal = true;
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,baseDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,doubleDamage, DamageInfo.DamageType.NORMAL));
    }

    @Override
    public void usePreBattleAction() {
        if(AbstractDungeon.ascensionLevel>=18){
            addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new ScarePower(AbstractDungeon.player,1),1));
        }
        super.usePreBattleAction();
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
            case "COMBAT":
                this.state.setAnimation(0, "Combat", false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
        }
    }

    @Override
    public void takeTurn() {
        int countryAmount = 1;
        AbstractPower country = getPower(CountryPower.POWER_ID);
        if(country!=null&&country.amount>0){
            countryAmount+=country.amount;
        }
        switch (this.nextMove){
            case 1:
                for(int i =0;i<countryAmount;i++){
                    addToBot(new GainBlockAction(this,block));
                }
                for(int i =0;i<countryAmount;i++){
                    addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                }
                for(int i =0;i<countryAmount;i++){
                    addToBot(new RemoveLastDebuffAction(this));
                }
                break;
            case 2:
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(this.waitTime));
                for(int i =0;i<countryAmount;i++){
                    addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                for(int i =0;i<countryAmount;i++){
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new ScarePower(AbstractDungeon.player,1),1));
                }

                break;
            case 3:
                addToBot(new ChangeStateAction(this,"COMBAT"));
                addToBot(new LongWaitAction(this.waitTime));
                for(int i =0;i<countryAmount;i++){
                    addToBot(new DamageAction(AbstractDungeon.player,damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }

                break;
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        AbstractPower country = getPower(CountryPower.POWER_ID);
        if(country!=null){
            int amount = (int) ((float)(this.maxHealth-currentHealth)/((float)this.maxHealth/(special?5:4)));
            if(country.amount!=amount){
                country.amount = amount;
                country.onSpecificTrigger();
                if(this.moveHistory.size()>0){
                    byte move = moveHistory.get(moveHistory.size()-1);
                    if(move==(byte) 3){
                        setMove((byte) 3,Intent.ATTACK,damage.get(1).base,1+amount,amount>0);
                    }
                    else if(move==(byte) 2){
                        setMove((byte) 2,Intent.ATTACK_DEBUFF,damage.get(0).base,1+amount,amount>0);
                    }
                    createIntent();
                }
            }
        }
    }

    @Override
    protected void getMove(int i) {
        int amt = 1;
        AbstractPower country = getPower(CountryPower.POWER_ID);
        if(country!=null){
            amt+=country.amount;
        }
        if(GameActionManager.turn>0&&GameActionManager.turn%3==0){

            setMove((byte) 3,Intent.ATTACK,damage.get(1).base,amt,amt>1);
        }
        else {
            if(i<50){
                setMove((byte) 1,Intent.DEFEND_BUFF);
            }
            else {
                setMove((byte) 2,Intent.ATTACK_DEBUFF,damage.get(0).base,amt,amt>1);
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




