package SamiRouge.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NonAndExistencePower extends AbstractPower {
    public static final String POWER_ID = "samirg:NonAndExistencePower";
    private static final PowerStrings powerStrings;

    public NonAndExistencePower(AbstractMonster owner){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/NonAndExistence_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/NonAndExistence_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.flashWithoutSound();
        return 0;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        return 0;
    }

    @Override
    public void updateDescription() {
        String des = powerStrings.DESCRIPTIONS[0];
        this.description = des;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}
