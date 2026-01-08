package SamiRouge.command;

import SamiRouge.cards.ciphertext.AbstractCipherTextCard;
import SamiRouge.cards.ciphertext.CipherText;
import SamiRouge.helper.DeclareHelper;
import basemod.devcommands.ConsoleCommand;

public class DeclareAdd extends ConsoleCommand {
    public DeclareAdd() {
        this.requiresPlayer = true;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] strings, int i) {
        for (AbstractCipherTextCard card : DeclareHelper.allCards) {
            if(card.cipherText.type == CipherText.CipherType.Layout){
                DeclareHelper.layout.add((AbstractCipherTextCard) card.makeCopy());
            }
            else if(card.cipherText.type == CipherText.CipherType.Reason){
                DeclareHelper.reason.add((AbstractCipherTextCard) card.makeCopy());
            }
        }
    }

    @Override
    protected void errorMsg() {
        Declare.cmdDeclareHelp();
    }
}

