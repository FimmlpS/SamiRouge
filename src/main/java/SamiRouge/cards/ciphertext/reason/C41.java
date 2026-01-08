package SamiRouge.cards.ciphertext.reason;

import SamiRouge.blights.AntiInterference;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import TreeHole.mod.TreeHoleHelper;
import TreeHole.patches.TreeHolePatch;
import TreeHole.room.TreeHoleEnterRoom;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class C41 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C41";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C41() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Nature);
    }

    @Override
    public void declared(int theX, boolean together) {
        this.remainX = 0;
        this.together = together;
        if(together){
            for(int i =0;i<theX;i++){
                AbstractBlight anti = AbstractDungeon.player.getBlight(AntiInterference.ID);
                if(anti instanceof AntiInterference){
                    ((AntiInterference) anti).buyOne();
                }
                else{
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new AntiInterference());
                }
            }
        }

        if(AbstractDungeon.id != null){
            if(TreeHoleHelper.contains(AbstractDungeon.id)){
                return;
            }
        }

        MapRoomNode node = AbstractDungeon.getCurrMapNode();
        if(node != null){
            int y = node.y;
            ArrayList<MapRoomNode> tmp = new ArrayList<>();
            if(y<0 && !AbstractDungeon.map.isEmpty()){
                ArrayList<MapRoomNode> nodes = AbstractDungeon.map.get(0);
                for(MapRoomNode n : nodes){
                    if(n!=node){
                        if(canChangeNode(n.room)){
                            tmp.add(n);
                        }
                    }
                }
            }
            else {
                if(AbstractDungeon.map.size()>y){
                    ArrayList<MapRoomNode> nodes = AbstractDungeon.map.get(y);
                    for(MapRoomNode n : nodes){
                        if(n!=node){
                            if(!n.taken && node.isConnectedTo(n)){
                                if(canChangeNode(n.room))
                                    tmp.add(n);
                            }
                        }
                    }
                }
                if(AbstractDungeon.map.size()>y+1){
                    ArrayList<MapRoomNode> nodes = AbstractDungeon.map.get(y+1);
                    for(MapRoomNode n : nodes){
                        if(n!=node){
                            if(!n.taken && node.isConnectedTo(n)){
                                if(canChangeNode(n.room))
                                    tmp.add(n);
                            }
                        }
                    }
                }
            }
            Collections.shuffle(tmp,new Random(AbstractDungeon.miscRng.randomLong()));
            if(!tmp.isEmpty()){
                int remainChange = theX;
                for(MapRoomNode n : tmp){
                    if(remainChange>0){
                        remainChange--;
                        int rX = n.x;
                        int rY = n.y;
                        n.room = new TreeHoleEnterRoom();
                        TreeHolePatch.spawnX.add(rX);
                        TreeHolePatch.spawnY.add(rY);
                        TreeHolePatch.entered.add(false);
                    }
                    else {
                        break;
                    }
                }
            }
        }
    }

    private boolean canChangeNode(AbstractRoom room){
        if(room instanceof MonsterRoom && !(room instanceof MonsterRoomBoss))
            return true;
        if(room instanceof EventRoom)
            return true;
        if(room instanceof TreasureRoom)
            return true;
        if(room instanceof RestRoom)
            return true;
        if(room instanceof ShopRoom)
            return true;
        return false;
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}





