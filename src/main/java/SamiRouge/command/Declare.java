package SamiRouge.command;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

import java.util.ArrayList;

public class Declare extends ConsoleCommand {
    public Declare() {
        this.requiresPlayer = true;
        this.minExtraTokens = 1;
        this.maxExtraTokens = 1;
        this.simpleCheck = true;
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("addAll");
        result.add("removeAll");
        return result;
    }

    @Override
    protected void execute(String[] strings, int i) {
        if(strings[1].equals("addAll")) {
            for (AbstractCipherTextCard card : DeclareHelper.allCards) {
                if(card.cipherText.type == CipherText.CipherType.Layout){
                    DeclareHelper.layout.add((AbstractCipherTextCard) card.makeCopy());
                }
                else if(card.cipherText.type == CipherText.CipherType.Reason){
                    DeclareHelper.reason.add((AbstractCipherTextCard) card.makeCopy());
                }
            }
        }
        else if(strings[1].equals("removeAll")) {
            DeclareHelper.layout.clear();
            DeclareHelper.reason.clear();
        }
        else
            cmdDeclareHelp();
    }

    @Override
    protected void errorMsg() {
        cmdDeclareHelp();
    }

    public static void cmdDeclareHelp(){
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* addAll");
        DevConsole.log("* removeAll");
    }
}
