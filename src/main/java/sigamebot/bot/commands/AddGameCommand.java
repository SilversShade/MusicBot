package sigamebot.bot.commands;

import sigamebot.bot.addgame.AddGame;
import sigamebot.bot.botstate.SigameBotFileRequestStage;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.StreamReader;
import sigamebot.utilities.properties.FilePaths;

import java.io.IOException;
import java.util.ArrayList;

public class AddGameCommand extends SigameBotCommand {
    public AddGameCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) {
        var display = SigameBot.displays.get(chatId);
        if (SigameBot.displays.get(chatId).stageFileRequest.getState() != SigameBotFileRequestStage.DEFAULT_STATE)
            return;
            AddGame game = new AddGame();
    }
}
