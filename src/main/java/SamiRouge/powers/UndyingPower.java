package SamiRouge.powers;

import SamiRouge.actions.SummonSmshdwAction;
import SamiRouge.monsters.Smkght;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class UndyingPower extends AbstractPower {
    public static final String POWER_ID = "samirg:UndyingPower";
    private static final PowerStrings powerStrings;

    public UndyingPower(AbstractCreature owner){
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        String path128 = "SamiRougeResources/img/powers_SamiRouge/Undying_84.png";
        String path48 = "SamiRougeResources/img/powers_SamiRouge/Undying_32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128),0,0,84,84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48),0,0,32,32);
        this.updateDescription();
        this.isTurnBased = true;
        this.type = PowerType.BUFF;
        this.priority = 99;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }

    @Override
    public void onDeath() {
        this.flash();
        for(AbstractMonster m:AbstractDungeon.getMonsters().monsters){
            if(m instanceof Smkght && !m.isDeadOrEscaped()){
                addToBot(new ApplyPowerAction(m,this.owner,new StrengthPower(m,2),2));
            }
        }
        addToBot(new SummonSmshdwAction(this.owner.drawX,this.owner.drawY));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    }
}





