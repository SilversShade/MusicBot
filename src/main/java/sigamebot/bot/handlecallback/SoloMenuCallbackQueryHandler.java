package sigamebot.bot.handlecallback;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.logic.SoloGame;
import sigamebot.utilities.CallbackPrefix;
import sigamebot.utilities.FileParser;

import java.util.ArrayList;
import java.util.List;

public class SoloMenuCallbackQueryHandler implements ICallbackQueryHandler {
    private final ITelegramBot bot;
    public SoloMenuCallbackQueryHandler(ITelegramBot bot){ this.bot = bot; }

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        bot.deleteMessage(chatId, messageId);
        var splittedData = callData.split(" ");
        switch (splittedData[1]) {
            case "add_game" -> {
                bot.sendMessage("Отправьте Ваш пак. Если Вы передумали, введите команду /cancel", chatId);
                SigameBot.chatToBotState.put(chatId, SigameBot.chatToBotState.get(chatId).nextState());
                return;
            }
            case "base" -> sendPacks(splittedData, chatId, "packs");
            case "user_pack" -> sendPacks(splittedData, chatId, "userpacks");
            default -> {
                String path = "src/main/resources/" + splittedData[1];
                SoloGame.startNewSoloGame(bot, chatId, Integer.parseInt(splittedData[2]), path);
            }
        }
    }
    private void sendErrorMessage(Long chatId){
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText("Вернуться в меню");
        button.setCallbackData(CallbackPrefix.MENU + " /menu");
        buttons.add(List.of(button));
        bot.sendMessage("Ошибка, игры не найдены", chatId, buttons);
    }
    private void sendPacks(String[] splittedData, Long chatId, String type){
        var packs = FileParser.getAllFilesFromDir("src/main/resources/" + type);
        var page = Integer.parseInt(splittedData[2]);
        var maxPage = packs.size() / 5 + (packs.size() % 5 > 0 ? 1 : 0);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if(packs.size() == 0){
            sendErrorMessage(chatId);
            return;
        }
        for(var i = 5 * page + 1; i < Math.min(5 * (page + 1), packs.size()); i++){
            var button = new InlineKeyboardButton();
            button.setText(i + ") " + packs.get(i).getName());
            button.setCallbackData(CallbackPrefix.SOLO_MENU + " " + type + " " + (i + 1));
            buttons.add(List.of(button));
        }
        if(packs.size() > 5){
            List<InlineKeyboardButton> raw = new ArrayList<>();
            int previousPage = page - 1 < 0 ? maxPage - 1 : page - 1;
            int nextPage = page + 1 > maxPage - 1 ? 0 : page + 1;
            var buttonLeft = new InlineKeyboardButton();
            buttonLeft.setText("<");
            buttonLeft.setCallbackData(CallbackPrefix.SOLO_MENU + " base " + previousPage);
            raw.add(buttonLeft);
            var buttonCenter = new InlineKeyboardButton();
            buttonCenter.setText((page + 1) + "/" + maxPage);
            buttonCenter.setCallbackData(CallbackPrefix.SOLO_MENU + " base " + page);
            raw.add(buttonCenter);
            var buttonRight = new InlineKeyboardButton();
            buttonRight.setText(">");
            buttonRight.setCallbackData(CallbackPrefix.SOLO_MENU + " base " + nextPage);
            raw.add(buttonRight);
            buttons.add(raw);
        }
        var button = new InlineKeyboardButton();
        button.setText("Назад");
        button.setCallbackData(CallbackPrefix.MENU + " /solo");
        buttons.add(List.of(button));
        bot.sendMessage("Выберите текст", chatId, buttons);
    }
}
