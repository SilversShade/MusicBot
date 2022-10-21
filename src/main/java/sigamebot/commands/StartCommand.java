package sigamebot.commands;

import sigamebot.SigameBot;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;

@Singleton
public class StartCommand extends SigameBotCommand{
    public StartCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) throws IOException {
        this.bot.sendMessage(StreamReader.readFromInputStream(
                new FileInputStream("src/main/resources/commandmessages/startcommandmessage.txt")), chatId);
    }
}
