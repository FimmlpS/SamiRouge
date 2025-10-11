package SamiRouge.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnumPatch {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen DECLARE_VIEW;
}
