package sigamebot.bot.core;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.ITelegramBotState;
import sigamebot.bot.botstate.SigameBotFileRequestStage;
import sigamebot.bot.commands.CancelCommand;
import sigamebot.bot.handlecallback.ICallbackQueryHandler;
import sigamebot.bot.userinteraction.UpdateProcessor;
import sigamebot.bot.commands.*;
import sigamebot.bot.handlecallback.*;
import sigamebot.bot.userinteraction.filehandlers.UserFileHandler;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.properties.CallbackPrefix;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import sigamebot.utilities.properties.CommandNames;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SigameBot extends TelegramLongPollingBot implements ITelegramBot{
    private static final String TOKEN = System.getenv("myBot");
    private static final String NAME = "SIGame Bot";
    public static Map<String, SigameBotCommand> commandMap;
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
                new CancelCommand(CommandNames.CANCEL_COMMAND_NAME, "Выход из режима ожидания отправки пака", this),
                CommandNames.ONLINE_MENU_COMMAND_NAME,
                new OnlineMenuCommand(CommandNames.ONLINE_MENU_COMMAND_NAME, "Онлайн игра", this));

        queryHandlerMap = Map.of(CallbackPrefix.MENU,
                new MenuCallbackQueryHandler(this),
                CallbackPrefix.SOLO_GAME,
                new SoloGameCallbackQueryHandler(),
                CallbackPrefix.SOLO_MENU,
                new SoloMenuCallbackQueryHandler(this));

        displays = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = null;
        if (update.hasMessage()) {
            message = update.getMessage();
        }
        if(message != null){
            if(!displays.containsKey(message.getChatId())){
                var messageId = sendMessage("Start message", message.getChatId());
                displays.put(message.getChatId(),
                        new TelegramGameDisplay(this, message.getChatId(), messageId));
            }
            if(message.getText() != null && commandMap.containsKey(message.getText())){
                UpdateProcessor.processCommands(message, commandMap);
                deleteMessage(message.getChatId(), message.getMessageId());
            }

            if (message.hasDocument())
                UserFileHandler.handleUserFiles(this, message);

        }


        if (update.hasCallbackQuery())
            UpdateProcessor.processCallbackQuery(update, queryHandlerMap);
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

    public boolean editMessage(String text, long chatId, int messageId){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            return false;
        }

    }
    public boolean editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons){
        EditMessageText message = createEditMessageObject(text, chatId, messageId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
        message.enableHtml(true);
        return message;
    }
}