package SamiRouge.screens;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.effects.DeclareEffect;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.helper.ImageHelper;
import SamiRouge.patches.EnumPatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;

import java.util.ArrayList;
import java.util.Collections;

public class DeclareScreen {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private static final int CARDS_PER_LINE = 2;
    private static final float SCROLL_BAR_THRESHOLD;
    private static final String HEADER_INFO;

    //declareButton
    TextureAtlas.AtlasRegion buttonImg = ImageHelper.xgDeclare;
    Hitbox buttonHb;
    Color buttonColor = new Color(1,1,1,0);
    float buttonAlpha = 0F;
    float buttonTargetScale = 1F*Settings.scale;
    float buttonScale = 1F*Settings.scale;
    boolean canDeclare = false;

    private boolean grabbedScreenLeft = false;
    private boolean grabbedScreenRight = false;
    private static float drawStartXLeft;
    private static float drawStartXRight;
    private static float drawStartYLeft;
    private static float drawStartYRight;
    private static float padX;
    private static float padY;
    private float scrollLowerBoundLeft;
    private float scrollLowerBoundRight;
    private float scrollUpperBoundLeft;
    private float scrollUpperBoundRight;
    private float grabStartYLeft;
    private float grabStartYRight;
    private float currentDiffYLeft;
    private float currentDiffYRight;
    private int prevDeckSizeLeft;
    private int prevDeckSizeRight;
    private ScrollBar scrollBarLeft;
    private ScrollBar scrollBarRight;
    public boolean opened;

    private AbstractCipherTextCard hoveredCard;
    private AbstractCipherTextCard hoveredCard2;
    private AbstractCipherTextCard selectedCardLeft;
    private AbstractCipherTextCard selectedCardRight;

    private Layout layout = new Layout();
    private Reason reason = new Reason();

    public DeclareScreen() {
        this.scrollLowerBoundLeft = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollLowerBoundRight = -Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBoundLeft = Settings.DEFAULT_SCROLL_LIMIT;
        this.scrollUpperBoundRight = Settings.DEFAULT_SCROLL_LIMIT;
        this.grabStartYLeft = this.scrollLowerBoundLeft;
        this.grabStartYRight = this.scrollLowerBoundRight;
        this.currentDiffYLeft = this.scrollLowerBoundLeft;
        this.currentDiffYRight = this.scrollLowerBoundRight;

        this.hoveredCard = null;
        this.hoveredCard2 = null;
        this.selectedCardLeft = null;
        this.selectedCardRight = null;
        this.prevDeckSizeLeft = 0;
        this.prevDeckSizeRight = 0;

        drawStartXLeft = (float)Settings.WIDTH;
        drawStartXLeft -= 5.0F * AbstractCard.IMG_WIDTH * 0.75F;
        drawStartXLeft -= 4.0F * Settings.CARD_VIEW_PAD_X;
        drawStartXLeft /= 2.0F;
        drawStartXLeft += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;
        drawStartXRight = drawStartXLeft + 3* (AbstractCard.IMG_WIDTH*0.75F + Settings.CARD_VIEW_PAD_X);
        padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        this.scrollBarLeft = new ScrollBar(layout,150F*Settings.scale - ScrollBar.TRACK_W/2F,(float)Settings.HEIGHT / 2.0F, (float)Settings.HEIGHT - 256.0F * Settings.scale);
        this.scrollBarRight = new ScrollBar(reason,(float)Settings.WIDTH - 150.0F * Settings.scale - ScrollBar.TRACK_W / 2.0F, (float)Settings.HEIGHT / 2.0F, (float)Settings.HEIGHT - 256.0F * Settings.scale);

        buttonHb = new Hitbox(300F * Settings.scale,300F * Settings.scale);
        buttonHb.move(Settings.WIDTH/2F,Settings.HEIGHT/2F);
    }

    public void update() {
        if (AbstractDungeon.player == null) {
            this.opened = false;
        } else {
            boolean isDraggingScrollBar = false;
            boolean leftSide = InputHelper.mX < Settings.WIDTH/2F;
            if (leftSide && shouldShowScrollBarLeft()) {
                isDraggingScrollBar = this.scrollBarLeft.update();
            }
            else if(!leftSide && shouldShowScrollBarRight()){
                isDraggingScrollBar = this.scrollBarRight.update();
            }

            if (!isDraggingScrollBar) {
                this.updateScrolling();
            }

            this.updatePositions();

            this.buttonScale = MathHelper.cardScaleLerpSnap(this.buttonScale, this.buttonTargetScale);

            canDeclare = false;
            if(opened && selectedCardLeft != null && selectedCardRight != null && selectedCardLeft.canDeclare(selectedCardRight.cipherText)) {
                canDeclare = true;
            }

            if(canDeclare){
                buttonAlpha += Gdx.graphics.getDeltaTime() * 5F;
                if(buttonAlpha>1F)
                    buttonAlpha = 1F;
                buttonColor.a = buttonAlpha;
                buttonHb.update();
                if(buttonHb.hovered){
                    buttonTargetScale = 1.2F*Settings.scale;
                }
                else {
                    buttonTargetScale = 1F*Settings.scale;
                }
                if(buttonHb.justHovered){
                    CardCrawlGame.sound.play("UI_HOVER");
                }
                if(buttonHb.hovered && InputHelper.justClickedLeft){
                    AbstractDungeon.topLevelEffects.add(new DeclareEffect(selectedCardLeft,selectedCardRight));
                    AbstractDungeon.closeCurrentScreen();
                }
            }
            else {
                buttonAlpha -= Gdx.graphics.getDeltaTime() * 5F;
                if(buttonAlpha<0F)
                    buttonAlpha = 0F;
                buttonColor.a = buttonAlpha;
            }
        }
    }

    private void updatePositions(){
        boolean changedSelect = false;
        this.hoveredCard = null;
        //left
        int lineNum = 0;
        int lineIndex = 0;
        ArrayList<AbstractCipherTextCard> cards = DeclareHelper.layout;
        for(AbstractCipherTextCard card : cards){
            card.target_x = drawStartXLeft + lineIndex*padX;
            card.target_y = drawStartYLeft + currentDiffYLeft - lineNum*padY;
            //card.targetDrawScale = 0.75F;
            card.update();
            if(AbstractDungeon.topPanel.potionUi.isHidden){
                card.updateHoverLogic();
                if(card.hb.hovered){
                    this.hoveredCard = card;
                }
            }

            lineIndex++;
            if(lineIndex==2){
                lineIndex = 0;
                lineNum++;
            }
        }

        if(hoveredCard != null && InputHelper.justClickedLeft){
            changedSelect = true;
            CardCrawlGame.sound.play("CARD_SELECT");
            if(selectedCardLeft == hoveredCard){
                selectedCardLeft = null;
                hoveredCard.stopGlowing();
            }
            else {
                if(selectedCardLeft!=null)
                    selectedCardLeft.stopGlowing();
                selectedCardLeft = hoveredCard;
                hoveredCard.glowColor = Color.GOLDENROD.cpy();
                hoveredCard.beginGlowing();
            }
            if(InputHelper.justReleasedClickLeft && hoveredCard == selectedCardLeft){
                InputHelper.justReleasedClickLeft = false;
            }
        }

        //right
        this.hoveredCard2 = null;
        lineNum = 0;
        lineIndex = 0;
        cards = DeclareHelper.reason;
        for(AbstractCipherTextCard card : cards){
            card.target_x = drawStartXRight + lineIndex*padX;
            card.target_y = drawStartYRight + currentDiffYRight - lineNum*padY;
            //card.targetDrawScale = 0.75F;
            card.update();
            if(AbstractDungeon.topPanel.potionUi.isHidden){
                card.updateHoverLogic();
                if(card.hb.hovered){
                    this.hoveredCard2 = card;
                }
            }


            lineIndex++;
            if(lineIndex==2){
                lineIndex = 0;
                lineNum++;
            }
        }

        if(hoveredCard2 != null && InputHelper.justClickedLeft){
            changedSelect = true;
            CardCrawlGame.sound.play("CARD_SELECT");
            if(selectedCardRight == hoveredCard2){
                selectedCardRight = null;
                hoveredCard2.stopGlowing();
            }
            else {
                if(selectedCardRight!=null)
                    selectedCardRight.stopGlowing();
                selectedCardRight = hoveredCard2;
                hoveredCard2.glowColor = Color.GOLDENROD.cpy();
                hoveredCard2.beginGlowing();
            }
            if(InputHelper.justReleasedClickLeft && hoveredCard2 == selectedCardRight){
                InputHelper.justReleasedClickLeft = false;
            }
        }

        //glowing处理
        if(changedSelect){
            if(selectedCardLeft != null && selectedCardRight == null){
                for(AbstractCipherTextCard card : DeclareHelper.layout){
                    if(card != selectedCardLeft)
                        card.stopGlowing();
                }
                for(AbstractCipherTextCard card:DeclareHelper.reason){
                    if(CipherText.isTogether(selectedCardLeft.cipherText,card.cipherText)){
                        card.glowColor = Color.WHITE.cpy();
                        card.beginGlowing();
                    }
                    else {
                        card.stopGlowing();
                    }
                }
            }
            else if(selectedCardRight != null && selectedCardLeft == null){
                for(AbstractCipherTextCard card:DeclareHelper.layout){
                    if(CipherText.isTogether(card.cipherText,selectedCardRight.cipherText)){
                        card.glowColor = Color.WHITE.cpy();
                        card.beginGlowing();
                    }
                    else {
                        card.stopGlowing();
                    }
                }
                for(AbstractCipherTextCard card:DeclareHelper.reason){
                    if(card != selectedCardRight)
                        card.stopGlowing();
                }
            }
            else {
                for(AbstractCipherTextCard card:DeclareHelper.layout){
                    if(card != selectedCardLeft)
                        card.stopGlowing();
                }
                for(AbstractCipherTextCard card:DeclareHelper.reason){
                    if(card != selectedCardRight)
                        card.stopGlowing();
                }
            }
        }
    }

    private void updateScrolling() {
        int y = InputHelper.mY;
        boolean leftSide = InputHelper.mX < Settings.WIDTH / 2F;
        if (leftSide) {
            if (!this.grabbedScreenLeft) {
                if (InputHelper.scrolledDown) {
                    this.currentDiffYLeft += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.currentDiffYLeft -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    this.grabbedScreenLeft = true;
                    this.grabStartYLeft = (float) y - this.currentDiffYLeft;
                }
            } else if (InputHelper.isMouseDown) {
                this.currentDiffYLeft = (float) y - this.grabStartYLeft;
            } else {
                this.grabbedScreenLeft = false;
            }
        } else {
            if (!this.grabbedScreenRight) {
                if (InputHelper.scrolledDown) {
                    this.currentDiffYRight += Settings.SCROLL_SPEED;
                } else if (InputHelper.scrolledUp) {
                    this.currentDiffYRight -= Settings.SCROLL_SPEED;
                }

                if (InputHelper.justClickedLeft) {
                    this.grabbedScreenRight = true;
                    this.grabStartYRight = (float) y - this.currentDiffYRight;
                }
            } else if (InputHelper.isMouseDown) {
                this.currentDiffYRight = (float) y - this.grabStartYRight;
            } else {
                this.grabbedScreenRight = false;
            }
        }

        if(this.prevDeckSizeLeft != DeclareHelper.layout.size()){
            this.calculateScrollBoundsLeft();
        }
        if(this.prevDeckSizeRight != DeclareHelper.reason.size()){
            this.calculateScrollBoundsRight();
        }

        this.resetScrolling();
        this.updateBarPosition();
    }

    private void calculateScrollBoundsLeft() {
        int scrollTmp = getTotalLineLeft();
        if(scrollTmp>2){
            scrollTmp -= 2;
            this.scrollUpperBoundLeft = Settings.DEFAULT_SCROLL_LIMIT + (float)scrollTmp * padY;
        }
        else{
            this.scrollUpperBoundLeft = Settings.DEFAULT_SCROLL_LIMIT;
        }
        this.prevDeckSizeLeft = DeclareHelper.layout.size();
    }

    private void calculateScrollBoundsRight() {
        int scrollTmp = getTotalLineRight();
        if(scrollTmp>2){
            scrollTmp -= 2;
            this.scrollUpperBoundRight = Settings.DEFAULT_SCROLL_LIMIT + (float)scrollTmp * padY;
        }
        else{
            this.scrollUpperBoundRight = Settings.DEFAULT_SCROLL_LIMIT;
        }
        this.prevDeckSizeLeft = DeclareHelper.reason.size();
    }

    public boolean shouldShowScrollBarLeft(){
        return this.scrollUpperBoundLeft > SCROLL_BAR_THRESHOLD;
    }

    public boolean shouldShowScrollBarRight(){
        return this.scrollUpperBoundRight > SCROLL_BAR_THRESHOLD;
    }

    private int getTotalLineLeft(){
        int line = 0;
        line = (DeclareHelper.layout.size()+1)/2;
        return line;
    }

    private int getTotalLineRight(){
        int line = 0;
        line = (DeclareHelper.reason.size()+1)/2;
        return line;
    }

    private void resetScrolling(){
        if (this.currentDiffYLeft < this.scrollLowerBoundLeft) {
            this.currentDiffYLeft = MathHelper.scrollSnapLerpSpeed(this.currentDiffYLeft, this.scrollLowerBoundLeft);
        } else if (this.currentDiffYLeft > this.scrollUpperBoundLeft) {
            this.currentDiffYLeft = MathHelper.scrollSnapLerpSpeed(this.currentDiffYLeft, this.scrollUpperBoundLeft);
        }

        if (this.currentDiffYRight < this.scrollLowerBoundRight) {
            this.currentDiffYRight = MathHelper.scrollSnapLerpSpeed(this.currentDiffYRight, this.scrollLowerBoundRight);
        } else if (this.currentDiffYRight > this.scrollUpperBoundRight) {
            this.currentDiffYRight = MathHelper.scrollSnapLerpSpeed(this.currentDiffYRight, this.scrollUpperBoundRight);
        }
    }

    private void updateBarPosition(){
        float percent = MathHelper.percentFromValueBetween(scrollLowerBoundLeft,scrollUpperBoundLeft,currentDiffYLeft);
        scrollBarLeft.parentScrolledToPercent(percent);
        percent = MathHelper.percentFromValueBetween(scrollLowerBoundRight,scrollUpperBoundRight,currentDiffYRight);
        scrollBarRight.parentScrolledToPercent(percent);
    }

    public void open(){
        AbstractDungeon.player.releaseCard();
        CardCrawlGame.sound.play("DECK_OPEN");
        this.currentDiffYLeft = this.scrollLowerBoundLeft;
        this.grabStartYLeft = this.scrollLowerBoundLeft;
        this.currentDiffYRight = this.scrollLowerBoundRight;
        this.grabStartYRight = this.scrollLowerBoundRight;
        this.grabbedScreenLeft = false;
        this.grabbedScreenRight = false;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = EnumPatch.DECLARE_VIEW;
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[2]);
        drawStartYLeft = (float)Settings.HEIGHT * 0.66F;
        drawStartYRight = (float)Settings.HEIGHT * 0.66F;

        selectedCardLeft = null;
        selectedCardRight = null;
        buttonAlpha = 0F;

        //sort
        Collections.sort(DeclareHelper.layout);
        Collections.sort(DeclareHelper.reason);
        for(AbstractCard c : DeclareHelper.layout){
            c.stopGlowing();
            c.glowColor = Color.WHITE.cpy();
        }
        for(AbstractCard c : DeclareHelper.reason){
            c.stopGlowing();
            c.glowColor = Color.WHITE.cpy();
        }

        this.calculateScrollBoundsLeft();
        this.calculateScrollBoundsRight();
        this.opened = true;
    }

    public void render(SpriteBatch sb){
        if(shouldShowScrollBarLeft()){
            scrollBarLeft.render(sb);
        }
        if(shouldShowScrollBarRight()){
            scrollBarRight.render(sb);
        }
        sb.setColor(buttonColor);
        sb.draw(buttonImg,buttonHb.cX-buttonScale*buttonImg.packedWidth/2F,buttonHb.cY-buttonScale*buttonImg.packedHeight/2F,0,0,buttonImg.packedWidth,buttonImg.packedHeight,buttonScale,buttonScale,0F);

        sb.setColor(Color.WHITE.cpy());
        for(AbstractCard c : DeclareHelper.layout){
            if(c != hoveredCard){
                c.render(sb);
            }
        }
        if(hoveredCard!=null){
            hoveredCard.renderHoverShadow(sb);
            hoveredCard.render(sb);
            hoveredCard.renderCardTip(sb);
        }

        for(AbstractCard c : DeclareHelper.reason){
            if(c != hoveredCard2){
                c.render(sb);
            }
        }
        if(hoveredCard2!=null){
            hoveredCard2.renderHoverShadow(sb);
            hoveredCard2.render(sb);
            hoveredCard2.renderCardTip(sb);
        }
        sb.setColor(Color.WHITE.cpy());
        FontHelper.renderDeckViewTip(sb, HEADER_INFO, 96.0F * Settings.scale, Settings.CREAM_COLOR);
    }


    public class Layout implements ScrollBarListener {
        @Override
        public void scrolledUsingBar(float newPercent) {
            currentDiffYLeft = MathHelper.valueFromPercentBetween(scrollLowerBoundLeft, scrollUpperBoundLeft, newPercent);
            updateBarPosition();
        }
    }

    public class Reason implements ScrollBarListener {
        @Override
        public void scrolledUsingBar(float newPercent) {
            currentDiffYRight = MathHelper.valueFromPercentBetween(scrollLowerBoundRight, scrollUpperBoundRight, newPercent);
            updateBarPosition();
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Declare");
        TEXT = uiStrings.TEXT;
        SCROLL_BAR_THRESHOLD = 500F* Settings.scale;
        HEADER_INFO = TEXT[1];
    }
}
