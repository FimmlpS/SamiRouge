package SamiRouge.monsters.alts;

import SamiRouge.effects.FlkingEyeParticle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.EntangleEffect;

public class NamelessHero extends AbstractMonster {
    public static final String ID = "samirg:NamelessHero";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final int HP_MIN = 46;
    private static final int HP_MAX = 50;
    private static final int A_2_HP_MIN = 48;
    private static final int A_2_HP_MAX = 52;
    private static final int STAB_DMG = 13;
    private static final int A_2_STAB_DMG = 14;
    private static final int SCRAPE_DMG = 8;
    private static final int A_2_SCRAPE_DMG = 9;
    private int stabDmg;
    private int scrapeDmg;
    private int VULN_AMT = 1;
    private static final byte STAB = 1;
    private static final byte ENTANGLE = 2;
    private static final byte SCRAPE = 3;
    private static final String SCRAPE_NAME;
    private static final String ENTANGLE_NAME;
    private boolean usedEntangle = false;
    private boolean firstTurn = true;

    private Bone eye = null;
    private float fireTimer = 0F;
    private boolean animateParticles = true;

    public NamelessHero(float x, float y) {
        super(NAME, ID, 200, 0.0F, 0.0F, 225.0F, 270.0F, (String)null, x, y);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(198, 202);
        } else {
            this.setHp(180, 184);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.stabDmg = 14;
            this.scrapeDmg = 9;
        } else {
            this.stabDmg = 13;
            this.scrapeDmg = 8;
        }
        this.type = EnemyType.ELITE;
        this.damage.add(new DamageInfo(this, this.stabDmg));
        this.damage.add(new DamageInfo(this, this.scrapeDmg));
        this.loadAnimation("images/monsters/theBottom/redSlaver/skeleton.atlas", "images/monsters/theBottom/redSlaver/skeleton.json", 1F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.firstTurn = true;
        this.eye = this.skeleton.findBone("eye");
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new FeelNoPainPower(this,3),3));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new BeatOfDeathPower(this,2),2));
        if (AbstractDungeon.ascensionLevel >= 18) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngryPower(this, 1)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CuriosityPower(this, 2)));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngryPower(this, 1)));
            //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CuriosityPower(this, 1)));
        }
        super.usePreBattleAction();
    }

    public void takeTurn() {
        if(firstTurn){
            this.firstTurn = false;
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 3.0F));
            int level = Math.round(0.4F*this.maxHealth);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new InvinciblePower(this,level),level));
        }
        switch(this.nextMove) {
            case 1:
                this.playSfx();
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case 2:
                this.playSfx();
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "Use Net"));
                if (this.hb != null && AbstractDungeon.player.hb != null && !Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new EntangleEffect(this.hb.cX - 70.0F * Settings.scale, this.hb.cY + 10.0F * Settings.scale, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                }

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EntanglePower(AbstractDungeon.player)));
                this.usedEntangle = true;
                break;
            case 3:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.VULN_AMT + 1, true), this.VULN_AMT + 1));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.VULN_AMT, true), this.VULN_AMT));
                }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void playSfx() {
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
            CardCrawlGame.sound.play("VO_SLAVERRED_2A");
        } else {
            CardCrawlGame.sound.play("VO_SLAVERRED_2B");
        }

    }

    public void changeState(String stateName) {
        float tmp = this.state.getCurrent(0).getTime();
        AnimationState.TrackEntry e = this.state.setAnimation(0, "idleNoNet", true);
        e.setTime(tmp);
    }

    protected void getMove(int num) {
        if (this.firstTurn) {
            this.setMove((byte)1, Intent.ATTACK, this.stabDmg);
        } else if (num >= 75 && !this.usedEntangle) {
            this.setMove(ENTANGLE_NAME, (byte)2, Intent.STRONG_DEBUFF);
        } else if (num >= 55 && this.usedEntangle && !this.lastTwoMoves((byte)1)) {
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        } else if (AbstractDungeon.ascensionLevel >= 17) {
            if (!this.lastMove((byte)3)) {
                this.setMove(SCRAPE_NAME, (byte)3, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
            } else {
                this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
            }
        } else if (!this.lastTwoMoves((byte)3)) {
            this.setMove(SCRAPE_NAME, (byte)3, Intent.ATTACK_DEBUFF, ((DamageInfo)this.damage.get(1)).base);
        } else {
            this.setMove((byte)1, Intent.ATTACK, ((DamageInfo)this.damage.get(0)).base);
        }
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && animateParticles){
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if(this.fireTimer < 0F){
                this.fireTimer = 0.1F;
                if(!firstTurn)
                    AbstractDungeon.effectList.add(new FlkingEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY()));
                else{
                    AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.eye.getWorldX(), this.skeleton.getY() + this.eye.getWorldY()));
                }
            }
        }
    }

    public void die() {
        super.die();
        this.playDeathSfx();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
        SCRAPE_NAME = MOVES[0];
        ENTANGLE_NAME = MOVES[1];
    }
}
