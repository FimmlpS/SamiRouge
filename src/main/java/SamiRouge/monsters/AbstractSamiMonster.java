package SamiRouge.monsters;

import SamiRouge.effects.FlashSingleIntentEffect;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashIntentEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractSamiMonster extends AbstractMonster {

    public AbstractSamiMonster(String name,String ID,int maxHealth,float hb_x,float hb_y,float hb_w,float hb_h,String imgUrl,float x, float y){
        super(name, ID,maxHealth,hb_x,hb_y,hb_w,hb_h,imgUrl,x,y);
        this.setMove((byte) -1,Intent.DEBUG);
    }

    public boolean enableExtraIntent = false;
    public ArrayList<EnemyMoveInfo> extraMoves = new ArrayList<>();
    public ArrayList<ExtraIntent> extraIntents = new ArrayList<>();
    public float intentOffsetSet = -1F;
    public float totalIntentOffSet = 0F;

    public boolean enableExtraSpine = false;
    public ArrayList<ExtraSpine> extraSpines = new ArrayList<>();

    @Override
    public void takeTurn() {
        if(enableExtraIntent){
            for(EnemyMoveInfo info:extraMoves){
                takeTurnForSingle(info.nextMove);
            }
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }
        else{
            takeTurnForSingle(this.nextMove);
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        }
    }

    public abstract void takeTurnForSingle(byte singleMove);

    @Override
    public void applyPowers() {
        super.applyPowers();
        for(int i =0;i<extraMoves.size()&&i<extraIntents.size();i++) {
            EnemyMoveInfo info = extraMoves.get(i);
            if (info.baseDamage > -1) {
                calculateDamage(info.baseDamage);
            }
            ExtraIntent intent = extraIntents.get(i);
            intent.intentDmg = getIntentDmg();
            intent.intentImg = intent.getIntentImg();
            updateSingleIntentTip(intent);
        }
    }

    @Override
    public void createIntent() {
        super.createIntent();
        createExtraIntent();
        updateIntentTip();
    }

    public void createExtraIntent(){
        int addAmt = extraMoves.size()-extraIntents.size();
        if(addAmt>0){
            for(int i =0;i<addAmt;i++){
                extraIntents.add(new ExtraIntent());
            }
        }
        else{
            for(int i =0;i<-addAmt;i++){
                if(extraIntents.size()>0)
                    extraIntents.remove(extraIntents.size()-1);
            }
        }
        int index = 0;
        for(EnemyMoveInfo move:extraMoves){
            ExtraIntent intent = extraIntents.get(index);
            intent.intent = move.intent;
            intent.intentBaseDmg = move.baseDamage;
            if (move.baseDamage > -1) {
                calculateDamage(intent.intentBaseDmg);
                intent.intentDmg = this.getIntentDmg();
                if (move.isMultiDamage) {
                    intent.intentMultiAmt = move.multiplier;
                    intent.isMultiDmg = true;
                } else {
                    intent.intentMultiAmt = -1;
                    intent.isMultiDmg = false;
                }
            }
            intent.intentImg = intent.getIntentImg();
            intent.intentBg = null;
            index++;
        }
        updateExtraIntent();
    }

    @SpireOverride
    protected void renderDamageRange(SpriteBatch sb){
        if(!enableExtraIntent){
            SpireSuper.call(sb);
        }
        else{
            Color intentColor = ReflectionHacks.getPrivate(this,AbstractMonster.class,"intentColor");
            BobEffect bobEffect = ReflectionHacks.getPrivate(this,AbstractMonster.class,"bobEffect");
            for(ExtraIntent intent:extraIntents){
                if (intent.intent.name().contains("ATTACK")) {
                    if (intent.isMultiDmg) {
                        FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(intent.intentDmg) + "x" + Integer.toString(intent.intentMultiAmt), intent.intentHb.cX - 30.0F * Settings.scale, intent.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, intentColor);
                    } else {
                        FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(intent.intentDmg), intent.intentHb.cX - 30.0F * Settings.scale, intent.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, intentColor);
                    }
                }
            }
        }
    }

    @SpireOverride
    protected void calculateDamage(int dmg){
        SpireSuper.call(dmg);
    }

    @Override
    public void flashIntent() {
        if(enableExtraIntent){
            for(ExtraIntent intent:extraIntents){
                if(intent.intentImg!=null){
                    AbstractDungeon.effectList.add(new FlashSingleIntentEffect(this,intent));
                }
            }
            this.intentAlphaTarget = 0F;
        }
        else{
            super.flashIntent();
        }
    }

    @SpireOverride
    protected void updateIntentTip(){
        SpireSuper.call();
        for(ExtraIntent intent:extraIntents){
            updateSingleIntentTip(intent);
        }
    }

    private void updateSingleIntentTip(ExtraIntent extraIntent){
        switch(extraIntent.intent) {
            case ATTACK:
                extraIntent.intentTip.header = TEXT[0];
                if (extraIntent.isMultiDmg) {
                    extraIntent.intentTip.body = TEXT[1] + extraIntent.intentDmg + TEXT[2] + extraIntent.intentMultiAmt + TEXT[3];
                } else {
                    extraIntent.intentTip.body = TEXT[4] + extraIntent.intentDmg + TEXT[5];
                }

                extraIntent.intentTip.img = extraIntent.getAttackIntentTip();
                break;
            case ATTACK_BUFF:
                extraIntent.intentTip.header = TEXT[6];
                if (extraIntent.isMultiDmg) {
                    extraIntent.intentTip.body = TEXT[7] + extraIntent.intentDmg + TEXT[2] + extraIntent.intentMultiAmt + TEXT[8];
                } else {
                    extraIntent.intentTip.body = TEXT[9] + extraIntent.intentDmg + TEXT[5];
                }

                extraIntent.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                break;
            case ATTACK_DEBUFF:
                extraIntent.intentTip.header = TEXT[10];
                extraIntent.intentTip.body = TEXT[11] + extraIntent.intentDmg + TEXT[5];
                extraIntent.intentTip.img = ImageMaster.INTENT_ATTACK_DEBUFF;
                break;
            case ATTACK_DEFEND:
                extraIntent.intentTip.header = TEXT[0];
                if (extraIntent.isMultiDmg) {
                    extraIntent.intentTip.body = TEXT[12] + extraIntent.intentDmg + TEXT[2] + extraIntent.intentMultiAmt + TEXT[3];
                } else {
                    extraIntent.intentTip.body = TEXT[12] + extraIntent.intentDmg + TEXT[5];
                }

                extraIntent.intentTip.img = ImageMaster.INTENT_ATTACK_DEFEND;
                break;
            case BUFF:
                extraIntent.intentTip.header = TEXT[10];
                extraIntent.intentTip.body = TEXT[19];
                extraIntent.intentTip.img = ImageMaster.INTENT_BUFF;
                break;
            case DEBUFF:
                extraIntent.intentTip.header = TEXT[10];
                extraIntent.intentTip.body = TEXT[20];
                extraIntent.intentTip.img = ImageMaster.INTENT_DEBUFF;
                break;
            case STRONG_DEBUFF:
                extraIntent.intentTip.header = TEXT[10];
                extraIntent.intentTip.body = TEXT[21];
                extraIntent.intentTip.img = ImageMaster.INTENT_DEBUFF2;
                break;
            case DEFEND:
                extraIntent.intentTip.header = TEXT[13];
                extraIntent.intentTip.body = TEXT[22];
                extraIntent.intentTip.img = ImageMaster.INTENT_DEFEND;
                break;
            case DEFEND_DEBUFF:
                extraIntent.intentTip.header = TEXT[13];
                extraIntent.intentTip.body = TEXT[23];
                extraIntent.intentTip.img = ImageMaster.INTENT_DEFEND;
                break;
            case DEFEND_BUFF:
                extraIntent.intentTip.header = TEXT[13];
                extraIntent.intentTip.body = TEXT[24];
                extraIntent.intentTip.img = ImageMaster.INTENT_DEFEND_BUFF;
                break;
            case ESCAPE:
                extraIntent.intentTip.header = TEXT[14];
                extraIntent.intentTip.body = TEXT[25];
                extraIntent.intentTip.img = ImageMaster.INTENT_ESCAPE;
                break;
            case MAGIC:
                extraIntent.intentTip.header = TEXT[15];
                extraIntent.intentTip.body = TEXT[26];
                extraIntent.intentTip.img = ImageMaster.INTENT_MAGIC;
                break;
            case SLEEP:
                extraIntent.intentTip.header = TEXT[16];
                extraIntent.intentTip.body = TEXT[27];
                extraIntent.intentTip.img = ImageMaster.INTENT_SLEEP;
                break;
            case STUN:
                extraIntent.intentTip.header = TEXT[17];
                extraIntent.intentTip.body = TEXT[28];
                extraIntent.intentTip.img = ImageMaster.INTENT_STUN;
                break;
            case UNKNOWN:
                extraIntent.intentTip.header = TEXT[18];
                extraIntent.intentTip.body = TEXT[29];
                extraIntent.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                break;
            case NONE:
                extraIntent.intentTip.header = "";
                extraIntent.intentTip.body = "";
                extraIntent.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                break;
            default:
                extraIntent.intentTip.header = "NOT SET";
                extraIntent.intentTip.body = "NOT SET";
                extraIntent.intentTip.img = ImageMaster.INTENT_UNKNOWN;
        }
    }

    @SpireOverride
    protected void updateIntent(){
        SpireSuper.call();
        for(ExtraIntent intent:extraIntents){
            Iterator<AbstractGameEffect> var1 = intent.intentVfx.iterator();
            while (var1.hasNext()){
                AbstractGameEffect e = var1.next();
                e.update();
                if(e.isDone){
                    var1.remove();
                }
            }
        }
    }

    private void updateExtraIntent(){
        if(extraIntents==null){
            extraIntents = new ArrayList<>();
        }
        int size = extraIntents.size();
        float singleOffset = ExtraIntent.OFFSET_X * (size>3?0.8F:1F);
        if(intentOffsetSet>0F)
            singleOffset = intentOffsetSet;
        float startX = -(int)((size-1)/2) *  ExtraIntent.OFFSET_X * (size>3?0.8F:1F) + totalIntentOffSet;
        if(size%2==0){
            startX -= singleOffset /2F;
        }
        for(int i =0;i<size;i++){
            ExtraIntent extraIntent = extraIntents.get(i);
            extraIntent.intentOffsetX = startX + i * singleOffset;
            extraIntent.intentHb.move(this.hb.cX + extraIntent.intentOffsetX, this.hb.cY + this.hb_h /2F + 32F * Settings.scale);
        }
    }

    @Override
    protected void updateHitbox(float hb_x, float hb_y, float hb_w, float hb_h) {
        super.updateHitbox(hb_x, hb_y, hb_w, hb_h);
        updateExtraIntent();
    }

    @Override
    public void refreshIntentHbLocation() {
        super.refreshIntentHbLocation();
        updateExtraIntent();
    }

    @SpireOverride
    protected void updateIntentVFX(){
        if(!enableExtraIntent){
            SpireSuper.call();
        }
        else{
            for(ExtraIntent intent:extraIntents){
                updateSingleIntentVFX(intent);
            }
        }
    }

    private void updateSingleIntentVFX(ExtraIntent extraIntent){
        if (this.intentAlpha > 0.0F) {
            if (extraIntent.intent != AbstractMonster.Intent.ATTACK_DEBUFF && extraIntent.intent != AbstractMonster.Intent.DEBUFF && extraIntent.intent != AbstractMonster.Intent.STRONG_DEBUFF && extraIntent.intent != AbstractMonster.Intent.DEFEND_DEBUFF) {
                if (extraIntent.intent != AbstractMonster.Intent.ATTACK_BUFF && extraIntent.intent != AbstractMonster.Intent.BUFF && extraIntent.intent != AbstractMonster.Intent.DEFEND_BUFF) {
                    if (extraIntent.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
                        extraIntent.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (extraIntent.intentParticleTimer < 0.0F) {
                            extraIntent.intentParticleTimer = 0.5F;
                            extraIntent.intentVfx.add(new ShieldParticleEffect(extraIntent.intentHb.cX, extraIntent.intentHb.cY));
                        }
                    } else if (extraIntent.intent == AbstractMonster.Intent.UNKNOWN) {
                        extraIntent.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (extraIntent.intentParticleTimer < 0.0F) {
                            extraIntent.intentParticleTimer = 0.5F;
                            extraIntent.intentVfx.add(new UnknownParticleEffect(extraIntent.intentHb.cX, extraIntent.intentHb.cY));
                        }
                    } else if (extraIntent.intent == AbstractMonster.Intent.STUN) {
                        extraIntent.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                        if (extraIntent.intentParticleTimer < 0.0F) {
                            extraIntent.intentParticleTimer = 0.67F;
                            extraIntent.intentVfx.add(new StunStarEffect(extraIntent.intentHb.cX, extraIntent.intentHb.cY));
                        }
                    }
                } else {
                    extraIntent.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                    if (extraIntent.intentParticleTimer < 0.0F) {
                        extraIntent.intentParticleTimer = 0.1F;
                        extraIntent.intentVfx.add(new BuffParticleEffect(extraIntent.intentHb.cX, extraIntent.intentHb.cY));
                    }
                }
            } else {
                extraIntent.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (extraIntent.intentParticleTimer < 0.0F) {
                    extraIntent.intentParticleTimer = 1.0F;
                    extraIntent.intentVfx.add(new DebuffParticleEffect(extraIntent.intentHb.cX, extraIntent.intentHb.cY));
                }
            }
        }
    }

    @SpireOverride
    protected void renderIntent(SpriteBatch sb) {
        if (!enableExtraIntent)
            SpireSuper.call(sb);
        else {
            for (ExtraIntent intent : extraIntents) {
                renderSingleIntent(intent, sb);
            }
        }
    }

    private void renderSingleIntent(ExtraIntent extraIntent,SpriteBatch sb){
        Color intentColor = ReflectionHacks.getPrivate(this,AbstractMonster.class,"intentColor");
        intentColor.a = intentAlpha;
        BobEffect bobEffect = ReflectionHacks.getPrivate(this,AbstractMonster.class,"bobEffect");
        if(extraIntent.intentBg!=null){
            sb.setColor(new Color(1F,1F,1F,intentAlpha/2F));
            if (Settings.isMobile) {
                sb.draw(extraIntent.intentBg, extraIntent.intentHb.cX - 64.0F, extraIntent.intentHb.cY - 64.0F + bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, 0.0F, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(extraIntent.intentBg, extraIntent.intentHb.cX - 64.0F, extraIntent.intentHb.cY - 64.0F + bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            }
        }
        if(extraIntent.intentImg!=null && extraIntent.intent != AbstractMonster.Intent.UNKNOWN && extraIntent.intent != AbstractMonster.Intent.STUN) {
            if (extraIntent.intent != AbstractMonster.Intent.DEBUFF && extraIntent.intent != AbstractMonster.Intent.STRONG_DEBUFF) {
                extraIntent.intentAngle = 0.0F;
            } else {
                extraIntent.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
            }

            sb.setColor(intentColor);
            if (Settings.isMobile) {
                sb.draw(extraIntent.intentImg, extraIntent.intentHb.cX - 64.0F, extraIntent.intentHb.cY - 64.0F + bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, extraIntent.intentAngle, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(extraIntent.intentImg, extraIntent.intentHb.cX - 64.0F, extraIntent.intentHb.cY - 64.0F + bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, extraIntent.intentAngle, 0, 0, 128, 128, false, false);
            }
        }
    }

    @SpireOverride
    protected void renderIntentVfxBehind(SpriteBatch sb){
        if(!enableExtraIntent)
            SpireSuper.call(sb);
        else{
            for(ExtraIntent intent:extraIntents){
                renderSingleIntentVfxBehind(intent,sb);
            }
        }
    }

    private void renderSingleIntentVfxBehind(ExtraIntent extraIntent, SpriteBatch sb){
        for (AbstractGameEffect e : extraIntent.intentVfx) {
            if (e.renderBehind) {
                e.render(sb);
            }
        }
    }

    @SpireOverride
    protected void renderIntentVfxAfter(SpriteBatch sb){
        if(!enableExtraIntent){
            SpireSuper.call(sb);
        }
        else{
            for(ExtraIntent intent:extraIntents){
                renderSingleIntentVfxAfter(intent,sb);
            }
        }
    }

    private void renderSingleIntentVfxAfter(ExtraIntent extraIntent,SpriteBatch sb){
        for (AbstractGameEffect e : extraIntent.intentVfx) {
            if (!e.renderBehind) {
                e.render(sb);
            }
        }
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        if(!enableExtraIntent)
            super.renderTip(sb);
        else {
            this.tips.clear();
            if (this.intentAlphaTarget == 1.0F && !AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != AbstractMonster.Intent.NONE) {
                for(ExtraIntent intent:extraIntents){
                    this.tips.add(intent.intentTip);
                }
            }

            for (AbstractPower p : this.powers) {
                if (p.region48 != null) {
                    this.tips.add(new PowerTip(p.name, p.description, p.region48));
                } else {
                    this.tips.add(new PowerTip(p.name, p.description, p.img));
                }
            }

            if (!this.tips.isEmpty()) {
                if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                    TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
                } else {
                    TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
                }
            }
        }
    }

    //Spine相关
    private void renderSingleSpine(ExtraSpine extraSpine, SpriteBatch sb){
        extraSpine.state.update(Gdx.graphics.getDeltaTime());
        extraSpine.state.apply(extraSpine.skeleton);
        extraSpine.skeleton.updateWorldTransform();
        extraSpine.skeleton.setPosition(this.drawX + this.animX + extraSpine.offsetX, this.drawY + this.animY + extraSpine.offsetY);
        extraSpine.skeleton.setColor(this.tint.color);
        extraSpine.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
        sb.end();
        CardCrawlGame.psb.begin();
        sr.draw(CardCrawlGame.psb, extraSpine.skeleton);
        CardCrawlGame.psb.end();
        sb.begin();
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void render(SpriteBatch sb) {
        if(enableExtraSpine){
            if(!this.isDead && !this.escaped){
                for(ExtraSpine extraSpine:extraSpines){
                    renderSingleSpine(extraSpine,sb);
                }
            }
        }
        super.render(sb);
    }

    //上一个行为（间隔式以兼容多意图）space取值为两个意图间的间隔值，如：3个意图，space应取2
    protected boolean lastMove(byte move,int space){
        if (this.moveHistory.size()<space+1) {
            return false;
        } else {
            return (Byte)this.moveHistory.get(this.moveHistory.size() - 1 - space) == move;
        }
    }

    protected boolean lastMoveBefore(byte move,int space){
        if (this.moveHistory.size()<space+2) {
            return false;
        } else {
            return (Byte)this.moveHistory.get(this.moveHistory.size() - 2 - space) == move;
        }
    }

    protected boolean lastTwoMoves(byte move,int space){
        if (this.moveHistory.size() < space+2) {
            return false;
        } else {
            return (Byte)this.moveHistory.get(this.moveHistory.size() - 1 - space) == move && (Byte)this.moveHistory.get(this.moveHistory.size() - 2 - space) == move;
        }
    }

    public static class MultiMoveBuilder{
        public ArrayList<EnemyMoveInfo> infos;
        AbstractSamiMonster monster;
        public MultiMoveBuilder(AbstractSamiMonster monster){
            infos = new ArrayList<>();
            this.monster = monster;
        }

        public MultiMoveBuilder setMoveName(String name){
            this.monster.moveName = name;
            return this;
        }

        public MultiMoveBuilder addMove(byte nextMove, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage){
            EnemyMoveInfo info = new EnemyMoveInfo(nextMove,intent,baseDamage,multiplier,isMultiDamage);
            infos.add(info);
            if(nextMove!= -1){
                monster.moveHistory.add(nextMove);
            }
            return this;
        }

        public MultiMoveBuilder addMove(byte nextMove, AbstractMonster.Intent intent, int baseDamage){
            return this.addMove(nextMove,intent,baseDamage,0,false);
        }

        public MultiMoveBuilder addMove(byte nextMove,  AbstractMonster.Intent intent){
            return addMove( nextMove, intent, -1, 0, false);
        }

        public MultiMoveBuilder removeLastHistory(){
            if(monster.moveHistory.size()>0){
                monster.moveHistory.remove(monster.moveHistory.size()-1);
            }
            return this;
        }

        public void build(){
            monster.extraMoves = infos;
            monster.createIntent();
        }
    }

    //单个额外意图
    public static class ExtraIntent{
        public static final float OFFSET_X = 140F* Settings.scale;
        public static final float INTENT_HB_W = 64F * Settings.scale;
        public Intent intent = Intent.DEBUG;
        public Texture intentImg = null;
        public Texture intentBg = null;
        public float intentAngle = 0F;
        public Hitbox intentHb;
        public float intentOffsetX = 0F;

        public float intentParticleTimer;
        public ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();

        public PowerTip intentTip;
        public int intentBaseDmg;
        public int intentDmg;
        public int intentMultiAmt;
        public boolean isMultiDmg;

        public ExtraIntent(){
            intentHb = new Hitbox(INTENT_HB_W,INTENT_HB_W);
            intentTip = new PowerTip();
        }

        public Texture getAttackIntentTip() {
            int tmp;
            if (this.isMultiDmg) {
                tmp = this.intentDmg * this.intentMultiAmt;
            } else {
                tmp = this.intentDmg;
            }

            if (tmp < 5) {
                return ImageMaster.INTENT_ATK_TIP_1;
            } else if (tmp < 10) {
                return ImageMaster.INTENT_ATK_TIP_2;
            } else if (tmp < 15) {
                return ImageMaster.INTENT_ATK_TIP_3;
            } else if (tmp < 20) {
                return ImageMaster.INTENT_ATK_TIP_4;
            } else if (tmp < 25) {
                return ImageMaster.INTENT_ATK_TIP_5;
            } else {
                return tmp < 30 ? ImageMaster.INTENT_ATK_TIP_6 : ImageMaster.INTENT_ATK_TIP_7;
            }
        }

        public Texture getIntentImg() {
            switch(this.intent) {
                case ATTACK:
                    return this.getAttackIntent();
                case ATTACK_BUFF:
                    return this.getAttackIntent();
                case ATTACK_DEBUFF:
                    return this.getAttackIntent();
                case ATTACK_DEFEND:
                    return this.getAttackIntent();
                case BUFF:
                    return ImageMaster.INTENT_BUFF_L;
                case DEBUFF:
                    return ImageMaster.INTENT_DEBUFF_L;
                case STRONG_DEBUFF:
                    return ImageMaster.INTENT_DEBUFF2_L;
                case DEFEND:
                    return ImageMaster.INTENT_DEFEND_L;
                case DEFEND_DEBUFF:
                    return ImageMaster.INTENT_DEFEND_L;
                case DEFEND_BUFF:
                    return ImageMaster.INTENT_DEFEND_BUFF_L;
                case ESCAPE:
                    return ImageMaster.INTENT_ESCAPE_L;
                case MAGIC:
                    return ImageMaster.INTENT_MAGIC_L;
                case SLEEP:
                    return ImageMaster.INTENT_SLEEP_L;
                case STUN:
                    return null;
                case UNKNOWN:
                    return ImageMaster.INTENT_UNKNOWN_L;
                default:
                    return ImageMaster.INTENT_UNKNOWN_L;
            }
        }

        public Texture getAttackIntent() {
            int tmp;
            if (this.isMultiDmg) {
                tmp = this.intentDmg * this.intentMultiAmt;
            } else {
                tmp = this.intentDmg;
            }

            if (tmp < 5) {
                return ImageMaster.INTENT_ATK_1;
            } else if (tmp < 10) {
                return ImageMaster.INTENT_ATK_2;
            } else if (tmp < 15) {
                return ImageMaster.INTENT_ATK_3;
            } else if (tmp < 20) {
                return ImageMaster.INTENT_ATK_4;
            } else if (tmp < 25) {
                return ImageMaster.INTENT_ATK_5;
            } else {
                return tmp < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
            }
        }

        public Texture getAttackIntent(int dmg) {
            if (dmg < 5) {
                return ImageMaster.INTENT_ATK_1;
            } else if (dmg < 10) {
                return ImageMaster.INTENT_ATK_2;
            } else if (dmg < 15) {
                return ImageMaster.INTENT_ATK_3;
            } else if (dmg < 20) {
                return ImageMaster.INTENT_ATK_4;
            } else if (dmg < 25) {
                return ImageMaster.INTENT_ATK_5;
            } else {
                return dmg < 30 ? ImageMaster.INTENT_ATK_6 : ImageMaster.INTENT_ATK_7;
            }
        }
    }

    //单Spine额外渲染
    public static class ExtraSpine{
        public TextureAtlas atlas;
        public Skeleton skeleton;
        public AnimationStateData stateData;
        public AnimationState state;

        public float offsetX;
        public float offsetY;

        public ExtraSpine(float offsetX, float offsetY){
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public ExtraSpine loadAnimationSingleNoEnemy(String atlasUrl, String skeletonUrl, float scale){
            this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
            SkeletonJson json = new SkeletonJson(this.atlas);
            json.setScale(Settings.renderScale / scale);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
            this.skeleton = new Skeleton(skeletonData);
            this.skeleton.setColor(Color.WHITE);
            this.stateData = new AnimationStateData(skeletonData);
            this.state = new AnimationState(this.stateData);
            return this;
        }

        public ExtraSpine loadAnimationSingle(String atlasUrl, String skeletonUrl, float scale){
            this.atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
            SkeletonJson json = new SkeletonJson(this.atlas);
            if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hasRelic("PreservedInsect") && AbstractDungeon.getCurrRoom().eliteTrigger) {
                    scale += 0.3F;
                }

                if (ModHelper.isModEnabled("MonsterHunter")) {
                    scale -= 0.3F;
                }
            }

            json.setScale(Settings.renderScale / scale);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
            this.skeleton = new Skeleton(skeletonData);
            this.skeleton.setColor(Color.WHITE);
            this.stateData = new AnimationStateData(skeletonData);
            this.state = new AnimationState(this.stateData);
            return this;
        }
    }

}
