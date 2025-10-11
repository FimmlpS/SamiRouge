package SamiRouge.ui;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.helper.DeclareHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public class CipherTextPreview {
    public static final float PAD_X = Settings.scale * 64F;

    public Hitbox hb;
    public AbstractCipherTextCard card;
    public int index;
    public float x;
    public float y;
    public float drawScale = 0.36F*Settings.scale;
    public float targetDrawScale;
    private TextureAtlas.AtlasRegion region;

    public CipherTextPreview(AbstractCipherTextCard card,int index) {
        this.card = card;
        this.index = index;
        region = card.portrait;
        hb = new Hitbox(64F*Settings.scale, 64F*Settings.scale);
    }

    //传入第一个preview所在位置的中心点
    public void update(float startX, float startY){
        x = startX + index * PAD_X;
        y = startY;
        hb.move(x,y);
        hb.update();
        targetDrawScale = 0.36F*Settings.scale;
        if(hb.hovered){
            targetDrawScale = 0.42F*Settings.scale;
        }
        this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);
    }

    public void render(SpriteBatch sb){
        sb.setColor(Color.WHITE);
        sb.draw(region,x-drawScale*region.getRegionWidth()/2F,y-drawScale*region.getRegionHeight()/2F,0,0,region.getRegionWidth(),region.getRegionHeight(),drawScale,drawScale,0);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, Integer.toString(card.remainX), x + 26.0F * Settings.scale, y - 14.0F * Settings.scale, Color.WHITE.cpy());
        if(hb.hovered){
            if ((float) InputHelper.mX < 1400.0F * Settings.scale) {
                TipHelper.renderGenericTip((float)InputHelper.mX + 60.0F * Settings.scale, (float)InputHelper.mY - 50.0F * Settings.scale, card.getPreviewTitle(),card.getPreviewDescription());
            } else {
                TipHelper.renderGenericTip((float)InputHelper.mX - 350.0F * Settings.scale, (float)InputHelper.mY - 50.0F * Settings.scale, card.getPreviewTitle(),card.getPreviewDescription());
            }
        }
    }
}
