package SamiRouge.cards.ciphertext.layout;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.EmptyRoom;

import java.util.ArrayList;

public class C16 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C16";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C16() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],"").setData(this, CipherText.CipherType.Layout, CipherText.CipherColor.Nature).setTheX(0);
    }

    @Override
    public int declare(boolean together) {
        return getConnectedCount();
    }

    private int getConnectedCount(){
        MapRoomNode node = AbstractDungeon.getCurrMapNode();
        if(node != null && AbstractDungeon.map!=null){
            int count = 0;
            int y = node.y;
            //修复：在进入一幕前宣告
            if(y<0 && !AbstractDungeon.map.isEmpty()){
                ArrayList<MapRoomNode> nodes = AbstractDungeon.map.get(0);
                for(MapRoomNode n : nodes){
                    if(n!=node){
                        if(n.room !=null && !(n.room instanceof EmptyRoom)){
                            count++;
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
                                count++;
                            }
                        }
                    }
                }
                if(AbstractDungeon.map.size()>y+1){
                    ArrayList<MapRoomNode> nodes = AbstractDungeon.map.get(y+1);
                    for(MapRoomNode n : nodes){
                        if(n!=node){
                            if(!n.taken && node.isConnectedTo(n)){
                                count++;
                            }
                        }
                    }
                }
            }
            if(count<1)
                count = 1;
            return count;
        }
        return 1;
    }
}



