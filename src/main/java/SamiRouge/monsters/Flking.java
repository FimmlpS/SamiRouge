package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.effects.FlkingEyeParticle;
import SamiRouge.powers.BowToBowPower;
import SamiRouge.powers.EndingPower;
import SamiRouge.powers.RootagePower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;

public class Flking extends AbstractMonster {
    public static final String ID = "samirg:Flking";
    private static final MonsterStrings monsterStrings;

    private int baseDamage = 8;
    private int doubleDamage = 20;
    int metals = 6;
    float waitTime = 0.5F;

    boolean firstTurn;

    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;

    public Flking(float x,float y){
        super(monsterStrings.NAME, ID,240,0F,0F,280F,300F,null,x,y);

        if(AbstractDungeon.ascensionLevel>=9)
        {
            this.setHp(AbstractDungeon.actNum<3?100:200);
        }
        else {
            this.setHp(AbstractDungeon.actNum<3?90:180);
        }


        if(AbstractDungeon.ascensionLevel>=18){
            metals = 8;
        }
        else {
            metals = 6;
        }
        if(AbstractDungeon.actNum>=3){
            metals*=2;
        }
        //在格挡属性上应用不可逆矩
        AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(ir!= null){
            metals += ir.counter;
        }

        if(AbstractDungeon.ascensionLevel>=4){
            baseDamage = 12;
            doubleDamage = 20;
        }
        else{
            baseDamage = 8;
            doubleDamage = 16;
        }
        if(AbstractDungeon.actNum>=3){
            baseDamage*=2;
            doubleDamage*=2;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Flking/enemy_2008_flking.atlas","SamiRougeResources/img/monsters_SamiRouge/Flking/enemy_2008_flking.json",1.4F);
        this.flipHorizontal = true;
        this.eye = this.skeleton.findBone("bone19");
        this.state.setAnimation(0,"Idle",true);
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this,baseDamage, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,doubleDamage, DamageInfo.DamageType.NORMAL));
        firstTurn = true;
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new EndingPower(AbstractDungeon.player)));
        addToBot(new ApplyPowerAction(this,this,new BarricadePower(this)));
        addToBot(new ApplyPowerAction(this,this,new MetallicizePower(this,metals),metals));
        addToBot(new ApplyPowerAction(this,this,new BowToBowPower(this)));
        super.usePreBattleAction();
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK_FAR":
                this.state.setAnimation(0, "Attack_1", false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
            case "ATTACK":
                int random = MathUtils.random(0,1);
                if(random==0)
                    this.state.setAnimation(0, "Attack_2", false);
                else
                    this.state.setAnimation(0,"Attack_3",false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
            case "SKILL":
                this.state.setAnimation(0, "Skill", false);
                this.state.addAnimation(0, "Idle", true, 0F);
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (i<40) {
            this.setMove((byte) 1, Intent.ATTACK_BUFF,damage.get(0).base);
        } else if (i<70) {
            this.setMove((byte) 2, Intent.ATTACK, damage.get(1).base);
        } else {
            this.setMove((byte) 3, Intent.DEFEND);
        }
    }

    @Override
    public void takeTurn() {
        if(firstTurn){
            addToBot(new TalkAction(this, monsterStrings.DIALOG[0], 0.5F, 2.0F));
            firstTurn = false;
        }

        switch (this.nextMove){
            case 1:
                addToBot(new ChangeStateAction(this,"ATTACK_FAR"));
                addToBot(new LongWaitAction(this.waitTime));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                addToBot(new ApplyPowerAction(this,this,new MetallicizePower(this,AbstractDungeon.actNum>=3?4:2),AbstractDungeon.actNum>=3?4:2));
                break;
            case 2:
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(this.waitTime));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 3:
                addToBot(new ChangeStateAction(this,"SKILL"));
                addToBot(new LongWaitAction(this.waitTime));
                int blk = 8;
                AbstractPower p = getPower(MetallicizePower.POWER_ID);
                if(p!=null){
                    blk = Math.max(blk,p.amount);
                }
                addToBot(new GainBlockAction(this,blk));
                break;
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && animateParticles){
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if(this.fireTimer < 0F){
                this.fireTimer = 0.1F;
                AbstractDungeon.effectList.add(new FlkingEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY() - 20F*Settings.scale));
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
