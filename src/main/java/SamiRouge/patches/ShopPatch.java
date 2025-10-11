package SamiRouge.patches;

import SamiRouge.blights.AntiInterference;
import SamiRouge.helper.ImageHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.relics.MembershipCard;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;

public class ShopPatch {

    public static boolean deviceFixBought = false;
    public static float deviceX;
    public static float deviceY;
    public static int deviceCost;
    public static float deviceScale;
    public static Hitbox deviceHitbox = new Hitbox(180F*Settings.scale,180F*Settings.scale);

    private static final float GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    private static final UIStrings uiStrings;


    @SpirePatch(clz = ShopScreen.class,method = "init")
    public static class ShopInitPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen _inst, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            deviceFixBought = false;
            deviceX = Settings.WIDTH * 0.12F;
            deviceY = (float)Settings.HEIGHT / 2.0F - 540.0F * Settings.yScale + 760.0F * Settings.yScale;
            deviceScale = 0.5F;
            deviceCost = 50;
            //the courier
            if(AbstractDungeon.player.hasRelic(Courier.ID)){
                deviceCost = MathUtils.round((float) deviceCost*0.8F);
            }
            //card
            if(AbstractDungeon.player.hasRelic(MembershipCard.ID)){
                deviceCost = MathUtils.round((float) deviceCost*0.5F);
            }
            deviceHitbox.move(deviceX,deviceY);
        }
    }

    @SpirePatch(clz = ShopScreen.class,method = "updateCards")
    public static class ShopUpdateCardsPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen _inst) {
            if(deviceFixBought)
                return;
            deviceHitbox.encapsulatedUpdate( new InputSettingsScreen(){
                @Override
                public void clicked(Hitbox hb) {
                    if(AbstractDungeon.player.gold>=deviceCost){
                        //商人的鼠鼠无限续杯
                        if(!AbstractDungeon.player.hasRelic(Courier.ID))
                            deviceFixBought = true;
                        CardCrawlGame.metricData.addShopPurchaseData(uiStrings.TEXT[0]);
                        AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                        if(anti instanceof AntiInterference){
                            ((AntiInterference) anti).buyOne();
                        }
                        else{
                            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new AntiInterference());
                        }
                        AbstractDungeon.player.loseGold(deviceCost);
                        CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);

                    }
                    else{
                        _inst.playCantBuySfx();
                        _inst.createSpeech(ShopScreen.getCantBuyMsg());
                    }
                }
            });
            updateScale();
        }
    }

    @SpirePatch(clz = ShopScreen.class,method = "renderCardsAndPrices")
    public static class ShopRenderPatch {
        @SpirePostfixPatch
        public static void Postfix(ShopScreen _inst, SpriteBatch sb) {
            if(deviceFixBought)
                return;
            sb.setColor(Color.WHITE);
            sb.draw(ImageHelper.deviceFix,deviceX - ImageHelper.deviceFix.packedWidth/2F * Settings.scale,deviceY - ImageHelper.deviceFix.packedHeight/2F * Settings.scale,0F,0F,ImageHelper.deviceFix.packedWidth,ImageHelper.deviceFix.packedHeight,deviceScale,deviceScale,0F);
            sb.draw(ImageMaster.UI_GOLD, deviceX + -50.0F * Settings.scale, deviceY + -165.0F * Settings.scale - (0.7F - 0.75F) * 200.0F * Settings.scale, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color color = Color.WHITE.cpy();
            if(deviceCost> AbstractDungeon.player.gold){
                color = Color.SALMON.cpy();
            }
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(deviceCost), deviceX + -50.0F * Settings.scale, deviceY + -165.0F * Settings.scale - (0.7F - 0.75F) * 200.0F * Settings.scale, color);

            if(deviceHitbox.hovered){
                TipHelper.renderGenericTip(deviceX + 100F*Settings.scale,deviceY + 80F * Settings.scale,uiStrings.TEXT[0],uiStrings.TEXT[1]);
            }
        }
    }

    private static void updateScale(){
        if(deviceHitbox.hovered){
            deviceScale = 1.2F;
        }
        else if(deviceScale>1F){
            deviceScale -= Gdx.graphics.getDeltaTime();
            if(deviceScale < 1.0F){
                deviceScale = 1.0F;
            }
        }
        else if(deviceScale<1F){
            deviceScale += Gdx.graphics.getDeltaTime();
            if(deviceScale > 1.0F){
                deviceScale = 1.0F;
            }
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("samirg:AntiInterference");
    }
}
