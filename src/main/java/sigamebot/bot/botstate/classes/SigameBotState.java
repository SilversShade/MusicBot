package sigamebot.bot.botstate.classes;

import sigamebot.bot.botstate.IState;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.settings.SettingsOptions;

public class SigameBotState {
    private IState state = BotStates.DEFAULT_STATE;

    public static SettingsOptions currentSettingsOption = SettingsOptions.NONE;
    public IState getState(){
        return state;
    }
    public void next(IState nextState){
        state = nextState;
    }
}
