package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotFileRequestStage;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.CommandNames;

public class CancelCommand extends SigameBotCommand{
    public CancelCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) {
        if (SigameBot.displays.get(chatId).stageFileRequest.getState() != SigameBotFileRequestStage.PACK_REQUESTED)
            return;
        SigameBot.displays.get(chatId).stageFileRequest.next();
        SigameBot.commandMap.get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(chatId);
    }
}
