package SamiRouge.powers;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.monsters.AbstractSamiMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ShieldPower extends AbstractPower {
    public static final String POWER_ID = "samirg:ShieldPower";
    private static final PowerStrings powerStrings;

    AbstractSamiMonster.ExtraSpine spine;
    boolean isDying = false;

    public ShieldPower(AbstractCreature owner, int amt){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amt;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Shield_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Shield_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = false;
        spine = new AbstractSamiMonster.ExtraSpine(0F,0F);
        spine.loadAnimationSingleNoEnemy("SamiRougeResources/img/chars_SamiRouge/Npcsld/trap_024_npcsld.atlas","SamiRougeResources/img/chars_SamiRouge/Npcsld/trap_024_npcsld.json",1.75F);
        spine.state.setAnimation(0,"Idle",true);
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0]+this.amount+powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        int decrease = Math.min(damageAmount, this.amount);
        if (decrease > 0) {
            this.flash();
        }
        this.amount -= decrease;
        updateDescription();
        if (this.amount <= 0 && !isDying) {
            isDying = true;
            spine.state.setAnimation(0,"Die",false);
            addToBot(new LongWaitAction(1F));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        return damageAmount - decrease;
    }

    public void renderSpine(SpriteBatch sb){
        if(spine!=null&&spine.atlas!=null){
            spine.state.update(Gdx.graphics.getDeltaTime());
            spine.state.apply(spine.skeleton);
            spine.skeleton.updateWorldTransform();
            spine.skeleton.setPosition(owner.drawX + 0.8F*owner.hb.width, owner.drawY + 2F* Settings.scale);
            spine.skeleton.setColor(owner.tint.color);
            spine.skeleton.setFlip(owner.flipHorizontal, owner.flipVertical);
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractPlayer.sr.draw(CardCrawlGame.psb, spine.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        renderSpine(sb);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}


