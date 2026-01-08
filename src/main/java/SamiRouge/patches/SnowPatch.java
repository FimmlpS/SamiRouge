package SamiRouge.patches;

import SamiRouge.effects.SingleSnowEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;

public class SnowPatch {
    public static boolean isSnowing = false;
    //音效
    public static boolean isLoop = false;
    public static float snowTimer = 0F;

    public static ArrayList<SingleSnowEffect> snows = new ArrayList<>();

    private static void render(SpriteBatch sb){
        Iterator<SingleSnowEffect> it = snows.iterator();
        while(it.hasNext()){
            SingleSnowEffect snow = it.next();
            snow.render(sb);
        }
    }

    private static void update(){
        if(isSnowing){
            if(!isLoop){
                isLoop = true;
            }

            snowTimer -= Gdx.graphics.getDeltaTime();
            if(snowTimer < 0){
                snowTimer = 0.1F;
                int snowLevel = 2 * AbstractDungeon.actNum + 2;
                if(snowLevel<4)
                    snowLevel = 4;
                if(snowLevel>8)
                    snowLevel = 8;
                for(int i =0;i<snowLevel;i++){
                    snows.add(new SingleSnowEffect(Settings.WIDTH * MathUtils.random(-0.05F,0.96F),Settings.WIDTH *MathUtils.random(0.05F,0.12F)));
                }
            }
        }
        else {
            if(isLoop){
                isLoop = false;
            }
        }

        Iterator<SingleSnowEffect> var1 = snows.iterator();
        while(var1.hasNext()){
            SingleSnowEffect s = var1.next();
            s.update();
            if(s.isDone){
                var1.remove();
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class,method = "render")
    public static class RainPatchRenderPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst, SpriteBatch sb) {
            render(sb);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class,method = "update")
    public static class RainPatchUpdatePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst) {
            update();
        }
    }
}
