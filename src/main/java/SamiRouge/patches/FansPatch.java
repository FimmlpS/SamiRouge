package SamiRouge.patches;

import SamiRouge.blights.*;
import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.cards.ciphertext.reason.C30;
import SamiRouge.dungeons.TheSami;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.samiMod.ModConfig;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.Objects;

public class FansPatch {

    //初始化获得抗干扰指数和一对密文板
    //开局
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeRelicList"
    )
    public static class InitializeRelicListPatch {
        public static void Postfix(AbstractDungeon _inst) {
            AbstractBlight blightToObtain = new AntiInterference();
            int index = AbstractDungeon.player.blights.size();
            blightToObtain.instantObtain(AbstractDungeon.player, index, false);

            //密文板初始化
            DeclareHelper.onInitialize();
        }
    }

    //觉醒者生命修正
    @SpirePatch(clz = AwakenedOne.class,method = "changeState")
    public static class AwakenedOnePatch{
        @SpireInsertPatch(rloc = 20)
        public static void Postfix(AwakenedOne _inst, String key){
            if(!Objects.equals(key, "REBIRTH"))
                return;
            IrreversibleMatrix ir = (IrreversibleMatrix) AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
            if(ir!=null){
                _inst.maxHealth = (int)(_inst.maxHealth * (1F+ ir.getBlvHP()));
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class,method = "loseGold")
    public static class DequantizationLoseGoldPatch{
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer _inst,int goldAmount){
            if((AbstractDungeon.id==null || !AbstractDungeon.id.equals(TheSami.ID))&&!ModConfig.all_fans)
                return;
            if(AbstractDungeon.currMapNode==null||AbstractDungeon.getCurrRoom() == null||AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.COMBAT)
                return;

            AbstractBlight de01 = _inst.getBlight(Dequantization01.ID);
            if(de01!=null){
                de01.flash();
                if(goldAmount>0){
                    int extra = goldAmount/4;
                    _inst.gold -= extra;
                    if(_inst.gold<0){
                        _inst.gold = 0;
                    }
                }
            }

            AbstractBlight de02 = _inst.getBlight(Dequantization02.ID);
            if(de02!=null){
                de02.flash();
                if(goldAmount>0){
                    int extra = goldAmount/2;
                    _inst.gold -= extra;
                    if(_inst.gold<0){
                        _inst.gold = 0;
                    }
                }
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class,method = "damage")
    public static class DequantizationLoseHPPatch{
        @SpireInsertPatch(rloc = 90,localvars = {"damageAmount"})
        public static void Insert(AbstractPlayer _inst, DamageInfo info, @ByRef int[] damageAmount){
            if((AbstractDungeon.id==null || !AbstractDungeon.id.equals(TheSami.ID))&&!ModConfig.all_fans)
                return;
            if(AbstractDungeon.currMapNode==null||AbstractDungeon.getCurrRoom() == null||AbstractDungeon.getCurrRoom().phase== AbstractRoom.RoomPhase.COMBAT)
                return;
            AbstractBlight de01 = _inst.getBlight(Dequantization01.ID);
            if(de01!=null){
                de01.flash();
                int tmp = damageAmount[0] + damageAmount[0]/4;
                if(damageAmount[0]<_inst.currentHealth&&tmp>=_inst.currentHealth){
                    damageAmount[0] = _inst.currentHealth-1;
                }
            }

            AbstractBlight de02 = _inst.getBlight(Dequantization02.ID);
            if(de02!=null){
                de02.flash();
                int tmp = damageAmount[0] + damageAmount[0]/2;
                if(damageAmount[0]<_inst.currentHealth&&tmp>=_inst.currentHealth){
                    damageAmount[0] = _inst.currentHealth-1;
                }
            }
        }
    }


    @SpirePatch(clz = AbstractRelic.class,method = "getPrice")
    public static class PansocialRelicPatch{
        @SpirePostfixPatch
        public static int Postfix(int _ret,AbstractRelic _inst){
            if((AbstractDungeon.id!=null && AbstractDungeon.id.equals(TheSami.ID))||!ModConfig.all_fans){
                AbstractBlight pan01 = AbstractDungeon.player.getBlight(PansocialParadox01.ID);
                if(pan01!=null){
                    _ret = MathUtils.round((float)_ret * 1.25F);
                }
                AbstractBlight pan02 = AbstractDungeon.player.getBlight(PansocialParadox02.ID);
                if(pan02!=null){
                    if(_inst.tier == AbstractRelic.RelicTier.COMMON)
                        _ret = MathUtils.round((float) _ret * 2F);
                    else
                        _ret = MathUtils.round((float) _ret * 1.5F);
                }
            }
            float decrease = 1F;
            for(AbstractCipherTextCard c: DeclareHelper.buffed){
                if(c instanceof C30){
                    if(c.together && _inst.tier == AbstractRelic.RelicTier.RARE){
                        decrease *= 0.25F;
                    }
                    else {
                        decrease *= 0.5F;
                    }
                }
            }
            if(decrease<1F){
                _ret = MathUtils.round((float) _ret * decrease);
            }
            return _ret;
        }
    }

    @SpirePatch(clz = AbstractPotion.class,method = "getPrice")
    public static class PansocialPotionPatch{
        @SpirePostfixPatch
        public static int Postfix(int _ret,AbstractPotion _inst){
            if((AbstractDungeon.id!=null && AbstractDungeon.id.equals(TheSami.ID))||!ModConfig.all_fans){
                AbstractBlight pan01 = AbstractDungeon.player.getBlight(PansocialParadox01.ID);
                if(pan01!=null){
                    _ret = MathUtils.round((float)_ret * 1.25F);
                }
                AbstractBlight pan02 = AbstractDungeon.player.getBlight(PansocialParadox02.ID);
                if(pan02!=null){
                    if(_inst.rarity == AbstractPotion.PotionRarity.COMMON)
                        _ret = MathUtils.round((float) _ret * 2F);
                    else
                        _ret = MathUtils.round((float) _ret * 1.5F);
                }
            }
            float decrease = 1F;
            for(AbstractCipherTextCard c: DeclareHelper.buffed){
                if(c instanceof C30){
                    if(c.together && _inst.rarity == AbstractPotion.PotionRarity.RARE){
                        decrease *= 0.25F;
                    }
                    else {
                        decrease *= 0.5F;
                    }
                }
            }
            if(decrease<1F){
                _ret = MathUtils.round((float) _ret * decrease);
            }
            return _ret;
        }
    }

    @SpirePatch(clz = AbstractCard.class,method = "getPrice")
    public static class PansocialCardPatch{
        @SpirePostfixPatch
        public static int Postfix(int _ret,AbstractCard.CardRarity rarity){
            if((AbstractDungeon.id!=null && AbstractDungeon.id.equals(TheSami.ID))||!ModConfig.all_fans) {
                AbstractBlight pan01 = AbstractDungeon.player.getBlight(PansocialParadox01.ID);
                if (pan01 != null) {
                    _ret = MathUtils.round((float) _ret * 1.25F);
                }
                AbstractBlight pan02 = AbstractDungeon.player.getBlight(PansocialParadox02.ID);
                if (pan02 != null) {
                    if (rarity == AbstractCard.CardRarity.COMMON)
                        _ret = MathUtils.round((float) _ret * 2F);
                    else
                        _ret = MathUtils.round((float) _ret * 1.5F);
                }
            }
            float decrease = 1F;
            for(AbstractCipherTextCard c: DeclareHelper.buffed){
                if(c instanceof C30){
                    if(c.together && rarity == AbstractCard.CardRarity.RARE){
                        decrease *= 0.25F;
                    }
                    else {
                        decrease *= 0.5F;
                    }
                }
            }
            if(decrease<1F){
                _ret = MathUtils.round((float) _ret * decrease);
            }
            return _ret;
        }
    }


}
