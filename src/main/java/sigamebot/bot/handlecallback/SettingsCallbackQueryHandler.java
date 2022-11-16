package sigamebot.bot.handlecallback;

import sigamebot.bot.core.ITelegramBot;

public class SettingsCallbackQueryHandler implements ICallbackQueryHandler {

    private final ITelegramBot bot;
    public SettingsCallbackQueryHandler(ITelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId) {

    }
}
