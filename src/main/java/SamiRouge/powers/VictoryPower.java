package SamiRouge.powers;

import SamiRouge.monsters.Smkght;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class VictoryPower extends AbstractPower {
    public static final String POWER_ID = "samirg:VictoryPower";
    private static final PowerStrings powerStrings;

    boolean isKhan;
    boolean special;

    public VictoryPower(AbstractCreature owner,boolean special){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.special = special;
        this.isKhan = owner instanceof Smkght;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Victory_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Victory_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        int desIndex = isKhan?0:1;
        if(special)
            desIndex+=2;
        this.description = powerStrings.DESCRIPTIONS[desIndex];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}




