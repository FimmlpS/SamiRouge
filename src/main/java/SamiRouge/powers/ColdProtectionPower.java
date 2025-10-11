package SamiRouge.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ColdProtectionPower extends AbstractPower {
    public static final String POWER_ID = "samirg:ColdProtectionPower";
    private static final PowerStrings powerStrings;

    public ColdProtectionPower(AbstractCreature owner, int level){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = level;
        this.updateDescription();

        String path128 = "SamiRougeResources/img/powers_SamiRouge/oths11_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/oths11_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);

        this.type = PowerType.BUFF;
        this.priority = 90;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0]+(this.amount*25)+powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        int dmg = info.output;
        float rate = 0.25F * (float)this.amount;
        this.flash();
        addToTop(new GainBlockAction(this.owner,(int)(rate*(float)(dmg)),true));
        return super.onAttacked(info,damageAmount);
    }

    static {
        powerStrings= CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}

