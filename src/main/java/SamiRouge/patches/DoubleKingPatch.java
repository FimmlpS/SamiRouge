package SamiRouge.patches;

import SamiRouge.dungeons.TheSami;
import SamiRouge.relics.DimensionalFluidity;
import TreeHole.patches.TreeHolePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

public class DoubleKingPatch {

    public static boolean enableExtra(){
        return SamiTreeHolePatch.enterDoubleKing && !SamiTreeHolePatch.enteredDoubleKing;
    }

    //change true victory
    @SpirePatch(clz = TrueVictoryRoom.class,method = "onPlayerEntry")
    public static class TrueVictoryPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(TrueVictoryRoom _inst){
            boolean continueIt = false;
            if(AbstractDungeon.player.hasRelic(DimensionalFluidity.ID)){
                if(!SamiTreeHolePatch.enteredDoubleKing)
                    continueIt = true;
            }
            if(continueIt){
                SamiTreeHolePatch.enterDoubleKing = true;
                CardCrawlGame.stopClock = false;
                CardCrawlGame.music.fadeOutTempBGM();
                MapRoomNode node = new MapRoomNode(-1, 15);
                node.room = new TreasureRoomBoss();
                AbstractDungeon.nextRoom = node;
                AbstractDungeon.closeCurrentScreen();
                CardCrawlGame.dungeon.nextRoomTransition();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    //dungeon
    @SpirePatch(clz = TreasureRoomBoss.class,method = "getNextDungeonName")
    public static class TreasureBossPatch{
        @SpirePostfixPatch
        public static String Postfix(String _ret,TreasureRoomBoss _inst){
            if(SamiTreeHolePatch.enterDoubleKing){
                if(!SamiTreeHolePatch.enteredDoubleKing){
                    TreeHolePatch.currentType = 4;
                    return TheSami.ID;
                }
            }
            return _ret;
        }
    }

    //dungeon actlikeit
    @SpirePatch(clz = ProceedButton.class,method = "goToNextDungeon")
    public static class ProceedButtonTPatch{
        @SpirePostfixPatch
        public static void Postfix(ProceedButton _inst){
            if(SamiTreeHolePatch.enterDoubleKing){
                if(!SamiTreeHolePatch.enteredDoubleKing){
                    TreeHolePatch.currentType = 4;
                    CardCrawlGame.nextDungeon = TheSami.ID;
                }
            }
        }
    }
}
