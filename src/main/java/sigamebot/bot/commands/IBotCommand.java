package sigamebot.bot.commands;

import java.io.IOException;

public interface IBotCommand {
    void executeCommand(long chatId) throws IOException;
}
