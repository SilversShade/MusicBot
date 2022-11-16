package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.commands.MenuCommand;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.properties.CallbackPrefix;

import java.util.List;
import java.util.Map;

public class Settings {

    public static final Map<String, ISettingsOption> optionNameToOptionHandler =
            Map.of(SettingsOptionsCallbackSuffix.ANSWER_TIME_CALLBACK_SUFFIX, new AnswerTimerSettingsOption());

    private static final List<List<InlineKeyboardButton>> settingsOptions
            = List.of(List.of(ITelegramBot.createInlineKeyboardButton("Время на ответ",
                    CallbackPrefix.SETTINGS + " " + SettingsOptionsCallbackSuffix.ANSWER_TIME_CALLBACK_SUFFIX)),
            MenuCommand.BACK_BUTTON);

    public static void displaySettingsOptions(TelegramGameDisplay display) {
        display.updateMenuMessage("Настройки", settingsOptions);
    }
}
