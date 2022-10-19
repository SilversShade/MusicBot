package SiGameBot.Commands;

import SiGameBot.SigameBot;
import SiGameBot.Utilities.StreamReader;

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
                new FileInputStream("src/main/resources/CommandMessages/startcommandmessage.txt")), chatId);
    }
}
