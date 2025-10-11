package SamiRouge.monsters;


import SamiRouge.actions.LongWaitAction;
import SamiRouge.actions.SummonSmdrnAction;
import SamiRouge.powers.TwistedPower;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;

public class Smsha extends AbstractSamiMonster {
    public static final String ID = "samirg:Smsha";
    private static final MonsterStrings monsterStrings;

    int tripleDamage = 8;
    int singleDamage = 18;

    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;

    public Smsha(float x,float y){
        super(monsterStrings.NAME,ID,150,0F,0F,250F,280F,null,x,y);

        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(AbstractDungeon.actNum<3?180:360);
        }
        else {
            this.setHp(AbstractDungeon.actNum<3?150:300);
        }

        if(AbstractDungeon.ascensionLevel>=4){
           tripleDamage = 10;
           singleDamage = 24;
        }
        else{
           tripleDamage = 8;
           singleDamage = 18;
        }

        if(AbstractDungeon.actNum>=3){
            tripleDamage*=2;
            singleDamage*=2;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smsha/enemy_2050_smsha.atlas","SamiRougeResources/img/monsters_SamiRouge/Smsha/enemy_2050_smsha.json",1.4F);
        this.flipHorizontal = true;
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;

        this.eye = this.skeleton.findBone("C_Head");

        damage.add(new DamageInfo(this,tripleDamage));
        damage.add(new DamageInfo(this,singleDamage));
        enableExtraIntent = true;


        powers.add(new TwistedPower(this));
    }

    @Override
    public void takeTurnForSingle(byte singleMove) {
        switch (singleMove){
            case 1:
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(0.6F));
                for(int i =0;i<3;i++){
                    addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
            case 2:
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DrawReductionPower(AbstractDungeon.player,1),1));
                break;
            case 3:
                addToBot(new ChangeStateAction(this,"SKILL"));
                addToBot(new LongWaitAction(0.5F));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case 4:
                addToBot(new ApplyPowerAction(this,this,new StrengthPower(this,2),2));
                break;
            case 5:
                addToBot(new SummonSmdrnAction((float) Settings.WIDTH * 0.5F + -200F * Settings.xScale,AbstractDungeon.floorY + 380F * Settings.yScale));
                break;
            case 6:
                addToBot(new GainBlockAction(this,this,11));
                break;
            case 7:
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new FrailPower(AbstractDungeon.player,1,true),1));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        MultiMoveBuilder builder = new MultiMoveBuilder(this);
        if(i<45 && !lastMove((byte) 1,1)){
            builder.addMove((byte) 1,Intent.ATTACK,damage.get(0).base,3,true);
            builder.addMove((byte) 2,Intent.DEBUFF);
        }
        else if(i<90 && !lastMove((byte) 3,1)){
            builder.addMove((byte) 3,Intent.ATTACK,damage.get(1).base);
            builder.addMove((byte) 4,Intent.BUFF);
        }
        else{
            builder.addMove((byte) 5,Intent.UNKNOWN);
            builder.addMove((byte) 6,Intent.DEFEND);
        }
        if(currentHealth<=0.5F*maxHealth){
            builder.addMove((byte) 7,Intent.STRONG_DEBUFF).removeLastHistory();
        }
        builder.build();
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
    public void die() {
        this.state.setTimeScale(1F);
        state.setAnimation(0,"Die",false);
        super.die();
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && animateParticles){
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if(this.fireTimer < 0F){
                this.fireTimer = 0.1F;
                AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY() + 30F* Settings.scale));
            }
        }
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}
