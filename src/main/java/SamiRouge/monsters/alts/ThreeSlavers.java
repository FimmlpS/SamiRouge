package SamiRouge.monsters.alts;

import SamiRouge.actions.DelayActionAction;
import SamiRouge.actions.LongWaitAction;
import SamiRouge.monsters.AbstractSamiMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;


public class ThreeSlavers extends AbstractSamiMonster {
    public static final String ID = "samirg:ThreeSlavers";
    private static final MonsterStrings monsterStrings;

    boolean firstTurn = true;
    boolean usedEntangle = false;

    private int stabDmg = 12; // BLUE AND RED
    private int rakeDmg = 7;
    private int masterDmg = 4;
    private int scrapeDmg;

    private int weakAmt = 1;
    private int woundCount;
    private int vulAmt = 1;

    Random singleRng;


    public ThreeSlavers(float x,float y){
        super(monsterStrings.NAME,ID,160,0F,0F,600F,280F,null,x,y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 18) {
            woundCount = 3;
            weakAmt = 2;
            vulAmt = 2;
        } else {
            woundCount = 2;
            weakAmt = 1;
            vulAmt = 1;
        }

        if(AbstractDungeon.ascensionLevel>=8){
            this.setHp(176);
        }
        else{
            this.setHp(160);
        }

        if(AbstractDungeon.ascensionLevel>=2){
            this.stabDmg = 13;
            this.rakeDmg = 8;
            masterDmg = 7;
            this.scrapeDmg = 9;
        }
        else{
            this.stabDmg = 12;
            this.rakeDmg = 7;
            masterDmg = 4;
            this.scrapeDmg = 8;
        }
        firstTurn = true;

        //damage blue-0 1  master- 2 red-3 4
        damage.add(new DamageInfo(this,stabDmg));
        damage.add(new DamageInfo(this,rakeDmg));
        damage.add(new DamageInfo(this,masterDmg));
        damage.add(new DamageInfo(this,stabDmg));
        damage.add(new DamageInfo(this,scrapeDmg));
        powers.add(new WeakPower(this,3,false));

        singleRng = new Random(Settings.seed,AbstractDungeon.aiRng.counter);

        enableExtraIntent = true;
        intentOffsetSet = 200F * Settings.xScale;
        totalIntentOffSet = -50F * Settings.xScale;
        //spines 0-blue 1-master 2-red
        this.flipHorizontal = false;
        enableExtraSpine = true;
        ExtraSpine blue = new ExtraSpine(-200F,-4F)
                .loadAnimationSingle("images/monsters/theBottom/blueSlaver/skeleton.atlas", "images/monsters/theBottom/blueSlaver/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = blue.state.setAnimation(0,"idle",true);
        e.setTime(e.getEndTime() * MathUtils.random());
        extraSpines.add(blue);

        ExtraSpine master = new ExtraSpine(-10F,-8F)
                .loadAnimationSingle("images/monsters/theCity/slaverMaster/skeleton.atlas", "images/monsters/theCity/slaverMaster/skeleton.json", 1.0F);
        e = master.state.setAnimation(0,"idle",true);
        e.setTime(e.getEndTime() * MathUtils.random());
        extraSpines.add(master);

        ExtraSpine red = new ExtraSpine(200F,0F)
                .loadAnimationSingle("images/monsters/theBottom/redSlaver/skeleton.atlas", "images/monsters/theBottom/redSlaver/skeleton.json", 1.0F);
        e = red.state.setAnimation(0,"idle",true);
        e.setTime(e.getEndTime() * MathUtils.random());
        extraSpines.add(red);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(this,monsterStrings.DIALOG[0]));
        if(Settings.FAST_MODE){
            addToBot(new DelayActionAction(new LongWaitAction(1.9F),new TalkAction(this,monsterStrings.DIALOG[1],0.8F,1.5F)));
        }
        else{
            addToBot(new DelayActionAction(new TalkAction(this,monsterStrings.DIALOG[1],0.8F,1.5F)));
        }
        super.usePreBattleAction();
    }

    @Override
    protected void getMove(int i) {
        MultiMoveBuilder builder = new MultiMoveBuilder(this);
        //blue
        int num = singleRng.random(99);
        if (num >= 40 && !this.lastTwoMoves((byte)1,2)) {
            builder.addMove((byte)11, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        } else if (AbstractDungeon.ascensionLevel >= 17) {
            if (!this.lastMove((byte)14,2)) {
                builder.addMove((byte)14, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            } else {
                builder.addMove((byte)11, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
        } else if (!this.lastTwoMoves((byte)14,2)) {
            builder.addMove((byte)14, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
        } else {
            builder.addMove((byte)11, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
        //master
        builder.addMove((byte)22, Intent.ATTACK_DEBUFF, damage.get(2).base);
        //red
        num = singleRng.random(99);
        if(firstTurn){
            builder.addMove((byte)31, Intent.ATTACK, damage.get(3).base);
        } else if (num >= 75 && !this.usedEntangle) {
            builder.addMove((byte)32, Intent.STRONG_DEBUFF);
        } else if (num >= 55 && this.usedEntangle && !this.lastTwoMoves((byte)1,2)) {
            builder.addMove((byte)31, Intent.ATTACK, ((DamageInfo)this.damage.get(3)).base);
        } else if (AbstractDungeon.ascensionLevel >= 17) {
            if (!this.lastMove((byte)33,2)) {
                builder.addMove((byte)33, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(4)).base);
            } else {
                builder.addMove((byte)31, Intent.ATTACK, ((DamageInfo)this.damage.get(3)).base);
            }
        } else if (!this.lastTwoMoves((byte)33,2)) {
            builder.addMove((byte)33, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(4)).base);
        } else {
            builder.addMove((byte)31, Intent.ATTACK, ((DamageInfo)this.damage.get(3)).base);
        }
        builder.build();
    }

    @Override
    public void takeTurnForSingle(byte singleMove) {
        switch (singleMove){
            case 11:
                playBlueSfx();
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 14:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,weakAmt,true),weakAmt));
                break;
            case 22:
                playMasterSfx();
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                addToBot(new MakeTempCardInHandAction(new Wound(),woundCount));
                if(AbstractDungeon.ascensionLevel>=18){
                    addToBot(new ApplyPowerAction(this,this,new GainStrengthPower(this,1),1));
                }
                break;
            case 31:
                playRedSfx();
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player, damage.get(3), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 32:
                playRedSfx();
                addToBot(new ChangeStateAction(this,"Use Net"));
                if(this.hb!=null && AbstractDungeon.player.hb!=null){
                    addToBot(new VFXAction(new EntangleEffect(this.hb.cX + 70.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                }
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new EntanglePower(AbstractDungeon.player)));
                this.usedEntangle = true;
                break;
            case 33:
                addToBot(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(4), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new VulnerablePower(AbstractDungeon.player,vulAmt,true),vulAmt));
                break;
        }
    }

    @Override
    public void takeTurn() {
        if(firstTurn){
            firstTurn = false;
            addToBot(new TalkAction(this,monsterStrings.DIALOG[2]));
        }
        super.takeTurn();
    }

    @Override
    public void changeState(String stateName) {
        ExtraSpine red = extraSpines.get(2);
        float tmp = red.state.getCurrent(0).getTime();
        AnimationState.TrackEntry e = red.state.setAnimation(0,"idleNoNet",true);
        e.setTime(tmp);
    }

    private void playBlueSfx(){
        int roll = MathUtils.random(1);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLAVERBLUE_1A"));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLAVERBLUE_1B"));
        }
    }

    private void playMasterSfx(){
        int roll = MathUtils.random(1);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLAVERLEADER_1A"));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLAVERLEADER_1B"));
        }
    }

    private void playRedSfx(){
        int roll = MathUtils.random(1);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLAVERRED_1A"));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_SLAVERRED_1B"));
        }
    }

    private void playDeathSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_SLAVERBLUE_2A");
        } else {
            CardCrawlGame.sound.play("VO_SLAVERBLUE_2B");
        }
        roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_SLAVERLEADER_2A");
        } else {
            CardCrawlGame.sound.play("VO_SLAVERLEADER_2B");
        }
        roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_SLAVERRED_2A");
        } else {
            CardCrawlGame.sound.play("VO_SLAVERRED_2B");
        }
    }

    @Override
    public void die() {
        super.die();
        playDeathSfx();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}
