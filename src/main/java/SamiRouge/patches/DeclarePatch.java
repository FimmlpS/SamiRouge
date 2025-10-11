package SamiRouge.patches;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.relics.DimensionalFluidity;
import SamiRouge.screens.DeclareScreen;
import SamiRouge.ui.CipherTextPreview;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

import java.util.ArrayList;

public class DeclarePatch {
    private static DeclarePatch Instance;
    private static final Color DISABLED_BTN_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.4F);
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Declare");

    public static DeclarePatch getInstance(){
        if(Instance == null){
            Instance = new DeclarePatch();
        }
        return Instance;
    }

    public DeclarePatch(){
        declareScreen = new DeclareScreen();
        buttonHb = new Hitbox(64F * Settings.scale, 64F * Settings.scale);
        buttonHb.move(64F * Settings.scale, 0F* Settings.scale + (Settings.isMobile ? (float)Settings.HEIGHT - 280.0F * Settings.scale : (float)Settings.HEIGHT - 250.0F * Settings.scale));
    }

    public DeclareScreen declareScreen;

    public Hitbox buttonHb;

    private boolean buttonDisabled = false;
    private float rotateTimer = 0F;
    private float buttonAngle = 0F;

    //每场战斗基础掉落2选1，精英战3选1，BOSS战掉落2组3选1；


    private ArrayList<CipherTextPreview> previews = new ArrayList<>();

    public void onBuffedChanged(){
        previews.clear();
        int index = 0;
        for(AbstractCipherTextCard c: DeclareHelper.buffed){
            CipherTextPreview preview = new CipherTextPreview(c,index);
            previews.add(preview);
            index++;
        }
    }


    private void updateButtonLogic(){
        if(AbstractDungeon.screen == EnumPatch.DECLARE_VIEW){
            rotateTimer += Gdx.graphics.getDeltaTime() * 4F;
            buttonAngle = MathHelper.angleLerpSnap(this.buttonAngle, MathUtils.sin(this.rotateTimer) * 15.0F);
        } else if (this.buttonHb.hovered) {
            this.buttonAngle = MathHelper.angleLerpSnap(this.buttonAngle, 15.0F);
        } else {
            this.buttonAngle = MathHelper.angleLerpSnap(this.buttonAngle, 0.0F);
        }

        if (AbstractDungeon.screen != EnumPatch.DECLARE_VIEW && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.COMBAT_REWARD && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DEATH && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.VICTORY && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.BOSS_REWARD && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SHOP && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.INPUT_SETTINGS && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.HAND_SELECT && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.CARD_REWARD) {
            this.buttonDisabled = true;
            this.buttonHb.hovered = false;
        } else {
            buttonHb.update();
        }

        boolean clickedButton = buttonHb.hovered && InputHelper.justClickedLeft;
        if(clickedButton && !CardCrawlGame.isPopupOpen){
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                AbstractDungeon.closeCurrentScreen();
                declareScreen.open();
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
            } else if (!AbstractDungeon.isScreenUp) {
                declareScreen.open();
            } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW){
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW;
                AbstractDungeon.closeCurrentScreen();
                declareScreen.open();
            } else if (AbstractDungeon.screen == EnumPatch.DECLARE_VIEW) {
                AbstractDungeon.screenSwap = false;
                if (AbstractDungeon.previousScreen == EnumPatch.DECLARE_VIEW) {
                    AbstractDungeon.previousScreen = null;
                }

                AbstractDungeon.closeCurrentScreen();
                CardCrawlGame.sound.play("DECK_CLOSE", 0.05F);
            } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.DEATH;
                AbstractDungeon.deathScreen.hide();
                declareScreen.open();
            } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.BOSS_REWARD) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.BOSS_REWARD;
                AbstractDungeon.bossRelicScreen.hide();
                declareScreen.open();
            } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.SHOP) {
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;
                declareScreen.open();
            } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !AbstractDungeon.dungeonMapScreen.dismissable) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.MAP;
                declareScreen.open();
            } else if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
                if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.INPUT_SETTINGS) {
                    if (AbstractDungeon.previousScreen != null) {
                        AbstractDungeon.screenSwap = true;
                    }

                    AbstractDungeon.closeCurrentScreen();
                    declareScreen.open();
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD) {
                    AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.CARD_REWARD;
                    AbstractDungeon.dynamicBanner.hide();
                    declareScreen.open();
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
                    AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.GRID;
                    AbstractDungeon.gridSelectScreen.hide();
                    declareScreen.open();
                } else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT) {
                    AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.HAND_SELECT;
                    declareScreen.open();
                }
            } else {
                if (AbstractDungeon.previousScreen != null) {
                    AbstractDungeon.screenSwap = true;
                }

                AbstractDungeon.closeCurrentScreen();
                declareScreen.open();
            }

            InputHelper.justClickedLeft = false;
        }

        for(CipherTextPreview preview : previews) {
            preview.update(buttonHb.cX + CipherTextPreview.PAD_X, buttonHb.cY);
        }
    }

    private void renderIcon(SpriteBatch sb){
        Color tmpColor = Color.WHITE.cpy();
        if(buttonDisabled){
            sb.setColor(DISABLED_BTN_COLOR);
            tmpColor = DISABLED_BTN_COLOR;
        }
        else if(buttonHb.hovered){
            sb.setColor(Color.CYAN);
            if(AbstractDungeon.screen != EnumPatch.DECLARE_VIEW){
                TipHelper.renderGenericTip(buttonHb.cX + 40F*Settings.scale,buttonHb.cY,uiStrings.TEXT[3],uiStrings.TEXT[4]);
            }
        }
        else {
            sb.setColor(tmpColor);
        }

        renderTheIcon(sb);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, Integer.toString(DeclareHelper.layout.size()), buttonHb.cX - 26.0F * Settings.scale, buttonHb.cY - 14.0F * Settings.scale, tmpColor);
        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont, Integer.toString(DeclareHelper.reason.size()), buttonHb.cX + 26.0F * Settings.scale, buttonHb.cY - 14.0F * Settings.scale, tmpColor);

        //展示目前已经生效的buff们
        for(CipherTextPreview preview:previews){
            preview.render(sb);
        }
    }

    private void renderTheIcon(SpriteBatch sb){
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.DECK_ICON, buttonHb.x, buttonHb.y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, this.buttonAngle, 0, 0, 64, 64, false, false);
        if (this.buttonHb.hovered) {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.25F));
            sb.draw(ImageMaster.DECK_ICON, buttonHb.x, buttonHb.y, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, this.buttonAngle, 0, 0, 64, 64, false, false);
            sb.setBlendFunction(770, 771);
        }

        this.buttonHb.render(sb);
    }

    @SpirePatch(clz= AbstractDungeon.class,method ="closeCurrentScreen")
    public static class CloseScreenPatch{
        @SpirePrefixPatch
        public static void Prefix(){
            if(getInstance().declareScreen!=null&&getInstance().declareScreen.opened){
                getInstance().declareScreen.opened = false;
                AbstractDungeon.overlayMenu.cancelButton.hide();
                ReflectionHacks.privateStaticMethod(AbstractDungeon.class,"genericScreenOverlayReset").invoke();
                for(AbstractCard c :DeclareHelper.layout){
                    c.unhover();
                    c.untip();
                }
                for(AbstractCard c :DeclareHelper.reason){
                    c.unhover();
                    c.untip();
                }
            }
        }
    }

    @SpirePatch(clz=AbstractDungeon.class,method = "update")
    public static class UpdatePatch{
        @SpireInsertPatch(rloc = 20)
        public static void Insert(){
            if(getInstance().declareScreen!=null && getInstance().declareScreen.opened)
                getInstance().declareScreen.update();
        }
    }

    @SpirePatch(clz=AbstractDungeon.class,method="render")
    public static class RenderPatch{
        @SpireInsertPatch(rloc = 45)
        public static void Insert(AbstractDungeon _inst, SpriteBatch sb){
            if(getInstance().declareScreen!=null && getInstance().declareScreen.opened)
                getInstance().declareScreen.render(sb);
            sb.setColor(Color.WHITE);
        }
    }

    @SpirePatch(clz = TopPanel.class,method = "unhoverHitboxes")
    public static class UnhoverHitboxesPatch{
        @SpirePrefixPatch
        public static void Prefix(TopPanel _inst){
            getInstance().buttonHb.unhover();
        }
    }

    @SpirePatch(clz = TopPanel.class,method = "updateButtons")
    public static class UpdateButtonsPatch{
        @SpirePrefixPatch
        public static void Prefix(TopPanel _inst){
            getInstance().updateButtonLogic();
        }

        @SpirePostfixPatch
        public static void Postfix(TopPanel _inst){
            if(getInstance().buttonHb.justHovered){
                CardCrawlGame.sound.play("UI_HOVER");
            }
        }
    }

    @SpirePatch(clz = TopPanel.class,method = "renderTopRightIcons")
    public static class RenderTopRightIconsPatch{
        @SpirePostfixPatch
        public static void Postfix(TopPanel _inst,SpriteBatch sb){
            getInstance().renderIcon(sb);
        }
    }

    //掉落
    @SpirePatch(clz = CombatRewardScreen.class,method = "setupItemReward")
    public static class SetUpCipherPatch{
        @SpirePostfixPatch
        public static void Postfix(CombatRewardScreen _inst){
            if ((AbstractDungeon.getCurrRoom().event == null || AbstractDungeon.getCurrRoom().event != null && !AbstractDungeon.getCurrRoom().event.noCardsInRewards) && !(AbstractDungeon.getCurrRoom() instanceof TreasureRoom) && !(AbstractDungeon.getCurrRoom() instanceof RestRoom)){
                int group = 1;
                int size = 2;
                if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite){
                    size = 3;
                }
                else if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
                    size = 3;
                    group = 2;
                }
                for(int i=0;i<group;i++){
                    RewardItem item = new RewardItem();
                    item.text = uiStrings.EXTRA_TEXT[0];
                    item.cards = new ArrayList<>(DeclareHelper.getRandomCipher(size));
                    _inst.rewards.add(item);
                }
                _inst.positionRewards();
            }
        }
    }

    //拾起
    @SpirePatch(clz = SoulGroup.class,method = "obtain")
    public static class CardRewardPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(SoulGroup _inst, AbstractCard card,boolean obtainCard){
            if(obtainCard && card instanceof AbstractCipherTextCard){
                CardCrawlGame.sound.play("CARD_OBTAIN");
                AbstractCipherTextCard c = (AbstractCipherTextCard)card;
                if(c.cipherText!=null && c.cipherText.type == CipherText.CipherType.Layout){
                    DeclareHelper.layout.add(c);
                }
                else if(c.cipherText!=null && c.cipherText.type == CipherText.CipherType.Reason){
                    DeclareHelper.reason.add(c);
                }
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

}
