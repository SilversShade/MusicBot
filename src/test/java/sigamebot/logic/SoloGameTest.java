package sigamebot.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sigamebot.bot.core.SigameBot;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.user.ChatInfo;

public class SoloGameTest {

    @Test
    public void startNewSoloGameDoesNotThrow() {
        var tgd = new TelegramGameDisplay(new SigameBot(), 12, 12);
        Assertions.assertDoesNotThrow(() -> SoloGame.startNewSoloGame(new ChatInfo(123, tgd),
                2, "does/not/exist", tgd));
    }
}
