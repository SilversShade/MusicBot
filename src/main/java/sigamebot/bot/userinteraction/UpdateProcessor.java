package sigamebot.bot.userinteraction;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sigamebot.bot.commands.IBotCommand;
import sigamebot.bot.core.ITelegramBot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class UpdateProcessor {
    public static void processUpdate(ITelegramBot bot,
                                     Update update,
                                     Map<String, ? extends IBotCommand> commandMap,
                                     Map<String, Class<? extends ICallbackQueryHandler>> queryHandlerMap) {
        Message message = null;

        if (update.hasMessage())
            message = update.getMessage();

        processCommands(bot, message, commandMap);

        if (update.hasCallbackQuery())
            processCallbackQuery(bot, update, queryHandlerMap);
    }

    private static void processCallbackQuery(ITelegramBot bot,
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

    private static void processCommands(ITelegramBot bot, Message message, Map<String, ? extends IBotCommand> commandMap) {
        if(message != null && commandMap.containsKey(message.getText())) {
            try {
                commandMap.get(message.getText()).executeCommand(message.getChatId());
            } catch (IOException e) {
                bot.sendMessage("При обработке команды произошла ошибка", message.getChatId());
                e.printStackTrace();
            }
        }
    }
}
