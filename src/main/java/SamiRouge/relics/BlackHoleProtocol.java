package SamiRouge.relics;

import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class BlackHoleProtocol extends CustomRelic {
    public static final String ID = "samirg:BlackHoleProtocol";
    private static final String IMG = "SamiRougeResources/img/relics/BlackHoleProtocol.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/BlackHoleProtocol_O.png";

    public BlackHoleProtocol(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O), AbstractRelic.RelicTier.UNCOMMON,LandingSound.MAGICAL);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if(EnergyPanel.getCurrentEnergy() >= AbstractDungeon.player.energy.energy){
            return damage +3F + MathUtils.round(EnergyPanel.getCurrentEnergy()-AbstractDungeon.player.energy.energy);
        }
        return super.atDamageModify(damage, c);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}



