package SamiRouge.samiMod;

import SamiRouge.blights.*;
import SamiRouge.relics.*;
import basemod.BaseMod;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;
import java.util.Collections;

public class SamiRougeHelper {
    public static String BOSS_KEY = "";
    private static final ArrayList<String> blights;
    public static ArrayList<String> samiRelics;
    private static boolean modAdded = false;

    public static ArrayList<AbstractRelic> getRelicsToAdd() {
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        //COMMON
        relics.add(new WarnWood());
        relics.add(new StoneRelic());
        relics.add(new MissHome());
        relics.add(new IcyBody());
        relics.add(new RoadVine());
        relics.add(new CloudMover());
        relics.add(new Friston());
        relics.add(new NorthWind());
        relics.add(new ExploreBag());
        relics.add(new MidMachine());
        relics.add(new TerraMap());
        relics.add(new ActionPlan());

        //UNCOMMON
        relics.add(new UrsasBlade());
        relics.add(new RitualBell());
        relics.add(new LiveWood());
        relics.add(new RockHorn());
        relics.add(new ImmortalTorch());
        relics.add(new ImprovePackage());
        relics.add(new ShellShield());
        relics.add(new BlackHoleProtocol());
        relics.add(new CombatPlan());
        relics.add(new ThornRing());

        //RARE
        relics.add(new HunterSee());
        relics.add(new GoldenCup());
        relics.add(new ToBeAHunter());
        relics.add(new HandOfBurst());
        relics.add(new HandOfOpportunity());
        relics.add(new HandOfDogfight());
        relics.add(new HandOfFlowing());
        relics.add(new HandOfHardness());
        relics.add(new HandOfExplosion());
        relics.add(new HandOfRapidness());
        relics.add(new HandOfClean());
        relics.add(new PeopleLetter());

        if (samiRelics == null) {
            samiRelics = new ArrayList<>();
            for (AbstractRelic r : relics) {
                samiRelics.add(r.relicId);
            }

            if (!modAdded) {
                //加载mod的遗物
                modAdded = true;
                if (BaseMod.hasModID("Muelsyse_FimmlpS:")) {
                    samiRelics.add("muelsyse:LiveRose");
                    samiRelics.add("muelsyse:TreeFruit");
                    samiRelics.add("muelsyse:SproutStick");
                }
            }
        }

        //BOSS
        relics.add(new OperationCall());

        //EVENT
        relics.add(new HatOfTreeScar());
        relics.add(new RouteMap());
        relics.add(new LimitlessGift());
        relics.add(new DimensionalFluidity());
        relics.add(new EveryNight());
        relics.add(new EchoWood());
        relics.add(new IceGrindingStone());
        relics.add(new FarScry());
        relics.add(new CollapseSeed());
        relics.add(new SpaceFragment());
        relics.add(new DeepBurn());

        return relics;
    }

    public static AbstractRelic returnTreeHoleRelic(AbstractRelic.RelicTier tier){
        AbstractRelic relic;
        String key = "";
        switch (tier){
            case COMMON:
                for(String s:AbstractDungeon.commonRelicPool){
                    if(samiRelics.contains(s)){
                        key = s;
                        break;
                    }
                }
                AbstractDungeon.commonRelicPool.remove(key);
                break;
            case UNCOMMON:
                for(String s:AbstractDungeon.uncommonRelicPool){
                    if(samiRelics.contains(s)){
                        key = s;
                        break;
                    }
                }
                AbstractDungeon.uncommonRelicPool.remove(key);
                break;
            case RARE:
                for(String s:AbstractDungeon.rareRelicPool){
                    if(samiRelics.contains(s)){
                        key = s;
                        break;
                    }
                }
                AbstractDungeon.rareRelicPool.remove(key);
                break;
        }
        if(key==""){
            key = AbstractDungeon.returnRandomRelicKey(tier);
        }
        relic = RelicLibrary.getRelic(key);
        return relic;
    }

    public static AbstractBlight getRandomBlightToObtain(Random rng){
        ArrayList<String> tmp = new ArrayList<>(blights);
        for(AbstractBlight blight:AbstractDungeon.player.blights){
            if(blight instanceof AbstractSamiBlight){
                tmp.remove(((AbstractSamiBlight) blight).getExclusionBlight());
            }
        }
        Collections.shuffle(tmp,rng.random);
        for(String s:tmp){
            for(AbstractBlight blight:AbstractDungeon.player.blights){
                if(blight.blightID == s){
                    return getBlightByID(((AbstractSamiBlight)blight).getUpgradeBlight());
                }
            }
            return getBlightByID(s);
        }
        return null;
    }

    public static void removeRandomBlight(Random rng){
        ArrayList<Integer> blightIndex = new ArrayList<>();
        int index = 0;
        for(AbstractBlight blight:AbstractDungeon.player.blights){
            if(blight instanceof AbstractSamiBlight){
                if(((AbstractSamiBlight) blight).getExclusionBlight()!=null || ((AbstractSamiBlight) blight).getUpgradeBlight()!=null){
                    blightIndex.add(index);
                }
            }
            index++;
        }
        if(blightIndex.isEmpty()){
            return;
        }
        int random = rng.random(0,blightIndex.size()-1);
        int indexReal = blightIndex.get(random);
        if(AbstractDungeon.player.blights.get(indexReal) instanceof AbstractSamiBlight){
            AbstractSamiBlight asb = (AbstractSamiBlight) AbstractDungeon.player.blights.get(indexReal);
            if(asb.getExclusionBlight()!=null){
                AbstractBlight ab = getBlightByID(asb.getExclusionBlight());
                ab.instantObtain(AbstractDungeon.player,indexReal,true);
                ab.flash();
            }
            else if(asb.getUpgradeBlight()!=null){
                AbstractDungeon.player.blights.remove(asb);
                ArrayList<AbstractBlight> tmpBlights = new ArrayList(AbstractDungeon.player.blights);
                AbstractDungeon.player.blights.clear();

                for(int i = 0; i < tmpBlights.size(); ++i) {
                    reorganizeObtainBlight(tmpBlights.get(i),AbstractDungeon.player,i,false,tmpBlights.size());
                    tmpBlights.get(i).flash();
                }
            }
        }
    }

    private static void reorganizeObtainBlight(AbstractBlight b,AbstractPlayer p, int slot, boolean callOnEquip, int blightAmt){
        b.isDone = true;
        b.isObtained = true;
        p.blights.add(b);
        b.currentX = 64.0F * Settings.xScale + (float)slot * 64.0F * Settings.xScale;
        b.currentY = Settings.isMobile ? (float)Settings.HEIGHT - 206.0F * Settings.scale : (float)Settings.HEIGHT - 176.0F * Settings.scale;
        b.targetX = b.currentX;
        b.targetY = b.currentY;
        b.hb.move(b.currentX, b.currentY);
        if (callOnEquip) {
            b.onEquip();
        }

        UnlockTracker.markRelicAsSeen(b.blightID);
    }

    public static void spawnIrreversibleOnce(){
        AbstractBlight matrix = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(matrix!=null){
            matrix.incrementUp();
            matrix.flash();
        }
        else {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain((float) Settings.WIDTH/2F,(float) Settings.HEIGHT/2F, new IrreversibleMatrix());
        }
    }

    public static int getIrreversibleMatrixLevel(){
        AbstractBlight matrix = AbstractDungeon.player.getBlight(IrreversibleMatrix.ID);
        if(matrix!=null){
            return matrix.counter;
        }
        return 0;
    }


    public static AbstractBlight getBlightByID(String id){
        switch (id){
            case AntiInterference.ID:
                return new AntiInterference();
            case IrreversibleMatrix.ID:
                return new IrreversibleMatrix();
            case Dequantization01.ID:
                return new Dequantization01();
            case Dequantization02.ID:
                return new Dequantization02();
            case SubstantialCollapse01.ID:
                return new SubstantialCollapse01();
            case SubstantialCollapse02.ID:
                return new SubstantialCollapse02();
            case NonlinearMovement01.ID:
                return new NonlinearMovement01();
            case NonlinearMovement02.ID:
                return new NonlinearMovement02();
            case EmotionalEntity01.ID:
                return new EmotionalEntity01();
            case EmotionalEntity02.ID:
                return new EmotionalEntity02();
            case PansocialParadox01.ID:
                return new PansocialParadox01();
            case PansocialParadox02.ID:
                return new PansocialParadox02();
            case AbnormalAirpressure01.ID:
                return new AbnormalAirpressure01();
            case AbnormalAirpressure02.ID:
                return new AbnormalAirpressure02();
            case TriggeringInjury01.ID:
                return new TriggeringInjury01();
            case TriggeringInjury02.ID:
                return new TriggeringInjury02();
            case ConvergentConsumption01.ID:
                return new ConvergentConsumption01();
            case ConvergentConsumption02.ID:
                return new ConvergentConsumption02();
        }
        return null;
    }

    public static AbstractMonster monsterModifier(float mul,AbstractMonster monster,float x,float y){
        monster.drawX = (float)Settings.WIDTH * 0.75F + x * Settings.xScale;
        monster.drawY = AbstractDungeon.floorY + y * Settings.yScale;
        return monsterModifier(mul,monster);
    }

    public static AbstractMonster monsterModifier(float mul,AbstractMonster monster){
        return monsterModifier(monster,(int) (monster.currentHealth*mul));
    }

    public static AbstractMonster monsterModifier(AbstractMonster monster,int health){
        monster.currentHealth = health;
        monster.maxHealth = monster.currentHealth;
        return monster;
    }

    public static AbstractMonster addPowerStarts(AbstractMonster monster, AbstractPower power){
        monster.powers.add(power);
        return monster;
    }

    public static AbstractMonster spawnGremlin(float x, float y) {
        ArrayList<String> gremlinPool = new ArrayList();
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinWarrior");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinThief");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinFat");
        gremlinPool.add("GremlinTsundere");
        gremlinPool.add("GremlinWizard");
        return MonsterHelper.getGremlin((String)gremlinPool.get(AbstractDungeon.miscRng.random(0, gremlinPool.size() - 1)), x, y);
    }

    //生成
    public static ArrayList<AbstractRelic> getRebuildRelics(AbstractRelic relic,int size){
        ArrayList<AbstractRelic> relics = new ArrayList<>();
        AbstractRelic.RelicTier tier = relic.tier;
        if(tier == AbstractRelic.RelicTier.STARTER)
            tier = AbstractRelic.RelicTier.COMMON;
        else if(tier != AbstractRelic.RelicTier.COMMON && tier != AbstractRelic.RelicTier.UNCOMMON && tier != AbstractRelic.RelicTier.RARE && tier != AbstractRelic.RelicTier.BOSS)
            tier = AbstractRelic.RelicTier.UNCOMMON;
        for(int i =0;i<size;i++) {
            AbstractRelic.RelicTier tmpRelicTier = tier;
            if (tmpRelicTier != AbstractRelic.RelicTier.BOSS) {
                boolean upgraded = AbstractDungeon.relicRng.randomBoolean(0.1F);
                if (upgraded && tier == AbstractRelic.RelicTier.COMMON)
                    tmpRelicTier = AbstractRelic.RelicTier.UNCOMMON;
                else if (upgraded && tier == AbstractRelic.RelicTier.UNCOMMON)
                    tmpRelicTier = AbstractRelic.RelicTier.RARE;
            }
            relics.add(AbstractDungeon.returnRandomScreenlessRelic(tmpRelicTier));
        }
        return relics;
    }

    public static ArrayList<AbstractCard> getRebuildCards(AbstractCard card, int size){
        //update remove sameID 2025/6/7
        ArrayList<AbstractCard> common = new ArrayList<>(AbstractDungeon.commonCardPool.group);
        common.removeIf(c -> c.cardID.equals(card.cardID));
        Collections.shuffle(common,AbstractDungeon.cardRandomRng.random);
        ArrayList<AbstractCard> uncommon = new ArrayList<>(AbstractDungeon.uncommonCardPool.group);
        uncommon.removeIf(c -> c.cardID.equals(card.cardID));
        Collections.shuffle(uncommon,AbstractDungeon.cardRandomRng.random);
        ArrayList<AbstractCard> rare = new ArrayList<>(AbstractDungeon.rareCardPool.group);
        rare.removeIf(c -> c.cardID.equals(card.cardID));
        Collections.shuffle(rare,AbstractDungeon.cardRandomRng.random);

        ArrayList<AbstractCard> returnCards = new ArrayList<>();
        AbstractCard.CardRarity rarity = card.rarity;
        if(rarity == AbstractCard.CardRarity.BASIC)
            rarity = AbstractCard.CardRarity.COMMON;
        else if(rarity != AbstractCard.CardRarity.COMMON && rarity!= AbstractCard.CardRarity.UNCOMMON && rarity != AbstractCard.CardRarity.RARE)
            rarity = AbstractCard.CardRarity.UNCOMMON;
        for(int i =0;i<size;i++){
            AbstractCard.CardRarity tmpRarity = rarity;
            boolean upgraded = AbstractDungeon.relicRng.randomBoolean(0.1F);
            if(upgraded && rarity== AbstractCard.CardRarity.COMMON)
                tmpRarity = AbstractCard.CardRarity.UNCOMMON;
            else if(upgraded && rarity== AbstractCard.CardRarity.UNCOMMON)
                tmpRarity = AbstractCard.CardRarity.RARE;
            if(tmpRarity == AbstractCard.CardRarity.COMMON){
                returnCards.add(common.remove(0));
            }
            else if(tmpRarity == AbstractCard.CardRarity.UNCOMMON){
                returnCards.add(uncommon.remove(0));
            }
            else {
                returnCards.add(rare.remove(0));
            }
        }
        return returnCards;
    }

    static {
        blights = new ArrayList<>();
        blights.add(AbnormalAirpressure01.ID);
        blights.add(ConvergentConsumption01.ID);
        blights.add(Dequantization01.ID);
        blights.add(EmotionalEntity01.ID);
        blights.add(NonlinearMovement01.ID);
        blights.add(PansocialParadox01.ID);
        blights.add(SubstantialCollapse01.ID);
        blights.add(TriggeringInjury01.ID);
    }

}
