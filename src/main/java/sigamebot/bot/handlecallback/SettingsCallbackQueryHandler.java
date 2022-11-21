package sigamebot.bot.handlecallback;

import sigamebot.bot.settings.Settings;

public class SettingsCallbackQueryHandler implements ICallbackQueryHandler {

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId) {
        var parsedData = callData.split(" ");
        if (!Settings.optionNameToOptionHandler.containsKey(parsedData[1]))
            return;
        Settings.optionNameToOptionHandler.get(parsedData[1]).handleSettingsOption(chatId);
    }
}
