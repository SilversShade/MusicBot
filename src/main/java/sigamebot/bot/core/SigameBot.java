package sigamebot.bot.core;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.botstate.classes.SigameBotState;
import sigamebot.bot.commands.*;
import sigamebot.bot.handlecallback.*;
import sigamebot.bot.settings.Settings;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.bot.userinteraction.filehandlers.UserFileHandler;
import sigamebot.exceptions.IncorrectSettingsParameterException;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SigameBot extends TelegramLongPollingBot implements ITelegramBot {
    private static final String TOKEN = System.getenv("botToken");
    private static final String NAME = "SIGame Bot";
    public static Map<String, SigameBotCommand> commandMap;
    private static Map<String, ICallbackQueryHandler> queryHandlerMap;

    //todo:
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
    }

    @Deprecated
    @Override
    public void onUpdateReceived(Update update) {
        // TODO: decompose
        if (update.hasCallbackQuery())
            UpdateProcessor.processCallbackQuery(update, queryHandlerMap);

        if (!update.hasMessage())
            return;
        Message message = update.getMessage();

        if (isBotWaitingAnswer(message)) {
            try {
                Settings.userResponseDelegator.get(SigameBotState.currentSettingsOption).processUserResponse(message);
            } catch (IncorrectSettingsParameterException e) {
                sendMessage(e.getMessage(), message.getChatId());
            }
            deleteMessage(message.getChatId(), message.getMessageId());
            return;
        }

        if (!displays.containsKey(message.getChatId())) {
            var messageId = sendMessage("Start message", message.getChatId());
            displays.put(message.getChatId(),
                    new TelegramGameDisplay(this, message.getChatId(), messageId));
        }
        if (message.getText() != null && commandMap.containsKey(message.getText())) {
            UpdateProcessor.processCommands(message, commandMap);
            deleteMessage(message.getChatId(), message.getMessageId());
        }

        if (message.hasDocument())
            UserFileHandler.handleUserFiles(this, message);
    }

    private boolean isBotWaitingAnswer(Message message)
    {
        return displays.containsKey(message.getChatId())
                && displays.get(message.getChatId()).currentBotState.getState() == BotStates.SETTING_UP
                && Settings.userResponseDelegator.containsKey(SigameBotState.currentSettingsOption);
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