package sigamebot.bot.userinteraction;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sigamebot.bot.commands.IBotCommand;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.userinteraction.filehandlers.IFileHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class UpdateProcessor {

    public static void processCallbackQuery(ITelegramBot bot,
                                             Update update,
                                             Map<String, Class<? extends ICallbackQueryHandler>> queryHandlerMap) {
        var callData = update.getCallbackQuery().getData();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        var callbackPrefix = callData.split(" ")[0];
        if (queryHandlerMap.containsKey(callbackPrefix)) {
            try {
                queryHandlerMap
                        .get(callbackPrefix)
                        .getMethod("handleCallbackQuery",
                                ITelegramBot.class, String.class, Integer.class, Long.class)
                        .invoke(null, bot, callData, messageId, chatId);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                bot.sendMessage("Произошла ошибка во время обработки запроса", chatId);
                e.printStackTrace();
            }
        }

    }

    public static void handleUserFile(IFileHandler handler) {
        handler.handleFile();
    }

    public static void processCommands(Message message, Map<String, ? extends IBotCommand> commandMap) {
        if(message != null && message.getText() != null &&  commandMap.containsKey(message.getText())) {
            commandMap.get(message.getText()).executeCommand(message.getChatId());
        }
    }
}
