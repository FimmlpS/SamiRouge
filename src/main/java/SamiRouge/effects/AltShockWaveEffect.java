package SamiRouge.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.BlurWaveAdditiveEffect;
import com.megacrit.cardcrawl.vfx.combat.BlurWaveChaoticEffect;
import com.megacrit.cardcrawl.vfx.combat.BlurWaveNormalEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class AltShockWaveEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private ShockWaveEffect.ShockWaveType type;
    private Color color;

    public AltShockWaveEffect(float x, float y, Color color, ShockWaveEffect.ShockWaveType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.color = color;
    }

    public void update() {
        float speed = -MathUtils.random(1000.0F, 1200.0F) * Settings.scale;
        int i;
        label32:
        switch(this.type) {
            case ADDITIVE:
                i = 0;

                while(true) {
                    if (i >= 40) {
                        break label32;
                    }

                    AbstractDungeon.effectsQueue.add(new BlurWaveAdditiveEffect(this.x, this.y, this.color.cpy(), speed));
                    ++i;
                }
            case NORMAL:
                i = 0;

                while(true) {
                    if (i >= 40) {
                        break label32;
                    }

                    AbstractDungeon.effectsQueue.add(new BlurWaveNormalEffect(this.x, this.y, this.color.cpy(), speed));
                    ++i;
                }
            case CHAOTIC:
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);

                for(i = 0; i < 40; ++i) {
                    AbstractDungeon.effectsQueue.add(new AltBlurWaveChaoticEffect(this.x, this.y, this.color.cpy(), speed));
                }
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }

    public static enum ShockWaveType {
        ADDITIVE,
        NORMAL,
        CHAOTIC;

        private ShockWaveType() {
        }
    }
}
