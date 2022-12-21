package sigamebot.bot.handlecallback;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.botstate.BuilderStates;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.JsonParser;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;
import sigamebot.utilities.properties.FilePaths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SoloTestBuilderCallbackQueryHandler implements ICallbackQueryHandler {

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, ChatInfo chatInfo) {
        var display = chatInfo.getGameDisplay();
        var soloGameBuilder = display.soloTestBuilder;
        var text = soloGameBuilder.nextStep(callData.split(" ", 2)[1]);
        var buttonsText = soloGameBuilder.getButtons();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        if (soloGameBuilder.state == BuilderStates.FINISH) {
            var game = soloGameBuilder.getSoloGame();
            try {
                createUserpacksDirectoryIfNotPresent();
                var jsonGame = JsonParser.getJsonFromGame(game);
                var file = new File(FilePaths.USERPACKS_DIRECTORY + game.name + ".json");
                if (!file.exists()) file.createNewFile();
                var fileWriter = new FileWriter(file);
                fileWriter.write(jsonGame);
                fileWriter.close();
                row.add(TelegramBotMessageApi.createInlineKeyboardButton("Вернуться в меню", CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME));
                display.currentBotState.next(BotStates.DEFAULT_STATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            for (var buttonText : buttonsText) {
                row.add(TelegramBotMessageApi.createInlineKeyboardButton(buttonText,
                        CallbackPrefix.SOLO_BUILDER + " " + buttonText));
            }
        }
        buttons.add(row);
        display.updateMenuMessage(text, buttons);
    }

    private void createUserpacksDirectoryIfNotPresent() throws IOException {
        Path pathToUserpacksDirectory = Path.of(FilePaths.USERPACKS_DIRECTORY);
        if (Files.notExists(pathToUserpacksDirectory))
            Files.createDirectory(pathToUserpacksDirectory);
    }
}