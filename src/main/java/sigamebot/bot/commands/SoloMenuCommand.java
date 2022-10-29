package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.CallbackPrefix;
import sigamebot.utilities.FileParser;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.StreamReader;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class SoloMenuCommand extends SigameBotCommand{
    public SoloMenuCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId) {
        String[] menuOpinions = new String[0];
        try {
            menuOpinions = StreamReader.readFromInputStream("src/main/resources/commandmessages/sologamemenu.txt")
                    .split("\n");
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String menuOpinion : menuOpinions) {
            var button = new InlineKeyboardButton();
            var opinion = menuOpinion.split(":");
            button.setText(opinion[0]);
            button.setCallbackData(CallbackPrefix.SOLO_MENU + " " + opinion[1]);
            buttons.add(List.of(button));
        }
        this.bot.sendMessage("Одиночная игра", chatId, buttons);
    }

}
