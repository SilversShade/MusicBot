package sigamebot.bot.handlecallback;

import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.settings.Settings;

public class SettingsCallbackQueryHandler implements ICallbackQueryHandler {

    private final ITelegramBot bot;
    public SettingsCallbackQueryHandler(ITelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId) {
        var parsedData = callData.split(" ");
        if (!Settings.optionNameToOptionHandler.containsKey(parsedData[1]))
            return;
        Settings.optionNameToOptionHandler.get(parsedData[1]).handleSettingsOption(chatId);
    }
}
