package SamiRouge.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CtBehavior;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ScorePatch {
    public static final ScoreBonusStrings samiTH;
    public static final ScoreBonusStrings bossTH;

    public static final ScoreBonusStrings ttwtmTH;
    public static final ScoreBonusStrings wfTH;
    public static final ScoreBonusStrings liaaTH;
    public static final ScoreBonusStrings whtTH;
    public static final ScoreBonusStrings wbfweTH;

    @SpirePatch(clz = GameOverScreen.class,method = "checkScoreBonus")
    public static class CheckScorePatch{
        @SpireInsertPatch(locator = Locator.class, localvars = {"points"})
        public static void Insert(boolean victory, @ByRef int[] points) {
            points[0] += 100*SamiTreeHolePatch.treeHoleEntered;
            points[0] += 150*SamiTreeHolePatch.bossKilled;
            if(SamiTreeHolePatch.toTalkWithMountains){
                points[0] += 200;
            }
            if(SamiTreeHolePatch.winterFall){
                points[0] += 200;
            }
            if(SamiTreeHolePatch.longIntoAnAbyss){
                points[0] += 300;
            }
            if(SamiTreeHolePatch.withHerTalk){
                points[0] += 200;
            }
            if(SamiTreeHolePatch.whatBeginsFollowsWhatEnds){
                points[0] += 500;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctBehavior, (Matcher)methodCallMatcher);
            }
        }
    }

    @SpirePatch(clz = VictoryScreen.class, method = "createGameOverStats")
    public static class VictoryStatsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(VictoryScreen _inst) {
            ScorePatch.addStats((GameOverScreen)_inst);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(VictoryScreen.class, "IS_POOPY");
                return LineFinder.findInOrder(ctBehavior, (Matcher)fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = DeathScreen.class, method = "createGameOverStats")
    public static class DeathStatsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(DeathScreen _inst) {
            ScorePatch.addStats((GameOverScreen)_inst);
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(DeathScreen.class, "IS_POOPY");
                return LineFinder.findInOrder(ctBehavior, (Matcher)fieldAccessMatcher);
            }
        }
    }

    private static void addStats(GameOverScreen _inst) {
        try {
            Field stats = GameOverScreen.class.getDeclaredField("stats");
            stats.setAccessible(true);
            if (SamiTreeHolePatch.treeHoleEntered>0)
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(samiTH.NAME +"("+SamiTreeHolePatch.treeHoleEntered+")", samiTH.DESCRIPTIONS[0], Integer.toString(100*SamiTreeHolePatch.treeHoleEntered)));
            if(SamiTreeHolePatch.bossKilled>0)
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(bossTH.NAME+"("+SamiTreeHolePatch.bossKilled+")",bossTH.DESCRIPTIONS[0],Integer.toString(150*SamiTreeHolePatch.bossKilled)));
            if(SamiTreeHolePatch.toTalkWithMountains){
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(ttwtmTH.NAME, ttwtmTH.DESCRIPTIONS[0],Integer.toString(200)));
            }
            if(SamiTreeHolePatch.winterFall){
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(wfTH.NAME, wfTH.DESCRIPTIONS[0],Integer.toString(200)));
            }
            if(SamiTreeHolePatch.longIntoAnAbyss){
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(liaaTH.NAME, liaaTH.DESCRIPTIONS[0],Integer.toString(300)));
            }
            if(SamiTreeHolePatch.withHerTalk){
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(whtTH.NAME, whtTH.DESCRIPTIONS[0],Integer.toString(200)));
            }
            if(SamiTreeHolePatch.whatBeginsFollowsWhatEnds){
                ((ArrayList<GameOverStat>)stats.get(_inst)).add(new GameOverStat(wbfweTH.NAME, wbfweTH.DESCRIPTIONS[0],Integer.toString(500)));
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to set game over stats.", e);
        }
    }

    @SpirePatch(clz = GameOverStat.class,method = SpirePatch.CLASS)
    public static class GameOverStatField {
        public static SpireField<Color> preColor = new SpireField<>(() -> Color.WHITE);
    }

    @SpirePatch(clz = GameOverStat.class,method = "render")
    public static class GameOverStatPatch {
        @SpirePrefixPatch
        public static void Prefix(GameOverStat _inst, SpriteBatch sb, float x, float y) {
            if(_inst.label != null && nameContains(_inst.label)){
                Color current = ReflectionHacks.getPrivate(_inst, GameOverStat.class, "color");
                GameOverStatField.preColor.set(_inst,current);
                Color golden = Color.GOLDENROD.cpy();
                golden.a = current.a;
                ReflectionHacks.setPrivate(_inst, GameOverStat.class, "color", golden);
            }
        }

        @SpirePostfixPatch
        public static void Postfix(GameOverStat _inst, SpriteBatch sb, float x, float y) {
            Color pre = GameOverStatField.preColor.get(_inst);
            ReflectionHacks.setPrivate(_inst, GameOverStat.class, "color",pre);
        }
    }

    private static boolean nameContains(String label){
        return label.equals(ttwtmTH.NAME) || label.equals(wfTH.NAME) || label.equals(liaaTH.NAME) || label.equals(whtTH.NAME) || label.equals(wbfweTH.NAME);
    }


    static {
        samiTH = CardCrawlGame.languagePack.getScoreString("samirg:SamiTreeHole");
        bossTH = CardCrawlGame.languagePack.getScoreString("samirg:SamiTreeHoleBoss");

        ttwtmTH = CardCrawlGame.languagePack.getScoreString("samirg:ToTalkWithMountains");
        wfTH = CardCrawlGame.languagePack.getScoreString("samirg:WinterFall");
        liaaTH = CardCrawlGame.languagePack.getScoreString("samirg:LongIntoAnAbyss");
        whtTH = CardCrawlGame.languagePack.getScoreString("samirg:WithHerTalk");
        wbfweTH = CardCrawlGame.languagePack.getScoreString("samirg:WhatBeginsFollowsWhatEnds");
    }
}
