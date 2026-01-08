package SamiRouge.effects;

import SamiRouge.actions.LongWaitAction;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.helper.ImageHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DeclareEffect extends AbstractGameEffect {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Declare");

    private float maskAlpha = 0F;
    private float effectAlpha = 0F;
    private float togetherAlpha = 0F;
    private float textAlpha = 0F;
    private Color maskColor = new Color(1F,1F,1F,0F);
    private Color effectColor = new Color(1F,1F,1F,0F);
    private Color togetherColor = new Color(1F,1F,1F,0F);
    private Color textColor = new Color(1F,1F,1F,0F);

    float cipherOffsetY = 180F*Settings.scale;
    //transition
    float middleDiff = -100F * Settings.scale;
    float sideDiff01 = -100F * Settings.scale;
    float sideDiff02 = -200F * Settings.scale;
    float cipherDiff = 60F * Settings.scale;

    float textDiff = 400F * Settings.scale;
    float rapidX = 30F * Settings.scale;

    private TextureAtlas.AtlasRegion xgMiddle01;
    private TextureAtlas.AtlasRegion xgMiddle02;
    private TextureAtlas.AtlasRegion xgMiddle03;
    private TextureAtlas.AtlasRegion xgLeft01;
    private TextureAtlas.AtlasRegion xgLeft02;
    private TextureAtlas.AtlasRegion xgRight01;
    private TextureAtlas.AtlasRegion xgRight02;
    private TextureAtlas.AtlasRegion xgText;
    private TextureAtlas.AtlasRegion xgMask;
    private TextureAtlas.AtlasRegion xgCipherBg;

    private AbstractCipherTextCard layout;
    private AbstractCipherTextCard reason;

    public DeclareEffect() {
        //该构造函数无参数 仅作演示
        xgMiddle01 = ImageHelper.xgMiddle01;
        xgMiddle02 = ImageHelper.xgMiddle02;
        xgMiddle03 = ImageHelper.xgMiddle03;
        xgLeft01 = ImageHelper.xgLeft01;
        xgLeft02 = ImageHelper.xgLeft02;
        xgRight01 = ImageHelper.xgRight01;
        xgRight02 = ImageHelper.xgRight02;
        xgText = ImageHelper.xgText;
        xgMask = ImageHelper.xgMask;
        xgCipherBg = ImageHelper.xgCipherBg;

        this.duration = startingDuration = 2.4F;
    }

    public DeclareEffect(AbstractCipherTextCard layout, AbstractCipherTextCard reason){
        this();
        this.layout = layout;
        this.reason = reason;
        this.together = CipherText.isTogether(layout.cipherText, reason.cipherText);
        if(together){
            extraTime = 0.8F;
            startingDuration += extraTime;
            duration += extraTime;
            switch (reason.cipherText.color){
                case Group:
                    left = uiStrings.EXTRA_TEXT[1];
                    break;
                case Soul:
                    left = uiStrings.EXTRA_TEXT[2];
                    break;
                case Nature:
                    left = uiStrings.EXTRA_TEXT[3];
                    break;
                case Thefair:
                    left = uiStrings.EXTRA_TEXT[4];
                    break;
            }
            right = uiStrings.EXTRA_TEXT[5];
        }
        if(DeclareHelper.isBattle()){
            AbstractDungeon.actionManager.addToTop(new LongWaitAction(2F+extraTime));
        }
        DeclareHelper.declare(layout, reason);
    }

    //0~0.1s 黑幕渐变
    //0.1~0.3s 效果渐变
    //0.1~0.5s 效果位移
    //middle部分从-100F到0F
    //left和right的1部分从-100F到0F 2部分从-200F到0F
    //1.9~2.0s 黑幕及效果渐退

    private float mathUtil(float start, float end, float rate){
        return start + (end-start)*rate;
    }

    private float extraTime = 0F;
    private boolean together = false;
    private boolean musiced = false;
    private String left;
    private String right;

    @Override
    public void update() {
        if(!musiced){
            musiced = true;
            if(together){
                CardCrawlGame.sound.play("samirg:Together");
            }
            else {
                CardCrawlGame.sound.play("samirg:Declare");
            }
        }

        float delta = Gdx.graphics.getDeltaTime();
        this.duration -= delta;

        float gone = startingDuration - duration;
        //alpha
        if(gone <0.2F)
            maskAlpha = mathUtil(0F,1F,gone/0.2F);
        else if(gone < 2F+extraTime)
            maskAlpha = 1F;
        else if(gone >=2F+extraTime)
            maskAlpha = mathUtil(0F,1F,duration/0.4F);

        if(gone >=0.2F && gone <0.6F)
            effectAlpha = mathUtil(0f,1f,(gone-0.2F)/0.4F);
        else if(gone >= 0.6F && gone <2F+extraTime)
            effectAlpha = 1F;
        else if(gone >= 2F+extraTime)
            effectAlpha = mathUtil(0F,1F,duration/0.4F);

        if(!together){
            if(gone >=0.2F && gone <0.6F)
                textAlpha = mathUtil(0f,1f,(gone-0.2F)/0.4F);
            else if(gone >= 0.6F && gone <2F+extraTime)
                textAlpha = 1F;
            else if(gone >= 2F+extraTime)
                textAlpha = mathUtil(0F,1F,duration/0.4F);
        }
        else {
            if(gone >=0.2F && gone <0.4F)
                togetherAlpha = mathUtil(0f,1f,(gone-0.2F)/0.2F);
            else if(gone >= 0.4F && gone <1.5F)
                togetherAlpha = 1F;
            else if(gone >= 1.5F && gone <1.7F)
                togetherAlpha = mathUtil(1F,0F,(gone-1.5F)/0.2F);
            else if(gone >=1.7F){
                togetherAlpha = 0F;
            }

            if(gone >=1.7F && gone <1.9F)
                textAlpha = mathUtil(0f,1f,(gone-1.7F)/0.2F);
            else if(gone >= 1.9F && gone <2.2F+extraTime)
                textAlpha = 1F;
            else if(gone >= 2.2F+extraTime)
                textAlpha = mathUtil(0F,1F,duration/0.2F);
        }

        if(duration<=0F){
            maskAlpha = 0F;
            effectAlpha = 0F;
            togetherAlpha = 0F;
            textAlpha = 0F;
        }

        maskColor.a = maskAlpha;
        effectColor.a = effectAlpha;
        togetherColor.a = togetherAlpha;
        textColor.a = textAlpha;

        //transition
        if(gone >=0.1F && gone <0.9F){
            middleDiff = mathUtil(-100F*Settings.scale,-20F,(gone-0.1F)/0.8F);
            sideDiff01 = mathUtil(-100F*Settings.scale,-20F * Settings.scale,(gone-0.1F)/0.8F);
            sideDiff02 = mathUtil(-200F*Settings.scale,-40F * Settings.scale,(gone-0.1F)/0.8F);
            textDiff = mathUtil(260F*Settings.scale,220F*Settings.scale,(gone-0.1F)/0.8F);
            cipherDiff = mathUtil(60F*Settings.scale, 20F*Settings.scale, (gone-0.1F)/0.8F);
        }
        else if(gone >= 0.9F){
            middleDiff += rapidX *delta;
            sideDiff01 += rapidX *delta;
            sideDiff02 += rapidX *delta * 2F;
            textDiff -= rapidX *delta * 0.8F;
            cipherDiff -= rapidX *delta;
            if(cipherDiff<0F){
                cipherDiff = 0F;
            }
            rapidX -= 50F * delta;
            if(rapidX<0F){
                rapidX = 0F;
            }
        }

        if(duration<0F){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        float width = xgMiddle01.packedWidth/2F * Settings.scale;
        float height = xgMiddle01.packedHeight/2F * Settings.scale;
        float mX = Settings.WIDTH / 2.0F - width;
        float mY = Settings.HEIGHT / 2.0F - height;
        //mask
        sb.setColor(maskColor);
        sb.draw(xgMask,0F,0F,0F,0F,Settings.WIDTH,Settings.HEIGHT,Settings.scale,Settings.scale,0F);

        sb.setColor(effectColor);
        //middle 01-02
        sb.draw(xgMiddle01,mX,mY + middleDiff,0F,0F,xgMiddle01.packedWidth,xgMiddle01.packedHeight,Settings.scale,Settings.scale,0F);
        sb.draw(xgMiddle02,mX,mY - middleDiff,0F,0F,xgMiddle02.packedWidth,xgMiddle02.packedHeight,Settings.scale,Settings.scale,0F);

        //left
        sb.draw(xgLeft01,mX-sideDiff01,mY,0F,0F,xgLeft01.packedWidth,xgLeft01.packedHeight,Settings.scale,Settings.scale,0F);
        sb.draw(xgLeft02,mX-sideDiff02,mY,0F,0F,xgLeft02.packedWidth,xgLeft02.packedHeight,Settings.scale,Settings.scale,0F);
        //right
        sb.draw(xgRight01,mX+sideDiff01,mY,0F,0F,xgRight01.packedWidth,xgRight01.packedHeight,Settings.scale,Settings.scale,0F);
        sb.draw(xgRight02,mX+sideDiff02,mY,0F,0F,xgRight02.packedWidth,xgRight02.packedHeight,Settings.scale,Settings.scale,0F);

        //middle 03
        sb.draw(xgMiddle03,mX,mY,0F,0F,xgMiddle03.packedWidth,xgMiddle03.packedHeight,Settings.scale,Settings.scale,0F);

        //text
        sb.draw(xgText,mX,mY,0F,0F,xgText.packedWidth,xgText.packedHeight,Settings.scale,Settings.scale,0F);

        sb.setColor(textColor);
        FontHelper.cardTitleFont.getData().setScale(0.7F);
        if(layout == null || reason == null){
            float textWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont,uiStrings.EXTRA_TEXT[0],Float.MAX_VALUE,FontHelper.cardTitleFont.getLineHeight());
            //left text
            FontHelper.renderSmartText(sb,FontHelper.cardTitleFont,uiStrings.EXTRA_TEXT[0],mX -textWidth/2F + width - textDiff,mY + height + 6F*Settings.scale,textColor);
            //right text
            FontHelper.renderSmartText(sb,FontHelper.cardTitleFont,uiStrings.EXTRA_TEXT[1],mX -textWidth/2F + width + textDiff,mY + height + 6F*Settings.scale,textColor);
        }
        else {
            float textWidth = 0F;
            if(together){
                textWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont,right,Float.MAX_VALUE,FontHelper.cardTitleFont.getLineHeight());
                //left text
                FontHelper.renderSmartText(sb,FontHelper.cardTitleFont,left,mX -textWidth/2F + width - textDiff,mY + height + 6F*Settings.scale,togetherColor);
                //right text
                FontHelper.renderSmartText(sb,FontHelper.cardTitleFont,right,mX -textWidth/2F + width + textDiff,mY + height + 6F*Settings.scale,togetherColor);
            }

            textWidth = FontHelper.getSmartWidth(FontHelper.cardTitleFont,layout.cipherText.declareWord,Float.MAX_VALUE,FontHelper.cardTitleFont.getLineHeight());
            //left text
            FontHelper.renderSmartText(sb,FontHelper.cardTitleFont,layout.cipherText.declareWord,mX -textWidth/2F + width - textDiff,mY + height + 6F*Settings.scale,textColor);
            //right text
            FontHelper.renderSmartText(sb,FontHelper.cardTitleFont,reason.cipherText.declareWord,mX -textWidth/2F + width + textDiff,mY + height + 6F*Settings.scale,textColor);

            sb.setColor(effectColor);
            //cipher
            float scaleCipher = Settings.scale;
            float wCipher = xgCipherBg.packedWidth * scaleCipher;
            float hCipher = xgCipherBg.packedHeight * scaleCipher;
            sb.draw(xgCipherBg,mX+width-wCipher/2F,mY+height-hCipher/2F+cipherOffsetY+cipherDiff,0,0,xgCipherBg.packedWidth,xgCipherBg.packedHeight,scaleCipher,scaleCipher,0F);
            sb.draw(xgCipherBg, mX+width-wCipher/2F,mY+height-hCipher/2F-cipherOffsetY-cipherDiff,0,0,xgCipherBg.packedWidth,xgCipherBg.packedHeight,scaleCipher,scaleCipher,0F);

            scaleCipher = Settings.scale;
            wCipher = layout.portrait.packedWidth * scaleCipher;
            hCipher = layout.portrait.packedHeight * scaleCipher;
            sb.draw(layout.portrait,mX+width-wCipher/2F,mY+height-hCipher/2F+cipherOffsetY+cipherDiff,0,0,layout.portrait.packedWidth,layout.portrait.packedHeight,scaleCipher,scaleCipher,0F);
            sb.draw(reason.portrait, mX+width-wCipher/2F,mY+height-hCipher/2F-cipherOffsetY-cipherDiff,0,0,reason.portrait.packedWidth,reason.portrait.packedHeight,scaleCipher,scaleCipher,0F);

        }


    }

    @Override
    public void dispose() {

    }
}
