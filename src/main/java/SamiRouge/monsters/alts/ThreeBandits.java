package SamiRouge.monsters.alts;

import SamiRouge.actions.DelayActionAction;
import SamiRouge.actions.LongWaitAction;
import SamiRouge.monsters.AbstractSamiMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ThreeBandits extends AbstractSamiMonster {
    public static final String ID = "samirg:ThreeBandits";
    private static final MonsterStrings monsterStrings;
    private float moveState = 2;

    int attackDmg = 5;
    int slashDmg = 15;
    int agonizeDmg = 10;
    int maulDmg = 20;
    int lungeDmg = 10;

    int weakAmount = 2;
    int con_reduction = -2;

    public ThreeBandits(float x,float y){
        super(monsterStrings.NAME,ID,111,0F,0F,600F,285F,null,x,y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.weakAmount = 3;
            con_reduction = -4;
        } else {
            this.weakAmount = 2;
            con_reduction = -2;
        }

        if(AbstractDungeon.ascensionLevel>=8){
            this.setHp(126);
        }
        else{
            this.setHp(111);
        }

        if(AbstractDungeon.ascensionLevel>=2){
            attackDmg = 6;
            this.slashDmg = 17;
            this.agonizeDmg = 12;
            this.maulDmg = 20;
            this.lungeDmg = 10;
        }
        else{
            attackDmg = 5;
            this.slashDmg = 15;
            this.agonizeDmg = 10;
            this.maulDmg = 18;
            this.lungeDmg = 9;
        }
        //pointy 0
        damage.add(new DamageInfo(this,attackDmg, DamageInfo.DamageType.NORMAL));
        //leader 1 2
        damage.add(new DamageInfo(this,slashDmg));
        damage.add(new DamageInfo(this,agonizeDmg));
        //bear 3 4
        damage.add(new DamageInfo(this,maulDmg));
        damage.add(new DamageInfo(this,lungeDmg));
        powers.add(new WeakPower(this,3,false));

        enableExtraIntent = true;
        intentOffsetSet = 200F * Settings.xScale;
        totalIntentOffSet = -50F * Settings.xScale;
        //spines 0-pointy 1-leader 2-bear
        this.flipHorizontal = true;
        enableExtraSpine = true;
        ExtraSpine pointy = new ExtraSpine(195F,-4F)
                .loadAnimationSingle("images/monsters/theCity/pointy/skeleton.atlas", "images/monsters/theCity/pointy/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = pointy.state.setAnimation(0,"Idle",true);
        e.setTime(e.getEndTime() * MathUtils.random());
        pointy.stateData.setMix("Hit", "Idle", 0.2F);
        pointy.state.setTimeScale(1F);
        extraSpines.add(pointy);

        ExtraSpine leader = new ExtraSpine(-10F,-7F)
                .loadAnimationSingle("images/monsters/theCity/romeo/skeleton.atlas", "images/monsters/theCity/romeo/skeleton.json", 1.0F);
        e = leader.state.setAnimation(0,"Idle",true);
        e.setTime(e.getEndTime() * MathUtils.random());
        leader.stateData.setMix("Hit", "Idle", 0.2F);
        leader.state.setTimeScale(0.8F);
        extraSpines.add(leader);

        ExtraSpine bear = new ExtraSpine(-205F,-4F)
                .loadAnimationSingle("images/monsters/theCity/bear/skeleton.atlas", "images/monsters/theCity/bear/skeleton.json", 1.0F);
        e = bear.state.setAnimation(0,"Idle",true);
        e.setTime(e.getEndTime() * MathUtils.random());
        bear.stateData.setMix("Hit", "Idle", 0.2F);
        bear.state.setTimeScale(1.0F);
        extraSpines.add(bear);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new SurroundedPower(AbstractDungeon.player)));
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
    public void takeTurnForSingle(byte singleMove) {
        switch (singleMove){
            case 11:
                addToBot(new ChangeStateAction(this,"POINTY_SLASH"));
                addToBot(new WaitAction(0.4F));
                addToBot(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                addToBot(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 21:
                addToBot(new ChangeStateAction(this,"LEADER_STAB"));
                addToBot(new WaitAction(0.5F));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case 22:
                addToBot(new TalkAction(this,monsterStrings.DIALOG[2]));
                break;
            case 23:
                addToBot(new ChangeStateAction(this,"LEADER_STAB"));
                addToBot(new WaitAction(0.5F));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new WeakPower(AbstractDungeon.player,weakAmount,true),weakAmount));
                break;
            case 31:
                addToBot(new ChangeStateAction(this,"BEAR_MAUL"));
                addToBot(new WaitAction(0.3F));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(3), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 32:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                addToBot(new ApplyPowerAction(AbstractDungeon.player,this,new DexterityPower(AbstractDungeon.player,con_reduction),con_reduction));
                break;
            case 33:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                addToBot(new DamageAction(AbstractDungeon.player,damage.get(4), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new GainBlockAction(this,this,12));
                break;
        }
    }

    @Override
    public void takeTurn() {
        if(moveState==2||moveState==1){
            moveState =3;
        }
        else{
            moveState =1;
        }
        super.takeTurn();
    }

    // pointy leader bear
    @Override
    protected void getMove(int i) {
        MultiMoveBuilder builder = new MultiMoveBuilder(this);
        if(moveState==2){
            builder.addMove((byte)32, Intent.STRONG_DEBUFF)
                    .addMove((byte)22, Intent.UNKNOWN)
                    .addMove((byte)11, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true)
                    .build();
        }
        else if(moveState==1){
            builder.addMove((byte)31, Intent.ATTACK, ((DamageInfo)this.damage.get(3)).base)
                    .addMove((byte)21, Intent.ATTACK, ((DamageInfo)this.damage.get(1)).base)
                    .addMove((byte)11, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true)
                    .build();
        }
        else {
            builder.addMove((byte)33, Intent.ATTACK_DEFEND, ((DamageInfo)this.damage.get(4)).base)
                    .addMove((byte)23, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(2)).base)
                    .addMove((byte)11, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base, 2, true)
                    .build();
        }
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName){
            case "POINTY_SLASH":
                ExtraSpine pointy = extraSpines.get(0);
                pointy.state.setAnimation(0,"Attack",false);
                pointy.state.addAnimation(0,"Idle",true,0F);
                break;
            case "LEADER_STAB":
                ExtraSpine leader = extraSpines.get(1);
                leader.state.setAnimation(0,"Attack",false);
                leader.state.addAnimation(0,"Idle",true,0F);
                break;
            case "BEAR_MAUL":
                ExtraSpine bear = extraSpines.get(2);
                bear.state.setAnimation(0,"Attack",false);
                bear.state.addAnimation(0,"Idle",true,0F);
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            ExtraSpine pointy = extraSpines.get(0);
            pointy.state.setAnimation(0, "Hit", false);
            pointy.state.setTimeScale(1.0F);
            pointy.state.addAnimation(0, "Idle", true, 0.0F);

            ExtraSpine leader = extraSpines.get(1);
            leader.state.setAnimation(0,"Hit",false);
            pointy.state.setTimeScale(0.8F);
            leader.state.addAnimation(0,"Idle",true,0F);

            ExtraSpine bear = extraSpines.get(2);
            bear.state.setAnimation(0, "Hit", false);
            bear.state.setTimeScale(1.0F);
            bear.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    }
}
