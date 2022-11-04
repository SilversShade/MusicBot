package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.CommandNames;

public class CancelCommand extends SigameBotCommand{
    public CancelCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) {
        if (SigameBot.chatToBotState.get(chatId) != SigameBotState.PACK_REQUESTED)
            return;
        SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
        SigameBot.commandMap.get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(chatId);
    }
}
