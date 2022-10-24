package sigamebot.bot;

public interface ICallbackQueryHandler {
    static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId){}
}
