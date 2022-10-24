package sigamebot.bot.userinteraction;

import sigamebot.bot.core.ITelegramBot;

public interface ICallbackQueryHandler {
    static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId){}
}
