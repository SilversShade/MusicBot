package sigamebot.bot.commands;

import sigamebot.bot.core.SigameBot;

import javax.inject.Singleton;

@Singleton
public class OnlineMenuCommand extends SigameBotCommand{
    public OnlineMenuCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId) {

    }

}
