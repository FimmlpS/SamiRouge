package SamiRouge.cards.ciphertext.reason;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;

import java.util.ArrayList;

public class C30 extends AbstractCipherTextCard {
    public static final String ID = "samirg:C30";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public C30() {
        super(ID,cardStrings.NAME,-2,cardStrings.DESCRIPTION,CardType.STATUS,CardColor.COLORLESS,CardRarity.SPECIAL,CardTarget.NONE);
        this.cipherText = new CipherText(cardStrings.NAME,cardStrings.EXTENDED_DESCRIPTION[0],cardStrings.EXTENDED_DESCRIPTION[1]).setData(this, CipherText.CipherType.Reason, CipherText.CipherColor.Soul);
    }

    @Override
    public void triggerOnce() {
        remainX--;
    }

    @Override
    public void declareAtBattleEnd() {
        triggerOnce();
    }

    @Override
    public void triggerAfterDeclareAtOnce() {
        if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() instanceof ShopRoom){
            ShopScreen screen = AbstractDungeon.shopScreen;
            if(screen!=null){
                ArrayList<AbstractCard>  coloredCards = ReflectionHacks.getPrivate(screen, ShopScreen.class,"coloredCards");
                for(AbstractCard c : coloredCards){
                    ReflectionHacks.privateMethod(ShopScreen.class,"setPrice",AbstractCard.class).invoke(screen,c);
                }
                ArrayList<AbstractCard> colorlessCards = ReflectionHacks.getPrivate(screen, ShopScreen.class,"colorlessCards");
                for(AbstractCard c : colorlessCards){
                    ReflectionHacks.privateMethod(ShopScreen.class,"setPrice",AbstractCard.class).invoke(screen,c);
                }
                ArrayList<StoreRelic> relics = ReflectionHacks.getPrivate(screen, ShopScreen.class,"relics");
                for(StoreRelic r : relics){
                    if(r.relic!=null){
                        r.price = r.relic.getPrice();
                        screen.getNewPrice(r);
                    }
                }
                ArrayList<StorePotion> potions = ReflectionHacks.getPrivate(screen, ShopScreen.class,"potions");
                for(StorePotion p : potions){
                    if(p.potion!=null){
                        p.price = p.potion.getPrice();
                        screen.getNewPrice(p);
                    }
                }
            }
        }
    }

    @Override
    public String getPreviewDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[2] + remainX + cardStrings.EXTENDED_DESCRIPTION[together?4:3];
    }
}


