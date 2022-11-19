package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface ISettingsOption {
    void handleSettingsOption(long chatId);

    void processUserResponse(Message userMessage);
}
