package SiGameBot;

import SiGameBot.Commands.BeginCommand;
import SiGameBot.Commands.SigameBotCommand;
import SiGameBot.Commands.StartCommand;
import SiGameBot.GameDisplaying.TelegramGameDisplay;
import SiGameBot.Logic.SoloGame;
import SiGameBot.Utilities.JsonParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SigameBot extends TelegramLongPollingBot {

    private static final String TOKEN = System.getenv("botToken");
    private static final String NAME = "SIGame Bot";
    private Map<Long, SoloGame> ongoingSoloGames;

    private static Map<String, SigameBotCommand> commandMap;
    public SigameBot() {
        commandMap = Map.of("/start",
                new StartCommand("/start", "Краткое описание бота и список доступных команд", this),
                "/begin",
                new BeginCommand("/begin", "Начало игры", this));
        ongoingSoloGames = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = null;

        if (update.hasMessage())
            message = update.getMessage();

        if(message != null && commandMap.containsKey(message.getText())) {
            try {
                commandMap.get(message.getText()).executeCommand(message.getChatId());
            } catch (IOException e) {
                this.sendMessage("При обработке команды произошла ошибка", message.getChatId());
                e.printStackTrace();
            }
        }
        else if (update.hasCallbackQuery()) {
            var callData = update.getCallbackQuery().getData();
            var messageId = update.getCallbackQuery().getMessage().getMessageId();
            var chatId = update.getCallbackQuery().getMessage().getChatId();

            var parsedData = callData.split(" ");
            if (parsedData[0].equals("testselect")) {
                this.deleteMessage(chatId, messageId);
                this.ongoingSoloGames.put(chatId, new SoloGame(chatId,
                        JsonParser.getGameFromJson(Integer.parseInt(parsedData[1])),
                        new TelegramGameDisplay(this, chatId)));
                this.ongoingSoloGames.get(chatId).start();
            }
            if (parsedData[0].equals("solo"))
            {
                parsedData = callData.split(" ", 2);
                this.ongoingSoloGames.get(chatId).nextQuestion(parsedData[1]);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return NAME;
    }

    @Override
    public String getBotToken() { return TOKEN; }

    // Отправка сообщений
    public int sendMessage(String text, long chatId){
        SendMessage message = createSendMessageObject(text, chatId);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons){
        SendMessage message = createSendMessageObject(text, chatId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }
    }
    public int sendMessage(String text, long chatId, InlineKeyboardMarkup keyboard){
        SendMessage message = createSendMessageObject(text, chatId);
        message.setReplyMarkup(keyboard);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    // Редактирование сообщения
    public int editMessage(String text, long chatId, int messageId){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int editMessage(String text, long chatId, int messageId, InlineKeyboardMarkup keyboard){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    // Удаление сообщения
    public int deleteMessage(long chatId, int messageId){
        DeleteMessage message = new DeleteMessage();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    // Дополнительные методы
    private SendMessage createSendMessageObject(String text, long chatId){
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        message.enableHtml(true);
        return message;
    }
    private EditMessageText createEditMessageObject(String text, long chatId, int messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId);
        return message;
    }
    public void deleteOngoingSoloGame(long chatId){
        ongoingSoloGames.remove(chatId);
    }
}

