package SamiRouge.samiMod;

import SamiRouge.actions.ExhaustToDrawAction;
import SamiRouge.actions.UniversalCheckAction;
import SamiRouge.cards.ciphertext.layout.*;
import SamiRouge.cards.ciphertext.reason.C22;
import SamiRouge.cards.curse.Coldness_SamiRouge;
import SamiRouge.cards.curse.Freeze_SamiRouge;
import SamiRouge.cards.curse.Hypothermia_SamiRouge;
import SamiRouge.cards.power.ColdProtection_SamiRouge;
import SamiRouge.cards.skill.ActivateMatrix_SamiRouge;
import SamiRouge.cards.skill.PastWeaveFuture_SamiRouge;
import SamiRouge.dungeons.TheSami;
import SamiRouge.events.*;
import SamiRouge.helper.AltMonsterHelper;
import SamiRouge.helper.DeclareHelper;
import SamiRouge.modifiers.ForeverModifier;
import SamiRouge.monsters.*;
import SamiRouge.monsters.alts.*;
import SamiRouge.patches.SamiTreeHolePatch;
import SamiRouge.patches.TreeHoleSami;
import SamiRouge.powers.AbsorbFirePower;
import SamiRouge.relics.RouteMap;
import TreeHole.mod.TreeHoleMod;
import basemod.BaseMod;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.CardModifierManager;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.SlaversCollar;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@SpireInitializer
public class SamiRougeMod implements PostInitializeSubscriber,PostBattleSubscriber,OnStartBattleSubscriber,OnPlayerTurnStartSubscriber,ISubscriber, AddAudioSubscriber, EditCardsSubscriber, EditKeywordsSubscriber, EditStringsSubscriber,EditRelicsSubscriber {
    public SamiRougeMod(){
        BaseMod.subscribe(this);
        ModConfig.initModSettings();
    }

    private static final Logger logger = LogManager.getLogger(SamiRougeMod.class);

    public static void initialize(){
        new SamiRougeMod();
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("samirg:Declare","SamiRougeResources/audio/declare.ogg");
        BaseMod.addAudio("samirg:Together","SamiRougeResources/audio/together.ogg");
    }

    public static void logSomething(String mes){
        logger.info(mes);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Coldness_SamiRouge());
        BaseMod.addCard(new Hypothermia_SamiRouge());
        BaseMod.addCard(new Freeze_SamiRouge());
        BaseMod.addCard(new ActivateMatrix_SamiRouge());

        //NO COLOR FOR SHOP
        BaseMod.addCard(new ColdProtection_SamiRouge());
        BaseMod.addCard(new PastWeaveFuture_SamiRouge());

        //CIPHERTEXT
        for(AbstractCard c:DeclareHelper.getCardsToAdd()){
            BaseMod.addCard(c);
        }
    }

    @Override
    public void receivePostInitialize() {
        //注册树洞
        TreeHoleMod.registerTreeHole(TheSami.ID,new TreeHoleSami());
        initializeMonsters();
        initializeEvents();
        ModConfig.initModConfigMenu();
    }

    private void initializeEvents(){
        BaseMod.addEvent((new AddEventParams.Builder(BeforeWoodCrack.ID,BeforeWoodCrack.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(SadCage.ID,SadCage.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(NoMountains.ID,NoMountains.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(Change.ID,Change.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(true)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(FarSee.ID, FarSee.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(DarkRoom.ID, DarkRoom.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(Rain.ID, Rain.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

        BaseMod.addEvent((new AddEventParams.Builder(CureRitual.ID, CureRitual.class))
                .eventType(EventUtils.EventType.ONE_TIME)
                .dungeonID(Exordium.ID)
                .dungeonID(TheCity.ID)
                .dungeonID(TheBeyond.ID)
                .dungeonID(TheSami.ID)
                .endsWithRewardsUI(false)
                .create());

    }

    private void initializeMonsters(){
        String[] names = CardCrawlGame.languagePack.getUIString("samirg:MonsterNames").TEXT;
        String[] extraNames = CardCrawlGame.languagePack.getUIString("samirg:MonsterNames").EXTRA_TEXT;
        //BOSS - Will of sami
        BaseMod.addMonster(WillOfSami.ID,names[0],()->{
            return new MonsterGroup(new AbstractMonster[]{new WillOfSami(0F,0F)});
        });
        BaseMod.addBoss("samirg:UnknownMotherGoose", WillOfSami.ID,WillOfSami.ICON,WillOfSami.ICON_O);

        BaseMod.addMonster(WoodCrack.ID,names[1],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new SamiWarSoldier(-480F,0,true,new Coldness_SamiRouge()),
                    new SamiWarSoldier(-190F,0,true,new Hypothermia_SamiRouge()),
                    new WoodCrack(150F,0F)
            });
        });
        BaseMod.addBoss("samirg:UnknownMotherGoose",WoodCrack.ID,WoodCrack.ICON,WoodCrack.ICON_O);

        BaseMod.addMonster("samirg:ThreeWarSoldier",names[2],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new SamiWarSoldier(-450F,0,false,new Coldness_SamiRouge()),
                    new SamiWarSoldier(-180F,0,true,new Hypothermia_SamiRouge()),
                    new SamiWarSoldier(90F,0,false,new Freeze_SamiRouge())
            });
        });

        BaseMod.addMonster(Cresson.ID,names[3],()->{
            return new MonsterGroup(new AbstractMonster[]{new Cresson(-200F,150F)});
        });
        BaseMod.addBoss("samirg:UnknownMotherGoose", Cresson.ID,Cresson.ICON,Cresson.ICON_O);

        BaseMod.addMonster("samirg:DoubleTimeEater",names[4],()->{
            return new MonsterGroup(new AbstractMonster[]{
               SamiRougeHelper.monsterModifier(0.25F,new SixTimeEater(0),-440F,30F),
                    SamiRougeHelper.monsterModifier(0.25F,new SixTimeEater(8),120F,30F),
            });
        });

        BaseMod.addMonster("samirg:TwoDarklings",names[5],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Darkling(-300.0F, 10.0F),
                    new Darkling(0F, 30.0F)
            });
        });

        BaseMod.addMonster("samirg:ExordiumElites",names[6],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Sentry(-330F,25F),
                    SamiRougeHelper.monsterModifier(0.4F,new Lagavulin(true),-85F,10F),
                    SamiRougeHelper.monsterModifier(0.4F,new GremlinNob(240F,10F,false))
            });
        });

        BaseMod.addMonster("samirg:TheCityElites",names[7],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Taskmaster(-250F,15F),
                    SamiRougeHelper.monsterModifier(0.5F,new BookOfStabbing(),100F,5F)
            });
        });

        BaseMod.addMonster("samirg:TheBeyondElites",names[8],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    SamiRougeHelper.monsterModifier(0.5F,new GiantHead(),-320F,0F),
                    SamiRougeHelper.monsterModifier(0.5F,new Nemesis(),100F,0F)
            });
        });

        BaseMod.addMonster("samirg:SmallBigJawWorm",names[9],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new JawWorm(-320F,0F,true),
                    SamiRougeHelper.monsterModifier(0.5F,new Maw(100F,0F))
            });
        });

        BaseMod.addMonster("samirg:ThreeGremlinLeader",names[10],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    SamiRougeHelper.spawnGremlin(ThreeGremlinLeader.POSX[0], ThreeGremlinLeader.POSY[0]),
                    SamiRougeHelper.spawnGremlin(ThreeGremlinLeader.POSX[1], ThreeGremlinLeader.POSY[1]),
                    SamiRougeHelper.spawnGremlin(ThreeGremlinLeader.POSX[2], ThreeGremlinLeader.POSY[2]),
                    new ThreeGremlinLeader()
            });
        });

        //UPDATE

        BaseMod.addMonster("samirg:CurseNemesis",names[11],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new CurseNemesis()
            });
        });

        BaseMod.addMonster("samirg:TheCityStrong",names[12],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    SamiRougeHelper.monsterModifier(1F,new ShelledParasite(-320F,0F)),
                    SamiRougeHelper.monsterModifier(1F,new SnakePlant(100F,0F))
            });
        });

        BaseMod.addMonster("samirg:FourGremlinWizard",names[13],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    SamiRougeHelper.monsterModifier(1F, new GremlinThief(-320F, 25F)),
                    SamiRougeHelper.monsterModifier(1F, new GremlinWizard(-160F, -12F)),
                    SamiRougeHelper.monsterModifier(1F, new GremlinWizard(25F, -35F)),
                    SamiRougeHelper.monsterModifier(1F, new GremlinWizard(205F, 40F))
            });
        });

        BaseMod.addMonster("samirg:Flking",names[14],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Flking(0F,0F)
            });
        });

        BaseMod.addMonster("samirg:Smgia",names[15],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Smgia(0F,0F)
            });
        });

        BaseMod.addMonster("samirg:Smgrd",names[16],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Smgrd(0F,0F)
            });
        });

        BaseMod.addMonster(Smkght.BOSS_ID,names[17],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Smkght(-1000F,10F),
                    new Smlion(70,10F)
            });
        });
        BaseMod.addBoss("samirg:UnknownMotherGoose", Smkght.BOSS_ID,Smkght.ICON,Smkght.ICON_O);

        BaseMod.addMonster("samirg:SandIllusion",names[18],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Redace(-120F,0),
                    new Redman(160F,5)
            });
        });

        BaseMod.addMonster("samirg:TwoChosen",names[19],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    SamiRougeHelper.monsterModifier(0.4F,new Chosen(-205F,5F)),
                    SamiRougeHelper.monsterModifier(0.4F,new Chosen(120F,10F))
            });
        });

        BaseMod.addMonster("samirg:HeroNoName",names[20],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new NamelessHero(0F, 5F)
            });
        });

        BaseMod.addMonster("samirg:SpecialSlime",names[21],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new BossAcidSlime()
            });
        });

        BaseMod.addMonster(AltMonsterHelper.BANDITS_AND_SLAVERS,names[22],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new ThreeBandits(-1000F,0F),
                    new ThreeSlavers(70F,0F)
            });
        });

        BaseMod.addMonster("samirg:Smsha",names[23],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Smsha(0F,0F)
            });
        });

        //added EXTRA
        BaseMod.addMonster(AltMonsterHelper.ONE_SLAVER,extraNames[0],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Taskmaster(0F,1F)
            });
        });

        BaseMod.addMonster(AltMonsterHelper.ONE_BEAR,extraNames[1],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new BanditBear(0F,1F)
            });
        });

        BaseMod.addMonster(AltMonsterHelper.ONE_DAGGERS,extraNames[2],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    SamiRougeHelper.monsterModifier(0.8F,new SnakeDagger(-160F,1F)),
                    SamiRougeHelper.monsterModifier(0.8F,new SnakeDagger(-20F,1F))
            });
        });

        BaseMod.addMonster(AltMonsterHelper.TWO_SOLDIER,extraNames[3],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new SamiWarSoldier(0F,1F)
            });
        });

        BaseMod.addMonster(AltMonsterHelper.TWO_S_AND_S,extraNames[4],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Smsbr(-120F,0),
                    new Smwiz(160F,5)
            });
        });

        BaseMod.addMonster(AltMonsterHelper.TWO_STRONG_BUG,extraNames[5],()->{
            AbstractMonster m = new LouseNormal(0F,0F);
            m.powers.add(new PlatedArmorPower(m,14));
            m.powers.add(new StrengthPower(m,5));
            return new MonsterGroup(new AbstractMonster[]{
               SamiRougeHelper.monsterModifier(5F,m)
            });
        });

        BaseMod.addMonster(AltMonsterHelper.THREE_B_C_T_C_B,extraNames[6],()->{
            return new MonsterGroup(new AbstractMonster[]{
                    new Byrd(-400F,0F),
                    new Cultist(-200F,0F),
                    new Chosen(0F,0F),
                    new Healer(200F,0F)
            });
        });

    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String lang = "en";
        if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
            lang = "zh";

        }
        else if(Settings.language == Settings.GameLanguage.KOR){
            lang = "kr";
        }
        else {
            lang = "en";
        }

        String json = Gdx.files.internal("SamiRougeResources/localization/ThMod_SamiRouge_keywords-"+lang+".json").readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = (Keyword[])gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            Keyword[] var5 = keywords;
            int var6 = keywords.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Keyword keyword = var5[var7];
                BaseMod.addKeyword("samirg", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditStrings() {
        String lang = "";
        String relic = "",card = "",power= "",potion="",event="",character="",ui="",monster="",blight = "",score = "";
        String fore = "SamiRougeResources/localization/";
        String mid = "/ThMod_SamiRouge_";
        String back = ".json";
        if(Settings.language== Settings.GameLanguage.ZHS||Settings.language== Settings.GameLanguage.ZHT){
            lang="zh";
        }
        else if(Settings.language== Settings.GameLanguage.KOR){
            lang="kr";
        }
        else{
            lang="en";
        }
        card = fore+lang+mid+"cards-"+lang+back;
        relic = fore+lang+mid+"relics-"+lang+back;
        power = fore+lang+mid+"powers-"+lang+back;
        potion = fore+lang+mid+"potions-"+lang+back;
        event = fore+lang+mid+"events-"+lang+back;
        monster = fore+lang+mid+"monsters-"+lang+back;
        character = fore+lang+mid+"characters-"+lang+back;
        ui = fore+lang+mid+"ui-"+lang+back;
        blight = fore+lang+mid+"blights-"+lang+back;
        score = fore+lang+mid+"scores-"+lang+back;
        String relicStrings = Gdx.files.internal(relic).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String cardStrings = Gdx.files.internal(card).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String powerStrings = Gdx.files.internal(power).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        //String potionStrings = Gdx.files.internal(potion).readString(String.valueOf(StandardCharsets.UTF_8));
        //BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String eventStrings = Gdx.files.internal(event).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
        String monsterStrings = Gdx.files.internal(monster).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(MonsterStrings.class,monsterStrings);
        //String characterStrings = Gdx.files.internal(character).readString(String.valueOf(StandardCharsets.UTF_8));
        //BaseMod.loadCustomStrings(CharacterStrings.class,characterStrings);
        String uiStrings = Gdx.files.internal(ui).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class,uiStrings);
        String blightStrings = Gdx.files.internal(blight).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(BlightStrings.class,blightStrings);
        String scoreStrings = Gdx.files.internal(score).readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(ScoreBonusStrings.class,scoreStrings);
    }


    @Override
    public void receiveEditRelics() {
        for(AbstractRelic r:SamiRougeHelper.getRelicsToAdd()){
            BaseMod.addRelic(r, RelicType.SHARED);
            RelicLibrary.getRelic(r.relicId).isSeen = true;
            UnlockTracker.relicSeenPref.putInteger(r.relicId, 1);
        }
        UnlockTracker.relicSeenPref.flush();
    }

    @Override
    public void receiveOnPlayerTurnStart() {
        DeclareHelper.atTurnStart();

        if(AbstractDungeon.id.equals(TheSami.ID)||ModConfig.all_fans){
            AbstractDungeon.actionManager.addToBottom(new UniversalCheckAction());
        }

        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (CardModifierManager.hasModifier(c, ForeverModifier.ID)) {
                AbstractDungeon.actionManager.addToTop(new ExhaustToDrawAction(c, true));
            }
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        AbsorbFirePower.someOneOwned = false;
        DeclareHelper.atBattleStart();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        DeclareHelper.atBattleEnd();
    }
}
