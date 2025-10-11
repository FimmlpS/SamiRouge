package SamiRouge.blights;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AbstractSamiBlight extends AbstractBlight {
    public AbstractSamiBlight(String ID,String NAME,String DESC,String img,boolean unique){
        super(ID, NAME, DESC, img, unique);
    }

    public String getExclusionBlight(){
        return null;
    }

    public String getUpgradeBlight(){ return null;}

    @Override
    public void obtain() {
        if(getExclusionBlight()!=null){
            if(AbstractDungeon.player.hasBlight(getExclusionBlight())){
                for(int i =0;i<AbstractDungeon.player.blights.size();i++){
                    if((AbstractDungeon.player.blights.get(i)).blightID.equals(getExclusionBlight())){
                        instantObtain(AbstractDungeon.player,i,true);
                        return;
                    }
                }
            }
        }
        super.obtain();
    }
}
