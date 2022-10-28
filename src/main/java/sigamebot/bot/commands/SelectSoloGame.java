package sigamebot.bot.commands;

import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.logic.SoloGame;
import sigamebot.logic.scenariologic.Category;
import sigamebot.utilities.JsonParser;
import sigamebot.utilities.callbackPrefix;
import sigamebot.utilities.FileParser;
import org.apache.commons.io.FilenameUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class SelectSoloGame extends SigameBotCommand implements ICallbackQueryHandler {
    public SelectSoloGame(String command, String description, SigameBot bot) {
        super(command, description, bot);
    }

    @Override
    public void executeCommand(long chatId){
        var tests = FileParser.getAllFilesFromDir("src/main/resources/tests/solo");
        if (tests.isEmpty()) {
            this.bot.sendMessage("Не найдено ни одного теста.", chatId);
            return;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (var i = 0; i < tests.size(); i++) {
            var button = new InlineKeyboardButton();
            button.setText(String.format("%d. %s", i + 1, FilenameUtils.removeExtension(tests.get(i).getName())));
            button.setCallbackData(callbackPrefix.SOLO_MENU + " " + i);
            buttons.add(List.of(button));
        }

        this.bot.sendMessage("Выберите тест из списка:", chatId, buttons);
    }

    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {
        var splitedData = callData.split(" ", 2);
        if(Objects.equals(splitedData[1], "start")){
            SoloGame.getOngoingSoloGames().get(chatId).nextQuestion("");
            return;
        }
        bot.deleteMessage(chatId, messageId);
        Category scenario = JsonParser.getSoloGameFromJson(Integer.parseInt(callData.split(" ")[1]));
        SoloGame soloGame = new SoloGame(chatId, scenario);
        soloGame.start();
    }

}
