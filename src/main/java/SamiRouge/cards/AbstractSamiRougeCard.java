package SamiRouge.cards;

import SamiRouge.samiMod.StringHelper;
import basemod.abstracts.CustomCard;

public abstract class AbstractSamiRougeCard extends CustomCard {
    public AbstractSamiRougeCard(String id,String name, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target){
        super(id,name, StringHelper.getCardIMGPATH(id,type),cost,rawDescription,type,color,rarity,target);
    }
}
