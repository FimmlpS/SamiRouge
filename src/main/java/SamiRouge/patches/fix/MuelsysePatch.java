package SamiRouge.patches.fix;

import SamiRouge.dungeons.TheSami;
import SamiRouge.events.TreeHoleEvent;
import SamiRouge.patches.SamiTreeHolePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MuelsysePatch {
    @SpirePatch(clz = AbstractMonster.class,method = "onFinalBossVictoryLogic")
    public static class onFinalBossVictoryLogic {
        @SpirePostfixPatch
        public static void Postfix(AbstractMonster _inst) {
            if(_inst.id!=null && _inst.id.equals(TreeHoleEvent.MUELSYSE_ANMA) && AbstractDungeon.id!=null && AbstractDungeon.id.equals(TheSami.ID)){
                AbstractDungeon.getCurrRoom().rewardAllowed = true;
                SamiTreeHolePatch.withHerTalk = true;
            }
        }
    }
}
