package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class StartCommand extends SigameBotCommand{
    public StartCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) {
        if (SigameBot.chatToBotState.get(chatId) != SigameBotState.DEFAULT_STATE)
            return;
        try {
            this.bot.sendMessage(StreamReader.readFromInputStream(
                            "src/main/resources/commandmessages/startcommandmessage.txt"),
                    chatId);
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }
    }
}
