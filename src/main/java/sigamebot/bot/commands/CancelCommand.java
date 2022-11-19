package sigamebot.bot.commands;

import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.CommandNames;

public class CancelCommand extends SigameBotCommand{
    public CancelCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) {
        SigameBot.displays.get(chatId).currentBotState.next(BotStates.DEFAULT_STATE);
        SigameBot.commandMap.get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(chatId);
    }
}
