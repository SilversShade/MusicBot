package sigamebot.bot.handlecallback;

public interface ICallbackQueryHandler {
    void handleCallbackQuery(String callData, Integer messageId, Long chatId);
}
