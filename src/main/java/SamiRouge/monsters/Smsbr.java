package SamiRouge.monsters;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.actions.SamiDroneMoveAction;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.save.SamiTreeHoleSave;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AngryPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;

public class Smsbr extends AbstractMonster {
    public static final String ID = "samirg:Smsbr";
    private static final MonsterStrings monsterStrings;

    private int baseDamage;
    private float wait = 0.5F;
    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;

    public Smsbr(float x,float y) {
        super(monsterStrings.NAME,ID,70,0F,0F,180F,180F,null,x,y);
        int hp = 26 + SamiTreeHolePatch.treeHoleEntered*4;
        if(AbstractDungeon.ascensionLevel>=7){
            hp = 26 + SamiTreeHolePatch.treeHoleEntered*6;
        }
        setHp(hp);
        baseDamage = 4 + SamiTreeHolePatch.treeHoleEntered;
        if(AbstractDungeon.ascensionLevel>=2){
            baseDamage = 6 + SamiTreeHolePatch.treeHoleEntered;
        }

        initializeSpine();
        this.type = EnemyType.NORMAL;
        this.damage.add(new DamageInfo(this,this.baseDamage, DamageInfo.DamageType.NORMAL));
        this.powers.add(new AngryPower(this, AbstractDungeon.ascensionLevel>=17?2:1));
    }

    public void initializeSpine(){
        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smsbr/enemy_2043_smsbr.atlas","SamiRougeResources/img/monsters_SamiRouge/Smsbr/enemy_2043_smsbr.json",1.6F);
        this.flipHorizontal = true;
        this.state.setAnimation(0,"Idle",true);
        this.eye = this.skeleton.findBone("Head");
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove){
            case 1:{
                addToBot(new ChangeStateAction(this,"ATTACK"));
                addToBot(new LongWaitAction(wait));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractPower str = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
                if(str == null || str.amount>0){
                    addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new StrengthPower(AbstractDungeon.player,SamiTreeHolePatch.treeHoleEntered>=6?-2:-1),SamiTreeHolePatch.treeHoleEntered>=6?-2:-1));
                }
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
                AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY() + 10F* Settings.scale));
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
