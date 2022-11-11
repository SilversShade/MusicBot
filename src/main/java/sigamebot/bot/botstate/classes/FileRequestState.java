package sigamebot.bot.botstate.classes;

import sigamebot.bot.botstate.ITelegramBotState;
import sigamebot.bot.botstate.SigameBotFileRequestStage;

public class FileRequestState {
    private ITelegramBotState state = SigameBotFileRequestStage.DEFAULT_STATE;
    public ITelegramBotState getState(){
        return state;
    }
    public void next(){
        state = state.nextState();
    }
}
