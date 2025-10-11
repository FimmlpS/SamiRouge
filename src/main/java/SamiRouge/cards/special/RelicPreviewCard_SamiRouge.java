package SamiRouge.cards.special;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Iterator;

public class RelicPreviewCard_SamiRouge extends CustomCard {
    private static final String ID = "RelicPreviewCard_SamiRouge";
    private static final CardStrings cardStrings;

    public RelicPreviewCard_SamiRouge(){
        super(ID, cardStrings.NAME, (String)null,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.COMMON,CardTarget.NONE);
        relic = null;
    }

    AbstractRelic relic;
    ArrayList<AbstractRelic> totalRelics = new ArrayList<>();

    public void setTotalRelics(ArrayList<AbstractRelic> totalRelics) {
        this.totalRelics = new ArrayList<>(totalRelics);
    }

    public void setRelic(AbstractRelic relic){
        this.relic = relic;
        name = relic.name;
        portrait = new TextureAtlas.AtlasRegion(relic.img,0,0,relic.img.getWidth(),relic.img.getHeight());
        rawDescription = relic.getUpdatedDescription();
        rawDescription = rawDescription.replace("#b","");
        rawDescription = rawDescription.replace("#r","");
        rawDescription = rawDescription.replace("#p","");
        rawDescription = rawDescription.replace("#g","");
        rawDescription = rawDescription.replace("#y","");
        initializeTitle();
        initializeDescription();
        switch (relic.tier){
            case STARTER:
            case COMMON:
                this.rarity = CardRarity.COMMON;
                break;
            case UNCOMMON:
            case SHOP:
            case SPECIAL:
                this.rarity = CardRarity.UNCOMMON;
                break;
            case RARE:
            case BOSS:
                this.rarity = CardRarity.RARE;
                break;
            default:
                this.rarity = CardRarity.SPECIAL;
                break;
        }

    }

    public AbstractRelic getRelicCopy(){
        if(relic==null)
            return null;
        return relic.makeCopy();
    }

    @Override
    public void onChoseThisOption() {
        AbstractRelic r = getRelicCopy();
        if(r!=null){
            //AbstractDungeon.topLevelEffectsQueue.add(new SpawnRelicAndObtainEffect(r));
            Iterator<AbstractRelic> it = totalRelics.iterator();
            while(it.hasNext()){
                AbstractRelic r2 = it.next();
                if(r2.relicId.equals(r.relicId)){
                    it.remove();
                }
            }
        }
        for(AbstractRelic r2 : totalRelics){
            if(r2.tier == AbstractRelic.RelicTier.COMMON){
                AbstractDungeon.commonRelicPool.add(r2.relicId);
            }
            else if(r2.tier == AbstractRelic.RelicTier.UNCOMMON){
                AbstractDungeon.uncommonRelicPool.add(r2.relicId);
            }
            else if(r2.tier == AbstractRelic.RelicTier.RARE){
                AbstractDungeon.rareRelicPool.add(r2.relicId);
            }
            else if(r2.tier == AbstractRelic.RelicTier.BOSS){
                AbstractDungeon.bossRelicPool.add(r2.relicId);
            }
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void upgrade() {

    }

    @SpireOverride
    protected void renderPortrait(SpriteBatch sb) {
        if(relic!=null){
            float drawX = this.current_x - 125.0F;
            float drawY = this.current_y - 95.0F;
            if (this.portrait != null) {
                Color renderColor = ReflectionHacks.getPrivate(this, AbstractCard.class,"renderColor");
                drawX = this.current_x - (float)this.portrait.packedWidth / 2.0F;
                drawY = this.current_y - (float)this.portrait.packedHeight / 2.0F;
                sb.setColor(renderColor);
                sb.draw(this.portrait, drawX, drawY + 12.0F, (float)this.portrait.packedWidth / 2.0F, (float)this.portrait.packedHeight / 2.0F - 72.0F, (float)this.portrait.packedWidth, (float)this.portrait.packedHeight, 2F*this.drawScale * Settings.scale, 2F*this.drawScale * Settings.scale, this.angle);
            }
        }
        else{
            SpireSuper.call(sb);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    }
}

