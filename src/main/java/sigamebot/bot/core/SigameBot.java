package sigamebot.bot.core;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sigamebot.bot.commands.*;
import sigamebot.bot.handlecallback.*;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SigameBot extends TelegramBotMessageApi {
    private static final String TOKEN = System.getenv("botToken");
    private static final String NAME = "SIGame Bot";

    private final UpdateProcessor updateProcessor;
    private static Map<String, SigameBotCommand> commandMap;
    private static Map<String, ICallbackQueryHandler> queryHandlerMap;

    public static Map<Long, TelegramGameDisplay> displays;

    public SigameBot() {
        commandMap = Map.of(CommandNames.START_COMMAND_NAME,
                new StartCommand(CommandNames.START_COMMAND_NAME, "Краткое описание бота и список доступных команд", this),
                CommandNames.SOLO_MENU_COMMAND_NAME,
                new SoloMenuCommand(CommandNames.SOLO_MENU_COMMAND_NAME, "Одиночная игра", this),
                CommandNames.MENU_COMMAND_NAME,
                new MenuCommand(CommandNames.MENU_COMMAND_NAME, "Меню игры", this),
                CommandNames.CANCEL_COMMAND_NAME,
                new CancelCommand(CommandNames.CANCEL_COMMAND_NAME, "Отмена текущего действия, возврат в главное меню", this));

        queryHandlerMap = Map.of(CallbackPrefix.MENU,
                new MenuCallbackQueryHandler(this),
                CallbackPrefix.SOLO_GAME,
                new SoloGameCallbackQueryHandler(),
                CallbackPrefix.SOLO_MENU,
                new SoloMenuCallbackQueryHandler(this),
                CallbackPrefix.SETTINGS,
                new SettingsCallbackQueryHandler());

        displays = new HashMap<>();
        updateProcessor = new UpdateProcessor(this);
    }

    public static Map<String, SigameBotCommand> getCommandMap() {
        return commandMap;
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.updateProcessor.handleReceivedUpdates(update, queryHandlerMap);
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
    public int sendMessage(String text, long chatId) {
        SendMessage message = createSendMessageObject(text, chatId);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    public int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons) {
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

    public boolean editMessage(String text, long chatId, int messageId) {
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }

    }

    public boolean editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons) {
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }

    }


    public int deleteMessage(long chatId, int messageId) {
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

    private SendMessage createSendMessageObject(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        message.enableHtml(true);
        return message;
    }

    private EditMessageText createEditMessageObject(String text, long chatId, int messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId);
        message.enableHtml(true);
        return message;
    }
}