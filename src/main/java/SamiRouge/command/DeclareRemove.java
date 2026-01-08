package SamiRouge.command;

import SamiRouge.helper.DeclareHelper;
import basemod.devcommands.ConsoleCommand;

public class DeclareRemove extends ConsoleCommand {
    public DeclareRemove() {
        this.requiresPlayer = true;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] strings, int i) {
        DeclareHelper.layout.clear();
        DeclareHelper.reason.clear();
    }

    @Override
    protected void errorMsg() {
        Declare.cmdDeclareHelp();
    }
}
