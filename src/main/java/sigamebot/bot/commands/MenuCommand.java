package sigamebot.bot.commands;

import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.StreamReader;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.JsonParser;
import sigamebot.utilities.callbackPrefix;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MenuCommand extends SigameBotCommand implements ICallbackQueryHandler {
    public MenuCommand(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }
    @Override
    public void executeCommand(long chatId){
        String[] menuOpinions = new String[0];
        try {
            menuOpinions = StreamReader.readFromInputStream("src/main/resources/commandmessages/menu.txt")
                    .split("\n");
        } catch (IOException e) {
            this.bot.sendMessage("Произошла ошибка при исполнении команды.", chatId);
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (String menuOpinion : menuOpinions) {
            var button = new InlineKeyboardButton();
            var opinion = menuOpinion.split(":");
            button.setText(opinion[0]);
            button.setCallbackData(callbackPrefix.MENU + " " + opinion[1]);
            buttons.add(List.of(button));
        }

        this.bot.sendMessage("Меню игры:", chatId, buttons);
    }

    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {
        var splitedData = callData.split(" ");
        if(SigameBot.commandMap.containsKey(splitedData[1])) {
            bot.deleteMessage(chatId, messageId);
            SigameBot.commandMap.get(splitedData[1]).executeCommand(chatId);
        }
        else
            bot.editMessage("Во время обработки запроса произошла ошибка", chatId, messageId);
    }

}
