package SamiRouge.patches;

import SamiRouge.cards.curse.Coldness_SamiRouge;
import SamiRouge.cards.curse.Hypothermia_SamiRouge;
import SamiRouge.monsters.Cresson;
import SamiRouge.monsters.SamiWarSoldier;
import SamiRouge.monsters.WillOfSami;
import SamiRouge.monsters.WoodCrack;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;

public class MonsterPatch {

    //测试期间替换三层BOSS
    private static final boolean test = false;

    @SpirePatch(clz = MonsterHelper.class,method = "getEncounter")
    public static class BossMixPatch{

        @SpirePostfixPatch
        public static MonsterGroup Postfix(MonsterGroup _result,String key){
            if(test){
                if(key.equals("Time Eater")){
                    return new MonsterGroup(new AbstractMonster[]{new WillOfSami(0,0)});
                }
                else if(key.equals("Awakened One")){
                    return new MonsterGroup(new AbstractMonster[]{
                            new SamiWarSoldier(-480F,0,true,new Coldness_SamiRouge()),
                            new SamiWarSoldier(-190F,0,true,new Hypothermia_SamiRouge()),
                            new WoodCrack(150F,0F)
                    });
                }
                else if(key.equals("Donu and Deca")){
                    return new MonsterGroup(new AbstractMonster[]{new Cresson(-200F,100F)});
                }
            }
            return _result;
        }
    }
}
