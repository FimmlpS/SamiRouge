package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HandOfRapidness extends CustomRelic {

    public static final String ID = "samirg:HandOfRapidness";
    private static final String IMG = "SamiRougeResources/img/relics/HandOfRapidness.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HandOfRapidness_O.png";

    boolean skilled = false;
    boolean attacked = false;

    public HandOfRapidness(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public void atTurnStart() {
        skilled = false;
        attacked = false;
    }

    //手中移除前
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard.type == AbstractCard.CardType.ATTACK || targetCard.type == AbstractCard.CardType.SKILL) {
            boolean playTwice = false;
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                //有其他同类型牌
                if (c.type == targetCard.type && c != targetCard) {
                    playTwice = false;
                    break;
                } else if (c == targetCard)
                    playTwice = true;
            }
            if (playTwice) {
                if (targetCard.type == AbstractCard.CardType.ATTACK) {
                    if (attacked) {
                        return;
                    } else {
                        attacked = true;
                    }
                }
                if (targetCard.type == AbstractCard.CardType.SKILL) {
                    if (skilled) {
                        return;
                    } else {
                        skilled = true;
                    }
                }

                this.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractMonster m = null;
                if (useCardAction.target != null) {
                    m = (AbstractMonster) useCardAction.target;
                }

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
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfRapidness();
    }
}



