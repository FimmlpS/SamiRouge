package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.actions.SamiDroneMoveAction;
import SamiRouge.effects.FlkingEyeParticle;
import SamiRouge.powers.UndyingPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class Smdrn extends AbstractMonster {
    public static final String ID = "samirg:Smdrn";
    private static final MonsterStrings monsterStrings;

    public float leftDraw = (float)Settings.WIDTH * 0.75F + -1000F * Settings.xScale;
    public float rightDraw = (float)Settings.WIDTH * 0.75F + 70F * Settings.xScale;
    public float upDraw = AbstractDungeon.floorY + 400F * Settings.yScale;
    public float downDraw = AbstractDungeon.floorY + 360F *Settings.yScale;

    private int baseDamage;

    private float wait = 0.5F;

    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;


    public Smdrn(float x,float y){
        super(monsterStrings.NAME,ID,70,0F,0F,120F,100F,null,x,y);
        int hp = 6*AbstractDungeon.actNum;
        if(AbstractDungeon.ascensionLevel>=8){
            setHp((int)(1.35f*hp));
        }
        else {
            setHp(hp);
        }
        if(AbstractDungeon.ascensionLevel>=18){

        }
        else {

        }
        if(AbstractDungeon.ascensionLevel>=3){
            baseDamage = 4 + 2*AbstractDungeon.actNum;
        }
        else {
            baseDamage = 3 + 2*AbstractDungeon.actNum;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smdrn/enemy_2045_smdrn.atlas","SamiRougeResources/img/monsters_SamiRouge/Smdrn/enemy_2045_smdrn.json",1.8F);
        this.flipHorizontal = true;
        setFastMode();
        this.state.setAnimation(0,"Idle",true);
        this.eye = this.skeleton.findBone("Body");
        this.type = EnemyType.NORMAL;
        this.powers.add(new FlightPower(this,1));
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
    }

    private void setFastMode(){
        state.setTimeScale(1);
        wait = 0.5F;
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
                addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                addToBot(new VFXAction(new BorderFlashEffect(Color.SKY)));
                addToBot(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                addToBot(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                addToBot(new MakeTempCardInHandAction(new VoidCard()));
                addToBot(new SamiDroneMoveAction(this));
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
        }
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && animateParticles){
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if(this.fireTimer < 0F){
                this.fireTimer = 0.1F;
                AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY() + 5F* Settings.scale));
            }
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 1,Intent.ATTACK_DEBUFF,damage.get(0).base);
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

