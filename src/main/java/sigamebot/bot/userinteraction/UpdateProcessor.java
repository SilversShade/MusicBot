package sigamebot.bot.userinteraction;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sigamebot.bot.commands.SigameBotCommand;
import sigamebot.bot.handlecallback.ICallbackQueryHandler;

import java.util.Map;

public class UpdateProcessor {

    public static void processCallbackQuery(Update update,
                                             Map<String, ICallbackQueryHandler> queryHandlerMap) {
        var callData = update.getCallbackQuery().getData();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        var callbackPrefix = callData.split(" ")[0];
        if (queryHandlerMap.containsKey(callbackPrefix))
            queryHandlerMap.get(callbackPrefix).handleCallbackQuery(callData, messageId, chatId);

    }

    public static void processCommands(Message message, Map<String, SigameBotCommand> commandMap){
            commandMap.get(message.getText()).executeCommand(message.getChatId());
    }
}
