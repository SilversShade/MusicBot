package sigamebot.bot.settings;

import sigamebot.ui.gamedisplaying.TelegramGameDisplay;

public interface ISettingsOption {
    void handle(long chatId);
}
