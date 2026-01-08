package SamiRouge.patches;

import SamiRouge.command.Declare;
import basemod.devcommands.ConsoleCommand;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;

public class CommandPatch {
    @SpirePatch(clz = ConsoleCommand.class,method = "initialize")
    public static class ConsoleCommandPatch {
        @SpirePrefixPatch
        public static void Prefix() {
            ConsoleCommand.addCommand("declare", Declare.class);
        }
    }
}
