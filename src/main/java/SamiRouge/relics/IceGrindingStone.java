package SamiRouge.relics;

import basemod.abstracts.CustomRelic;
import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class IceGrindingStone extends CustomRelic {

    public static final String ID = "samirg:IceGrindingStone";
    private static final String IMG = "SamiRougeResources/img/relics/IceGrindingStone.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/IceGrindingStone_O.png";


    public IceGrindingStone(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.CLINK);
    }

    @Override
    public void atBattleStart() {
        AbstractCard c = AbstractDungeon.returnTrulyRandomCard().makeCopy();
        c.upgrade();
        if(c.baseDamage>0){
            c.baseDamage *= 2;
            c.damage = c.baseDamage;
        }
        if(c.baseBlock>0){
            c.baseBlock *= 2;
            c.block = c.baseBlock;
        }
        if(c.baseMagicNumber>0){
            c.baseMagicNumber *= 2;
            c.magicNumber = c.baseMagicNumber;
        }
        if(c.cost>=0){
            c.updateCost(1);
        }
        CardModifierManager.addModifier(c,new EtherealMod());

        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player,this));
        addToBot(new MakeTempCardInDrawPileAction(c,1,true,true));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}









