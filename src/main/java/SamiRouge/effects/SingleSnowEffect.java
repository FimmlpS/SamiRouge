package SamiRouge.effects;

import SamiRouge.helper.ImageHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SingleSnowEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private float ySpeed;
    private float xSpeed;
    private TextureAtlas.AtlasRegion img;

    float scaleX;
    float scaleY;
    float a;
    float startA;

    public SingleSnowEffect(float x,float dX){
        this.duration = MathUtils.random(1.6F,2.1F);
        if(AbstractDungeon.actNum==2)
            this.duration -= 0.2F;
        else if(AbstractDungeon.actNum>=3){
            this.duration -= 0.4F;
        }
        startingDuration = duration;
        int index = MathUtils.random(0, 3);
        if(index >=1 )
            index--;
        if(index == 0)
            img = ImageHelper.snow01;
        else if(index == 1)
            img = ImageHelper.snow02;
        else
            img = ImageHelper.snow03;
        this.x = x;
        this.y = Settings.HEIGHT + 32F*Settings.yScale;
        this.targetX = x + dX;
        this.targetY = -32F*Settings.yScale;
        xSpeed = dX/duration;
        ySpeed = (64F*Settings.yScale + Settings.HEIGHT)/duration;
        scaleX = MathUtils.random(0.7F,1.1F);
        scaleY = MathUtils.random(0.8F,1.2F);
        a = MathUtils.random(0.25F,0.42F);
        startA = a;
        this.color = new Color(1,1,1,a);
    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        this.ySpeed += delta*0.2F;
        this.y -= delta*ySpeed;
        this.x += delta*xSpeed;
        this.duration -= delta;
        this.a = Math.max(duration/startingDuration,0.2F) * startA;
        this.color.a = a;
        if(duration <0F){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770,1);
        sb.setColor(this.color);
        sb.draw(this.img,this.x,this.y,(float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight,scaleX,scaleY,0);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}

