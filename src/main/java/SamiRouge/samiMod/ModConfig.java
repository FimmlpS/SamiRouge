package SamiRouge.samiMod;

import basemod.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.options.DropdownMenu;
import com.megacrit.cardcrawl.screens.options.DropdownMenuListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.function.BiConsumer;

public class ModConfig {
    public static final String IRREVERSIBLE_BLV = "samirg:IRREVERSIBLE_BLV";
    public static final String IRREVERSIBLE_BLV_ATTACK = "samirg:IRREVERSIBLE_BLV_ATTACK";
    public static final String EXTRA_FANS = "samirg:EXTRA_FANS";
    public static final String ALL_FANS = "samirg:ALL_FANS";
    public static final String ANTI_FALL_GLV = "samirg:ANTI_FALL_GLV";
    public static final String ROAD_SPAWN_GLV = "samirg:ROAD_SPAWN_GLV";

    public static ArrayList<String> options;
    public static ArrayList<String> optionsAnti;
    public static ArrayList<String> optionsRoad;
    public static UIStrings uiStrings;

    public static SpireConfig config = null;
    private static Properties defaultSettings = new Properties();
    private static ModPanel settingsPanel;

    public static final int blv_blv = 5;
    public static int ir_blv;
    public static int ir_blv_attack;
    public static boolean extra_fans;
    public static boolean all_fans;
    public static int anti_fall_glv; //2~5 * 5% default-2
    public static int road_spawn_glv; //3~6 * 5% default-3

    public static void initModSettings(){
        defaultSettings.setProperty(IRREVERSIBLE_BLV,String.valueOf(2));
        defaultSettings.setProperty(IRREVERSIBLE_BLV_ATTACK,String.valueOf(2));
        defaultSettings.setProperty(EXTRA_FANS,String.valueOf(false));
        defaultSettings.setProperty(ALL_FANS,String.valueOf(false));
        defaultSettings.setProperty(ANTI_FALL_GLV,String.valueOf(2));
        defaultSettings.setProperty(ROAD_SPAWN_GLV,String.valueOf(3));
        try {
            config = new SpireConfig("SamiRouge_FimmlpS","Common",defaultSettings);
            config.load();
            ir_blv = config.getInt(IRREVERSIBLE_BLV);
            ir_blv_attack = config.getInt(IRREVERSIBLE_BLV_ATTACK);
            extra_fans = config.getBool(EXTRA_FANS);
            all_fans = config.getBool(ALL_FANS);
            anti_fall_glv = config.getInt(ANTI_FALL_GLV);
            road_spawn_glv = config.getInt(ROAD_SPAWN_GLV);
        }
        catch (Exception e){
            //todo
        }
    }

    public static void initModConfigMenu(){
        uiStrings = CardCrawlGame.languagePack.getUIString("samirg:Config");
        options = new ArrayList<>();
        optionsAnti = new ArrayList<>();
        optionsRoad = new ArrayList<>();
        for(int i =0;i<9;i++){
            options.add(i*blv_blv + uiStrings.TEXT[5]);
        }
        for(int i =2;i<6;i++){
            optionsAnti.add(i*blv_blv + uiStrings.TEXT[5]);
            optionsRoad.add((i+1)*blv_blv + uiStrings.TEXT[5]);
        }

        settingsPanel = new ModPanel();
        addEnableMenu();
        String modConfDesc = uiStrings.TEXT[0];
        Texture badge  = ImageMaster.loadImage("SamiRougeResources/img/powers_SamiRouge/Rootage_32.png");
        BaseMod.registerModBadge(badge,"samirg","FimmlpS",modConfDesc,settingsPanel);
    }

    private static void addEnableMenu(){
        ModDropDownMenu drop = new ModDropDownMenu(380F,720F,(i,s)->{
            ir_blv = i;
            config.setInt(IRREVERSIBLE_BLV,ir_blv);
            try {
                config.save();
            }
            catch (IOException e){
                //todo
            }
        }, new ArrayList<>(options),FontHelper.charDescFont,Settings.CREAM_COLOR,uiStrings.TEXT[3]);
        drop.menu.setSelectedIndex(ir_blv);

        ModDropDownMenu drop2 = new ModDropDownMenu(380F,635F,(i,s)->{
            ir_blv_attack = i;
            config.setInt(IRREVERSIBLE_BLV_ATTACK,ir_blv_attack);
            try {
                config.save();
            }
            catch (IOException e){
                //todo
            }
        }, new ArrayList<>(options),FontHelper.charDescFont,Settings.CREAM_COLOR,uiStrings.TEXT[4]);
        drop2.menu.setSelectedIndex(ir_blv_attack);

        ModLabeledToggleButton btn = new ModLabeledToggleButton(uiStrings.TEXT[1],380F,550F, Settings.CREAM_COLOR, FontHelper.charDescFont,extra_fans,settingsPanel,modLabel -> {},modToggleButton -> {
            extra_fans = modToggleButton.enabled;
            config.setBool(EXTRA_FANS,extra_fans);
            try {
                config.save();
            }
            catch (IOException e){
                //todo
            }
        });

        ModLabeledToggleButton btn2 = new ModLabeledToggleButton(uiStrings.TEXT[2],380F,500F, Settings.CREAM_COLOR, FontHelper.charDescFont,all_fans,settingsPanel,modLabel -> {},modToggleButton -> {
            all_fans = modToggleButton.enabled;
            config.setBool(ALL_FANS,all_fans);
            try {
                config.save();
            }
            catch (IOException e){
                //todo
            }


        });

        ModDropDownMenu drop3 = new ModDropDownMenu(380F,450F,(i,s)->{
            anti_fall_glv = i+2;
            config.setInt(ANTI_FALL_GLV,anti_fall_glv);
            try {
                config.save();
            }
            catch (IOException e){
                //todo
            }
        }, new ArrayList<>(optionsAnti),FontHelper.charDescFont,Settings.CREAM_COLOR,uiStrings.TEXT[6]);
        drop3.menu.setSelectedIndex(anti_fall_glv-2);

        ModDropDownMenu drop4 = new ModDropDownMenu(380F,365F,(i,s)->{
            road_spawn_glv = i+3;
            config.setInt(ROAD_SPAWN_GLV,road_spawn_glv);
            try {
                config.save();
            }
            catch (IOException e){
                //todo
            }
        }, new ArrayList<>(optionsRoad),FontHelper.charDescFont,Settings.CREAM_COLOR,uiStrings.TEXT[7]);
        drop4.menu.setSelectedIndex(road_spawn_glv-3);

        settingsPanel.addUIElement(drop4);
        settingsPanel.addUIElement(drop3);
        settingsPanel.addUIElement(btn);
        settingsPanel.addUIElement(btn2);
        settingsPanel.addUIElement(drop2);
        settingsPanel.addUIElement(drop);

    }

    public static class ModDropDownMenu implements IUIElement, DropdownMenuListener {
        private float xPos;
        private float yPos;
        private final BiConsumer<Integer,String> onChangeSelectionTo;
        private final DropdownMenu menu;
        private final ModLabel textLabel;
        public float renderLayer;

        public ModDropDownMenu(float x,float y,BiConsumer<Integer,String> listener, ArrayList<String> options, BitmapFont font, Color textColor,String labelText){
            onChangeSelectionTo = listener;
            menu = new DropdownMenu(this,options,font,textColor);
            xPos = x;
            yPos = y;
            renderLayer = 1;
            this.textLabel = new ModLabel(labelText,xPos,yPos+20F,textColor,font,ModConfig.settingsPanel,(modLabel -> {}));
        }

        @Override
        public void render(SpriteBatch sb){
            sb.setColor(Color.WHITE);
            menu.render(sb,xPos*Settings.xScale,yPos*Settings.yScale);
            this.textLabel.render(sb);
        }

        @Override
        public void update() {
            menu.update();
            textLabel.update();
        }

        @Override
        public int renderLayer() {
            return 1;
        }

        @Override
        public int updateOrder() {
            return 1;
        }

        @Override
        public void changedSelectionTo(DropdownMenu dropdownMenu, int i, String s) {
            this.onChangeSelectionTo.accept(i,s);
        }

        @Override
        public void set(float xPos, float yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }

        @Override
        public float getX() {
            return this.xPos;
        }

        @Override
        public void setX(float xPos) {
            this.xPos = xPos;
            this.textLabel.setX(xPos);
        }

        @Override
        public float getY() {
            return this.yPos;
        }

        @Override
        public void setY(float yPos) {
            this.yPos = yPos;
            this.textLabel.setY(yPos + 20.0F);
        }
    }
}
