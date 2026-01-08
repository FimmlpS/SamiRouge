package SamiRouge.patches;

import SamiRouge.dungeons.TheSami;
import SamiRouge.events.TreeHoleEvent;
import SamiRouge.events.TreeHoleOutEvent;
import SamiRouge.relics.DimensionalFluidity;
import SamiRouge.samiMod.SamiRougeHelper;
import SamiRouge.save.SamiTreeHoleSave;
import TreeHole.mod.TreeHoleBase;
import TreeHole.save.TreeHoleSave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.ArrayList;

public class TreeHoleSami extends TreeHoleBase {
    @Override
    public AbstractDungeon getDungeon(AbstractPlayer abstractPlayer, ArrayList<String> arrayList, int i) {
        return new TheSami(abstractPlayer,arrayList);
    }

    @Override
    public AbstractDungeon getDungeon(AbstractPlayer abstractPlayer, SaveFile saveFile, int i) {
        return new TheSami(abstractPlayer,saveFile);
    }

    @Override
    public boolean isBoss(int i) {
        return i==3||i==4;
    }

    @Override
    public int getBossEnterY(int i) {
        if(i==3)
            return 4;
        if(i==4)
            return 4;
        return 15;
    }

    @Override
    public String getLevelName(int i) {
        if(i==4)
            return CardCrawlGame.languagePack.getUIString("samirg:TreeHole").TEXT[4];
        return CardCrawlGame.languagePack.getUIString("samirg:TreeHole").TEXT[1];
    }

    @Override
    public String getLevelNum(int i) {
        if(i==4)
            return CardCrawlGame.languagePack.getUIString("samirg:TreeHole").TEXT[3];
        return CardCrawlGame.languagePack.getUIString("samirg:TreeHole").TEXT[0];
    }

    @Override
    public float getMapSize(int i) {
        return -380F* Settings.scale;
    }

    @Override
    public float getScrollLimit(int i) {
        return -650F*Settings.scale;
    }

    @Override
    public String getEnterOption() {
        return CardCrawlGame.languagePack.getUIString("samirg:TreeHole").TEXT[2];
    }

    @Override
    public AbstractEvent treeHoleEnterEvent(Random random) {
        return new TreeHoleEvent(random);
    }

    @Override
    public AbstractEvent treeHoleOuterEvent(Random random) {
        return new TreeHoleOutEvent(random);
    }

    @Override
    public boolean enableTheEndingTreeHole() {
        return false;
    }

    @Override
    public void triggerWhenEnterTreeHole(int i) {
        //暂定不回血
        if(i!=4)
            AbstractDungeon.player.heal((AbstractDungeon.player.maxHealth-AbstractDungeon.player.currentHealth)/3);
        else {
            AbstractDungeon.player.heal((AbstractDungeon.player.maxHealth-AbstractDungeon.player.currentHealth)/2);
        }

        if(i==3){
            SamiRougeHelper.spawnIrreversibleOnce();
        }
        else if(i==4){
            SamiRougeHelper.spawnIrreversibleOnce();
            SamiRougeHelper.spawnIrreversibleOnce();
        }
    }

    @Override
    public Class<?> getSaveClass() {
        return SamiTreeHoleSave.class;
    }

    @Override
    public TreeHoleSave saveTreeHoleSave() {
        return SamiTreeHolePatch.samiSave;
    }

    @Override
    public void loadTreeHoleSave(TreeHoleSave treeHoleSave) {
        SamiTreeHolePatch.samiSave = (SamiTreeHoleSave) treeHoleSave;
    }
}
