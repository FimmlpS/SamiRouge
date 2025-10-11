package SamiRouge.rooms;

import SamiRouge.events.*;
import SamiRouge.patches.EventPatch;
import TreeHole.mod.TreeHoleHelper;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class THEventRoom extends EventRoom {

    @Override
    public void onPlayerEntry() {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        // (3,4) 节点100％
        boolean samiEvent = true;
        //其他节点40％概率
        if(!(AbstractDungeon.getCurrMapNode().x == 3 && AbstractDungeon.getCurrMapNode().y==4))
            samiEvent = AbstractDungeon.eventRng.random(0,99)<25;
        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
        if(TreeHoleHelper.getCurrentType()!=4){
            ArrayList<String> tmp = new ArrayList<>(EventPatch.specialEvents);
            if(samiEvent){
                if(!tmp.isEmpty()){
                    Logger.getGlobal().info("===== "+tmp.get(0)+" =====");
                    Collections.shuffle(tmp,eventRngDuplicate.random);
                    if(AbstractDungeon.actNum==1 && tmp.get(0) == Change.ID && tmp.size()>1)
                        tmp.remove(0);
                    switch (tmp.get(0)) {
                        case BeforeWoodCrack.ID:
                            this.event = new BeforeWoodCrack();
                            break;
                        case SadCage.ID:
                            this.event = new SadCage();
                            break;
                        case NoMountains.ID:
                            this.event = new NoMountains();
                            break;
                        case Change.ID:
                            this.event = new Change();
                            break;
                        case FarSee.ID:
                            this.event = new FarSee();
                            break;
                        case DarkRoom.ID:
                            this.event = new DarkRoom();
                            break;
                        case Rain.ID:
                            this.event = new Rain();
                            break;
                        case CureRitual.ID:
                            this.event = new CureRitual();
                            break;
                    }
                    EventPatch.specialEvents.remove(tmp.get(0));
                    AbstractDungeon.specialOneTimeEventList.remove(tmp.get(0));
                }
            }
        }
        else{
            //Reconstruction
            if(AbstractDungeon.getCurrMapNode().x == 3 && AbstractDungeon.getCurrMapNode().y==3){
                this.event = new Reconstruction();
            }
            //Little Step Of The Explorer todo
        }
        if(event==null){
            this.event = AbstractDungeon.generateEvent(eventRngDuplicate);
        }
        this.event.onEnterRoom();
    }
}
