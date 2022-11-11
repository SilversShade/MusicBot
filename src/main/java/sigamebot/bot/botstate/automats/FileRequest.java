
package sigamebot.bot.botstate.automats;

import sigamebot.bot.botstate.FileRequestStage;
import sigamebot.bot.botstate.ITelegramBotStage;

public class FileRequest implements IAutomats{
    private ITelegramBotStage state = FileRequestStage.DEFAULT_STATE;
    @Override
    public ITelegramBotStage getStage(){
        return state;
    }
    @Override
    public void next(){
        state = state.nextState();
    }
}