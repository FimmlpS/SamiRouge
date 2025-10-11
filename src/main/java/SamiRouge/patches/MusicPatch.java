package SamiRouge.patches;

import SamiRouge.dungeons.TheSami;
import SamiRouge.monsters.Cresson;
import SamiRouge.monsters.Smkght;
import SamiRouge.monsters.WillOfSami;
import SamiRouge.monsters.WoodCrack;
import SamiRouge.samiMod.SamiRougeHelper;
import TreeHole.mod.TreeHoleHelper;
import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MusicPatch {
    public MusicPatch(){}

    @SpirePatch(
            clz = MainMusic.class,
            method = "getSong"
    )
    public static class MainMusicPatch{
        @SpirePostfixPatch
        public static Music Postfix(Music _result, MainMusic _inst,String key){

            if(key.equals(TheSami.ID)){
                if(TreeHoleHelper.getCurrentType() < 3)
                    return MainMusic.newMusic("SamiRougeResources/audio/main1.ogg");
                else if(TreeHoleHelper.getCurrentType() == 3){
                    return MainMusic.newMusic("SamiRougeResources/audio/main2.ogg");
                }
                else if(TreeHoleHelper.getCurrentType() == 4){
                    return MainMusic.newMusic("SamiRougeResources/audio/main3.ogg");
                }
            }

            return _result;
        }
    }

    @SpirePatch(
            clz = TempMusic.class,
            method = "getSong"
    )
    public static class TempMusicPatch{
        @SpirePostfixPatch
        public static Music Postfix(Music _result, TempMusic _inst,String key){
            if(AbstractDungeon.id == null || !AbstractDungeon.id.equals(TheSami.ID))
                return _result;
            switch (key) {
                case "BOSS_BEYOND":
                    if (AbstractDungeon.bossKey != null && AbstractDungeon.bossKey.equals(WillOfSami.ID)) {
                        return MainMusic.newMusic("SamiRougeResources/audio/wos.ogg");
                    } else if (AbstractDungeon.bossKey != null && AbstractDungeon.bossKey.equals(WoodCrack.ID)) {
                        return MainMusic.newMusic("SamiRougeResources/audio/ml.ogg");
                    } else if (AbstractDungeon.bossKey != null && AbstractDungeon.bossKey.equals(Cresson.ID)) {
                        return MainMusic.newMusic("SamiRougeResources/audio/plt.ogg");
                    } else if (AbstractDungeon.bossKey != null && AbstractDungeon.bossKey.equals(Smkght.BOSS_ID)) {
                        return MainMusic.newMusic("SamiRougeResources/audio/sw.ogg");
                    }
                    return _result;
                case "ELITE":
                    return MainMusic.newMusic("SamiRougeResources/audio/bossbt.ogg");
                case "SHOP":
                    return MainMusic.newMusic("SamiRougeResources/audio/shop.ogg");
            }

            return _result;
        }
    }
}
