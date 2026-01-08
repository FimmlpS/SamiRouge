package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HandOfHardness extends CustomRelic {

    public static final String ID = "samirg:HandOfHardness";
    private static final String IMG = "SamiRougeResources/img/relics/HandOfHardness.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/HandOfHardness_O.png";

    public HandOfHardness(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.HEAVY);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
        updateDescription(AbstractPlayer.PlayerClass.IRONCLAD);
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = getUpdatedDescription();
        this.tips.clear();
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(MetallicizePower.POWER_ID);
        this.tips.add(new PowerTip(this.name,this.description));
        this.tips.add(new PowerTip(powerStrings.NAME,powerStrings.DESCRIPTIONS[0]+5+powerStrings.DESCRIPTIONS[1]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        this.counter = 0;
    }

    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter += 2;
        } else {
            ++this.counter;
        }

        if (this.counter == 3) {
            this.counter = 0;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new MetallicizePower(AbstractDungeon.player,4),4));
        }

    }

    @Override
    public AbstractRelic makeCopy() {
        return new HandOfHardness();
    }
}



