package sigamebot.bot.commands;

import sigamebot.bot.core.SigameBot;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import sigamebot.user.ChatInfo;

public abstract class SigameBotCommand extends BotCommand implements IBotCommand {

    protected final SigameBot bot;

    public SigameBotCommand(String command, String description, SigameBot bot) {
        super(command, description);
        this.bot = bot;
    }

    public abstract void executeCommand(ChatInfo chatInfo);
}
