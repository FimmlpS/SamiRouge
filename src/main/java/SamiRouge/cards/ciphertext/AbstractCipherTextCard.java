package SamiRouge.cards.ciphertext;

import SamiRouge.samiMod.StringHelper;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;

import java.util.ArrayList;

public abstract class AbstractCipherTextCard extends CustomCard{
    public AbstractCipherTextCard(String id,String name, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target){
        super(id,name, StringHelper.getCipherIMGPATH(id),cost,rawDescription,type,color,rarity,target);
    }

    public CipherText cipherText;

    public int remainX;
    public boolean together;

    public boolean canSelect(){
        return true;
    }

    public boolean canDeclare(CipherText theOther){
        if(cipherText.color == CipherText.CipherColor.Thefair)
            return theOther.color == CipherText.CipherColor.Thefair && cipherText.type != theOther.type;
        return cipherText.type != theOther.type;
    }

    public String getPreviewTitle(){
        return this.name;
    }

    public String getPreviewDescription(){
        return "";
    }

    public void addRewardToSave(){
        RewardItem item = new RewardItem();
        AbstractDungeon.getCurrRoom().addCardReward(item);
        if(CardCrawlGame.saveFile!=null){
            //CardCrawlGame.saveFile.combat_rewards.add(new RewardSave(item.type.toString(), (String)null));
        }
    }

    public void addRelicToSave(){
        AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
        RewardItem item = new RewardItem(relic);
        AbstractDungeon.getCurrRoom().rewards.add(item);
        if(CardCrawlGame.saveFile!=null){
            //CardCrawlGame.saveFile.combat_rewards.add(new RewardSave(item.type.toString(), item.relic.relicId));
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void upgrade() {

    }

    private void renderExtraTip(SpriteBatch sb){
        boolean renderTip = ReflectionHacks.getPrivate(this,AbstractCard.class,"renderTip");
        if(renderTip && cipherText.type == CipherText.CipherType.Reason){
            float x = this.current_x - AbstractCard.IMG_WIDTH/2F * drawScale;
            float y = this.current_y + AbstractCard.IMG_HEIGHT/2F * drawScale;
            ArrayList< PowerTip> tips = new ArrayList<>();
            tips.add(new PowerTip(CipherText.extraTitle,cipherText.extraDescription));
            ReflectionHacks.RMethod method = ReflectionHacks.privateStaticMethod(TipHelper.class,"renderPowerTips",float.class,float.class,SpriteBatch.class,ArrayList.class);
            method.invoke(null,x,y,sb,tips);
        }
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        super.renderCardTip(sb);
        renderExtraTip(sb);
    }

    @Override
    public void renderInLibrary(SpriteBatch sb) {
        super.renderInLibrary(sb);
        renderExtraTip(sb);
    }

    public void declareAtBattleStart(){}
    public void declareAtTurnStart(){}
    public void declareAtBattleEnd(){}
    public void triggerOnce(){
        remainX--;
    }

    //布局 处理完后获取真正的theX
    public int declare(boolean together){
        if(cipherText != null){
            return cipherText.theX;
        }
        return 1;
    }

    //本因 传入的theX必须大于0，处理完declared后若remainX>0则添加为BUFF，否则不添加
    public void declared(int theX, boolean together){
        this.remainX = theX;
        this.together = together;
    }
}

