package sigamebot.bot.commands;

import sigamebot.user.ChatInfo;

public interface IBotCommand {
    void executeCommand(ChatInfo chatInfo);
}
