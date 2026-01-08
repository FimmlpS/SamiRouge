package SamiRouge.relics;

import SamiRouge.actions.BeHunterAction;
import SamiRouge.monsters.AbstractSamiMonster;
import SamiRouge.patches.RelicPatch;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class ToBeAHunter extends CustomRelic {

    public static final String ID = "samirg:ToBeAHunter";
    private static final String IMG = "SamiRougeResources/img/relics/ToBeAHunter.png";
    private static final String IMG_O = "SamiRougeResources/img/relics/ToBeAHunter_O.png";

    AbstractSamiMonster.ExtraSpine spine;

    public ToBeAHunter(){
        super(ID, ImageMaster.loadImage(IMG),ImageMaster.loadImage(IMG_O),RelicTier.RARE,LandingSound.CLINK);
        RelicPatch.TreeHoleRelicPatch.samirgRelic.set(this,true);
        spine = new AbstractSamiMonster.ExtraSpine(0F,0F);
        spine.loadAnimationSingleNoEnemy("SamiRougeResources/img/chars_SamiRouge/Typhon/char_2012_typhon.atlas","SamiRougeResources/img/chars_SamiRouge/Typhon/char_2012_typhon.json",1.75F);
        spine.state.setAnimation(0,"Skill_3_Idle",true);
    }

    public void attackAnimation(){
        spine.state.setAnimation(0,"Skill_3_Loop",false);
        spine.state.addAnimation(0,"Skill_3_Idle",true,0F);
    }

    public void renderSpine(AbstractPlayer player, SpriteBatch sb){
        if(AbstractDungeon.player.name==null || AbstractDungeon.player.name.equals("Typhon"))
            return;
        if(spine!=null&&spine.atlas!=null){
            spine.state.update(Gdx.graphics.getDeltaTime());
            spine.state.apply(spine.skeleton);
            spine.skeleton.updateWorldTransform();
            spine.skeleton.setPosition(player.drawX - 0.8F*player.hb.width, player.drawY + 8F* Settings.scale);
            spine.skeleton.setColor(player.tint.color);
            spine.skeleton.setFlip(player.flipHorizontal, player.flipVertical);
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractPlayer.sr.draw(CardCrawlGame.psb, spine.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        this.flash();
        addToBot(new BeHunterAction(this));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}

