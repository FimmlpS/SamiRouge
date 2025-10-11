package SamiRouge.rooms;

import SamiRouge.events.LostAndFound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

public class LostAndFoundRoom extends AbstractRoom {
    public LostAndFoundRoom(){
        this.phase = AbstractRoom.RoomPhase.EVENT;
        this.mapSymbol = "?";
        this.mapImg = ImageMaster.loadImage("SamiRougeResources/img/icons/lostAndFound.png");
        this.mapImgOutline = ImageMaster.loadImage("SamiRougeResources/img/icons/lostAndFoundOutline.png");
    }

    public boolean renderThis = true;

    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
        this.event = new LostAndFound(eventRngDuplicate);

        this.event.onEnterRoom();
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp) {
            this.event.update();
        }

    }

    public void render(SpriteBatch sb) {
        if (this.event != null) {
            this.event.renderRoomEventPanel(sb);
            this.event.render(sb);
        }
        super.render(sb);
    }

    public void renderAboveTopPanel(SpriteBatch sb) {
        super.renderAboveTopPanel(sb);
        if (this.event != null) {
            this.event.renderAboveTopPanel(sb);
        }

    }

    @SpirePatch(clz = AbstractDungeon.class,method = "render")
    public static class RenderPatch{
        @SpireInsertPatch(rloc = 36)
        public static void Insert(AbstractDungeon _inst,SpriteBatch sb){
            if(AbstractDungeon.getCurrRoom() instanceof LostAndFoundRoom){
                AbstractDungeon.getCurrRoom().renderEventTexts(sb);
            }
        }
    }

    @SpirePatch(clz = ProceedButton.class,method = "update")
    public static class ProceedPatch{
        @SpireInsertPatch(rloc = 117)
        public static SpireReturn<Void> Insert(ProceedButton _inst){
            if(AbstractDungeon.getCurrRoom() instanceof LostAndFoundRoom){
                _inst.hide();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}


