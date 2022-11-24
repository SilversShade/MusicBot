package sigamebot.bot.commands;

import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.core.SigameBot;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.properties.CommandNames;

public class CancelCommand extends SigameBotCommand{
    public CancelCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(ChatInfo chatInfo) {
        chatInfo.getGameDisplay().currentBotState.next(BotStates.DEFAULT_STATE);
        SigameBot.getCommandMap().get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(chatInfo);
    }
}
