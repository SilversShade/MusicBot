package sigamebot.bot.botstate.automats;

import sigamebot.bot.botstate.ITelegramBotStage;
import sigamebot.bot.botstate.LineRequestStage;

public class LineRequest implements IAutomats{
    private ITelegramBotStage state = LineRequestStage.DEFAULT_STATE;
    @Override
    public ITelegramBotStage getStage(){
        return state;
    }
    @Override
    public void next(){
        state = state.nextState();
    }
}