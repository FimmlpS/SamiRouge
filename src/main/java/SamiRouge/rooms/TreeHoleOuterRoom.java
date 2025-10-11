package SamiRouge.rooms;

import SamiRouge.events.TreeHoleEndEvent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class TreeHoleOuterRoom extends AbstractRoom {
    public TreeHoleOuterRoom(){
        this.phase = AbstractRoom.RoomPhase.EVENT;
        this.mapSymbol = "?";
        this.mapImg = ImageMaster.loadImage("SamiRougeResources/img/icons/treeHole.png");
        this.mapImgOutline = ImageMaster.loadImage("SamiRougeResources/img/icons/treeHoleOutline.png");
    }

    public boolean renderThis = true;

    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
        this.event = new TreeHoleEndEvent(eventRngDuplicate);

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
            if(AbstractDungeon.getCurrRoom() instanceof TreeHoleOuterRoom){
                AbstractDungeon.getCurrRoom().renderEventTexts(sb);
            }
        }
    }
}

