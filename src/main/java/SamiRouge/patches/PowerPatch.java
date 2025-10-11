package SamiRouge.patches;

import SamiRouge.powers.AbsorbFirePower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FeelNoPainPower;

public class PowerPatch {

    @SpirePatch(clz = AbstractMonster.class,method = "damage")
    public static class HologcPatch{
        @SpirePrefixPatch
        public static void Prefix(AbstractMonster _inst, DamageInfo info){
            if(AbsorbFirePower.someOneOwned){
                if(!_inst.isDeadOrEscaped() && !_inst.hasPower(AbsorbFirePower.POWER_ID)){
                    for(AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                        if(!m.isDeadOrEscaped() && m.hasPower(AbsorbFirePower.POWER_ID)){
                            int shared = info.output/2;
                            info.output -= shared;
                            AbstractPower p = m.getPower(AbsorbFirePower.POWER_ID);
                            p.flash();
                            AbstractDungeon.actionManager.addToTop(new DamageAction(m,new DamageInfo(null,shared, DamageInfo.DamageType.THORNS)));
                            return;
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(clz = CardGroup.class,method = "moveToExhaustPile")
    public static class ExhaustPatch{
        @SpirePostfixPatch
        public static void Postfix(CardGroup _inst, AbstractCard c){
            for(AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                if (!m.isDeadOrEscaped()) {
                    AbstractPower p = m.getPower(FeelNoPainPower.POWER_ID);
                    if(p != null){
                        p.onExhaust(c);
                    }
                }
            }
        }
    }
}
