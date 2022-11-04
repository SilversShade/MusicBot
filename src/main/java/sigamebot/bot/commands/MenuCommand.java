package sigamebot.bot.commands;

import sigamebot.bot.botstate.SigameBotState;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.FilePaths;
import sigamebot.utilities.StreamReader;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.properties.CallbackPrefix;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MenuCommand extends SigameBotCommand{
    public MenuCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId){
        if (SigameBot.chatToBotState.get(chatId) != SigameBotState.DEFAULT_STATE)
            return;

        String[] menuOpinions = new String[0];
        try {
            menuOpinions = StreamReader.readFromInputStream(FilePaths.MENU_COMMAND_MESSAGE)
                    .split("\r");
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String menuOpinion : menuOpinions) {
            var opinion = menuOpinion.split(":");
            var button = ITelegramBot.createInlineKeyboardButton(opinion[0],
                    CallbackPrefix.MENU + " " + opinion[1]);
            buttons.add(List.of(button));
        }

        this.bot.sendMessage("Меню игры:", chatId, buttons);
    }

}
