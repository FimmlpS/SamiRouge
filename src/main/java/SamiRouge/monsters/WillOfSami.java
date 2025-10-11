package SamiRouge.monsters;

import SamiRouge.actions.ResetColdnessAction;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.powers.ColdnessPower;
import SamiRouge.samiMod.SamiRougeHelper;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class WillOfSami extends CustomMonster {
    public static final String ID = "samirg:WillOfSami";
    public static final String ICON = "SamiRougeResources/img/monsters_SamiRouge/Smdeer/WillOfSami_Icon.png";
    public static final String ICON_O = "SamiRougeResources/img/monsters_SamiRouge/Smdeer/WillOfSami_Icon_O.png";
    private static final MonsterStrings monsterStrings;
    private int iceThornDMG = 6;
    private int dazeCount = 3;
    private int stepAddDMG = 3;
    private int stepRound = 3;
    private int blockCount = 12;
    private boolean firstTurn = true;
    private boolean firstTurn2 = false;
    private boolean usedSami = false;
    boolean more;

    public WillOfSami(float x,float y){
        super(monsterStrings.NAME, ID,400,-10F,-30F,676F,510F,null,x,y);
        if(AbstractDungeon.ascensionLevel>=9){
            this.setHp(450);
        }
        else
            this.setHp(400);

        if(AbstractDungeon.ascensionLevel>=19){
            this.dazeCount = 4;
            more = true;
        }
        else{
            this.dazeCount = 3;
            more = false;
        }

        if(AbstractDungeon.ascensionLevel>=4){
            this.iceThornDMG = 6;
            this.stepRound = 4;
            this.stepAddDMG = 6;
            this.blockCount = 16;
        }
        else{
            this.iceThornDMG = 5;
            this.stepRound = 3;
            this.stepAddDMG = 3;
            this.blockCount = 12;
        }

        this.loadAnimation("SamiRougeResources/img/monsters_SamiRouge/Smdeer/enemy_2054_smdeer.atlas","SamiRougeResources/img/monsters_SamiRouge/Smdeer/enemy_2054_smdeer.json",1.5F);

        this.state.setAnimation(0,"Idle",true);

        this.type = EnemyType.BOSS;
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.damage.add(new DamageInfo(this,iceThornDMG, DamageInfo.DamageType.NORMAL));
        this.damage.add(new DamageInfo(this,stepAddDMG, DamageInfo.DamageType.NORMAL));

    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        SamiRougeHelper.BOSS_KEY = "WillOfSami";
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        SamiRougeHelper.BOSS_KEY = "";
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new ColdnessPower(this,more?32:16)));
    }

    @Override
    public void takeTurn() {
        if(this.firstTurn){
            if(AbstractDungeon.player.name == "Typhon")
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, monsterStrings.DIALOG[2], 0.5F, 2.0F));
            else{
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, monsterStrings.DIALOG[0], 0.5F, 2.0F));
            }
        }
        this.firstTurn = false;
        this.firstTurn2 = false;

        label32:
        switch (this.nextMove){
            case 2:
                int i =0;
                while(true){
                    if(i>=6){
                        addToBot(new WaitAction(0.3F));
                        addToBot(new MakeTempCardInHandAction(new Dazed(),this.dazeCount));
                        break label32;
                    }
                    addToBot(new VFXAction(this,new ShockWaveEffect(this.hb.cX,this.hb.cY,Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC),0.3F));
                    addToBot(new DamageAction(AbstractDungeon.player,this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    i++;
                }
            case 3:
                addToBot(new ChangeStateAction(this, "ATTACK"));
                addToBot(new WaitAction(0.4F));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractPower coldness = this.getPower("samirg:ColdnessPower");
                if(coldness!=null){
                    addToBot(new ResetColdnessAction((ColdnessPower) coldness,-1));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new ColdnessPower(this,32)));
                }
                break ;
            case 4:
                addToBot(new GainBlockAction(this,this.usedSami?2*this.blockCount:this.blockCount));
                if(AbstractDungeon.ascensionLevel>=19)
                    addToBot(new ApplyPowerAction(this,this,new BufferPower(this,2),2));
                break ;
            case 5:
                dazeCount+=1;
                firstTurn2 = true;
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, monsterStrings.DIALOG[1], 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                if(more){
                    addToBot(new HealAction(this,this,(int)(0.3F*maxHealth)));
                }
                AbstractPower coldness2 = this.getPower("samirg:ColdnessPower");
                if(coldness2!=null){
                    addToBot(new ResetColdnessAction((ColdnessPower) coldness2,more?64:32));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new ColdnessPower(this,more?64:32)));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,99,true),99));
                break ;
        }

        //行动后第二阶段伤害++
        stepAddDMG += stepRound;
        stepRound++;
        this.damage.get(1).base = stepAddDMG;

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "ATTACK":{
                this.state.setAnimation(0,"Attack",false);
                this.state.addAnimation(0,"Idle",true,0.0F);
                break;
            }
        }
    }

    @Override
    protected void getMove(int i) {
        int triggerHealth = more?((int)(maxHealth*0.6F)):maxHealth/2;
        if(!firstTurn&&this.currentHealth< triggerHealth &&!this.usedSami){
            //仅一次，低于 (60) 50％生命后下一次意图为强化
            this.usedSami = true;
            this.setMove((byte)5,Intent.BUFF);
        }
        else if(usedSami&&(firstTurn2||(i<40&&!lastMove((byte)3)))){
            //仅二阶段时：第一回合必定触发，40％概率，不会连续触发2次
            this.setMove((byte)3,Intent.ATTACK_BUFF,this.damage.get(1).base,1,false);
        }
        else if(!firstTurn&&((i<70&&!lastTwoMoves((byte) 2))||lastMove((byte) 4))){
            //70％概率，不会连续触发3次，但会在强化后一定触发
            this.setMove((byte)2,Intent.ATTACK_DEBUFF,this.damage.get(0).base,6,true);
        }
        else{
            //强化自身
            this.setMove((byte)4,Intent.DEFEND_BUFF);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            super.die();
            SamiTreeHolePatch.toTalkWithMountains = true;
            this.onBossVictoryLogic();
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}

