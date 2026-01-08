package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class EchoWood extends CustomRelic {

    public static final String ID = "samirg:EchoWood";
    private static final String IMG = "SamiRougeResources/img/relics/EchoWood.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/EchoWood_O.png";

    private ArrayList<AbstractCard.CardType> typeList = new ArrayList<>();

    public EchoWood(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.SPECIAL,LandingSound.HEAVY);
    }

    @Override
    public void atBattleStart() {
        typeList.clear();
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(!typeList.contains(targetCard.type)){
            typeList.add(targetCard.type);

            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractMonster m = null;
            if (useCardAction.target != null) {
                m = (AbstractMonster) useCardAction.target;
            }
            useCardAction.exhaustCard = true;
            AbstractCard tmp = targetCard.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = targetCard.current_x;
            tmp.current_y = targetCard.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float) Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, targetCard.energyOnUse, true, true), true);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EchoWood();
    }
}








