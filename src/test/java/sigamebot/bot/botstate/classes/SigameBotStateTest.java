package sigamebot.bot.botstate.classes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sigamebot.bot.botstate.BotStates;

public class SigameBotStateTest {

    private static final SigameBotState sigameBotState = new SigameBotState();

    @Test
    void methodNextSwitchesStatesCorrectly() {
        sigameBotState.next(BotStates.PACK_REQUESTED);
        Assertions.assertEquals(BotStates.PACK_REQUESTED, sigameBotState.getState());
        sigameBotState.next(BotStates.DEFAULT_STATE);
        Assertions.assertEquals(BotStates.DEFAULT_STATE, sigameBotState.getState());
    }
}
