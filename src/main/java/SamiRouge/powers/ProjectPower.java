package SamiRouge.powers;

import SamiRouge.monsters.Hologc;
import SamiRouge.monsters.Smlion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ProjectPower extends AbstractPower {
    public static final String POWER_ID = "samirg:ProjectPower";
    private static final PowerStrings powerStrings;
    boolean triggered;

    public ProjectPower(AbstractCreature owner){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Project_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Project_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99;
        triggered = false;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(info.type== DamageInfo.DamageType.NORMAL){
            if(!triggered){
                triggered = true;
                Hologc gc = new Hologc(-400F,0F);
                addToTop(new RemoveSpecificPowerAction(this.owner,this.owner,this));
                addToTop(new SpawnMonsterAction(gc,false));
                gc.usePreBattleAction();
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}







