package sigamebot.bot.core;

import sigamebot.bot.commands.*;
import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.ui.gamedisplaying.IGameDisplay;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.callbackPrefix;
import sigamebot.utilities.commandString;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class SigameBot extends TelegramLongPollingBot implements ITelegramBot{
    private static final String TOKEN = System.getenv("botToken");
    private static final String NAME = "SIGame Bot";
    public static Map<String, SigameBotCommand> commandMap;
    private static Map<String, Class<? extends ICallbackQueryHandler>> queryHandlerMap;
    public static IGameDisplay gameDisplay;

    public SigameBot() {
        commandMap = Map.of(commandString.START,
                new StartCommand(commandString.START, "Краткое описание бота и список доступных команд", this),
                commandString.MENU,
                new MenuCommand(commandString.MENU, "Меню игры", this),
                commandString.SOLO_GAME,
                new SelectSoloGame(commandString.SOLO_GAME, "Одиночная игра", this),
                commandString.ONLINE_GAME,
                new SelectOnlineGame(commandString.ONLINE_GAME, "Онлайн игра", this));

        queryHandlerMap = Map.of(callbackPrefix.MENU,
                MenuCommand.class,
                callbackPrefix.SOLO_MENU,
                SelectSoloGame.class,
                callbackPrefix.SOLO_GAME,
                TelegramGameDisplay.class);
        gameDisplay = new TelegramGameDisplay(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateProcessor.processUpdate(this, update, commandMap, queryHandlerMap);
    }

    @Override
    public String getBotUsername() {
        return NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    // Отправка сообщений
    public int sendMessage(SendMessage message){
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }
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

    // Редактирование сообщения
    public int editMessage(EditMessageText message){
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
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
}