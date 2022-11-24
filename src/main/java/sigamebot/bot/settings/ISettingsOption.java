package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.user.ChatInfo;

public interface ISettingsOption {
    void handleSettingsOption(ChatInfo chatInfo);

    void processUserResponse(Message userMessage, ChatInfo chatInfo);
}
