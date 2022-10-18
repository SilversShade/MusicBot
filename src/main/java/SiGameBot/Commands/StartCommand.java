package SiGameBot.Commands;

import SiGameBot.SigameBot;
import SiGameBot.Utilities.FileReader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import javax.inject.Singleton;
import javax.swing.text.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Singleton
public class StartCommand extends SigameBotCommand{
    public StartCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId) throws IOException {
        this.bot.sendMessage(FileReader.readFromInputStream(
                new FileInputStream("src/main/resources/CommandMessages/startcommandmessage.txt")), chatId);
    }
}
