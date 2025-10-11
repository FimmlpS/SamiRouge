package SamiRouge.patches;

import SamiRouge.blights.IrreversibleMatrix;
import SamiRouge.dungeons.TheSami;
import SamiRouge.helper.CardHelper;
import SamiRouge.relics.*;
import SamiRouge.samiMod.ModConfig;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import java.util.ArrayList;

public class RelicPatch {

    @SpirePatch(clz = AbstractPlayer.class,method = "render")
    public static class BeHunterPatch{
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer _inst, SpriteBatch sb){
            AbstractRelic beHunter = _inst.getRelic(ToBeAHunter.ID);
            if(beHunter!=null){
                ToBeAHunter toBeAHunter = (ToBeAHunter) beHunter;
                toBeAHunter.renderSpine(_inst,sb);
            }
        }
    }

    @SpirePatch(clz = RewardItem.class,method = "applyGoldBonus")
    public static class UrsasBladePatch{
        @SpireInsertPatch(rloc = 7)
        public static void Insert(RewardItem _inst, boolean theft){
            if(AbstractDungeon.player.hasRelic(UrsasBlade.ID)){
                int level = 0;
                AbstractBlight ir = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
                if(ir!=null)
                    level = ir.counter;
                _inst.bonusGold += MathUtils.round(_inst.goldAmt* 0.1F * level);
            }
        }
    }

    @SpirePatch(clz = AbstractRelic.class,method = SpirePatch.CLASS)
    public static class TreeHoleRelicPatch{
        public static SpireField<Boolean> samirgRelic = new SpireField<Boolean>(()->false);
    }

    @SpirePatch(clz = ApplyPowerAction.class,method = "update")
    public static class ApplyPowerPatch{
        @SpireInsertPatch(rlocs = {116,153})
        public static void Insert(ApplyPowerAction _inst){
            if(!(_inst.target instanceof AbstractPlayer))
                return;
            AbstractRelic eb = AbstractDungeon.player.getRelic(ExploreBag.ID);
            if(eb==null)
                return;
            AbstractPower p = ReflectionHacks.getPrivate(_inst,ApplyPowerAction.class,"powerToApply");
            if(p!=null&&p.type== AbstractPower.PowerType.DEBUFF){
                eb.onTrigger();
            }
        }

//        @SpirePostfixPatch
//        public static void Postfix(ApplyPowerAction _inst){
//            if(AbstractDungeon.id == TheSami.ID || ModConfig.all_fans){
//
//                if((float)ReflectionHacks.getPrivate(_inst,ApplyPowerAction.class,"duration")>0.02F)
//                ReflectionHacks.setPrivate(_inst,ApplyPowerAction.class,"duration",0.02F);
//            }
//        }
    }

    @SpirePatch(clz = FlightPower.class,method = "calculateDamageTakenAmount")
    public static class CloudMoverPatch{
        @SpirePostfixPatch
        public static float Postfix(float _ret, FlightPower _inst, float damage, DamageInfo.DamageType type){
            if(AbstractDungeon.player.hasRelic(CloudMover.ID))
                return 3F*_ret;
            return _ret;
        }
    }

    @SpirePatch(clz = CombatRewardScreen.class,method = "setupItemReward")
    public static class SetUpDFPatch{
        @SpirePostfixPatch
        public static void Postfix(CombatRewardScreen _inst){
            if(!SamiTreeHolePatch.getDimensionalFluidity)
                return;
            if(AbstractDungeon.player!=null&&AbstractDungeon.player.hasRelic(DimensionalFluidity.ID))
                return;
            _inst.rewards.add(0,new RewardItem(new DimensionalFluidity()));
            _inst.positionRewards();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class,method = "getColorlessRewardCards")
    public static class OperationCallColorlessPatch{
        @SpirePostfixPatch
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> _ret){
            if(!AbstractDungeon.player.hasRelic(OperationCall.ID))
                return _ret;
            for(AbstractCard c : _ret){
                CardHelper.markRandomGDJ(c);
            }
            return _ret;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class,method = "getRewardCards")
    public static class OperationCallPatch{
        @SpirePostfixPatch
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> _ret){
            if(!AbstractDungeon.player.hasRelic(OperationCall.ID))
                return _ret;
            for(AbstractCard c : _ret){
                CardHelper.markRandomGDJ(c);
            }
            return _ret;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class,method = "loseEnergy")
    public static class ThornRingPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst, int e){
            AbstractRelic r = _inst.getRelic(ThornRing.ID);
            if(r!=null){
                r.onTrigger();
            }
        }
    }

}
