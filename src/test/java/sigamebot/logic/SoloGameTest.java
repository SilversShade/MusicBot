package sigamebot.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sigamebot.bot.core.SigameBot;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;

public class SoloGameTest {

    @Test
    public void startNewSoloGameDoesNotThrow() {
        Assertions.assertDoesNotThrow(() -> SoloGame.startNewSoloGame(123,
                2, "does/not/exist", new TelegramGameDisplay(new SigameBot(), 12, 12)));
    }


}
