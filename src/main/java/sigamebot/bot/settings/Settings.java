package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import java.util.List;

public class Settings {

    private static final List<List<InlineKeyboardButton>> settingsOptions
            = List.of(List.of(ITelegramBot.createInlineKeyboardButton("Время на ответ", "response_time")),
                    List.of(ITelegramBot.createInlineKeyboardButton("Назад",
                            CallbackPrefix.MENU + " " + CommandNames.SOLO_MENU_COMMAND_NAME)));

    public static void sendSettingsOptions(TelegramGameDisplay display) {
        display.updateMenuMessage("Настройки", settingsOptions);
    }
}
