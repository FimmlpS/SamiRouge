package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.effects.FlkingEyeParticle;
import SamiRouge.powers.SelfCollapsePower;
import SamiRouge.powers.UndyingPower;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Smshdw extends AbstractMonster {
    public static final String ID = "samirg:Smshdw";
    private static final MonsterStrings monsterStrings;

    private int baseDamage;
    private int strength;

    private float wait = 0.5F;

    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;


    public Smshdw(float x,float y){
        super(monsterStrings.NAME,ID,70,0F,0F,180F,230F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=8){
            setHp(80);
        }
        else {
            setHp(70);
        }
        if(AbstractDungeon.ascensionLevel>=18){
            strength = 10;
        }
        else {
            strength = 6;
        }
        if(AbstractDungeon.ascensionLevel>=3){
            baseDamage = 7;
        }
        else {
            baseDamage = 5;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smshdw/enemy_2060_smshdw.atlas","SamiRougeResources/img/monsters_SamiRouge/Smshdw/enemy_2060_smshdw.json",1.6F);
        this.flipHorizontal = false;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.eye = this.skeleton.findBone("C_Head");
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
    }

    private void setFastMode(){
        state.setTimeScale(1);
        wait = 0.5F;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this,this,new UndyingPower(this)));
        super.usePreBattleAction();
    }

    @Override
    public void takeTurn() {
        setFastMode();
        switch (this.nextMove){
            case 1:{
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(wait));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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

    @Override
    protected void getMove(int i) {
        setMove((byte) 1,Intent.ATTACK_BUFF,damage.get(0).base);
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
