package sigamebot.bot.handlecallback;

import sigamebot.user.ChatInfo;

public interface ICallbackQueryHandler {
    void handleCallbackQuery(String callData, Integer messageId, ChatInfo chatInfo);
}
