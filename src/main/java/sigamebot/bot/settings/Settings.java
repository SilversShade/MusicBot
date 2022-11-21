package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.commands.MenuCommand;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.utilities.properties.CallbackPrefix;

import java.util.List;
import java.util.Map;

public class Settings {

    private static final AnswerTimerSettingsOption answerTimerSettingsOption = new AnswerTimerSettingsOption();

    public static final Map<SettingsOptions, ISettingsOption> userResponseDelegator = Map.of(
            SettingsOptions.ANSWER_TIMER, answerTimerSettingsOption);

    public static final Map<String, ISettingsOption> optionNameToOptionHandler =
            Map.of(SettingsOptionsCallbackSuffixes.ANSWER_TIME_CALLBACK_SUFFIX, answerTimerSettingsOption);

    private static final List<List<InlineKeyboardButton>> settingsOptions
            = List.of(List.of(TelegramBotMessageApi.createInlineKeyboardButton("Время на ответ",
                    CallbackPrefix.SETTINGS + " " + SettingsOptionsCallbackSuffixes.ANSWER_TIME_CALLBACK_SUFFIX)),
            MenuCommand.BACK_BUTTON);

    public static void displaySettingsOptions(TelegramGameDisplay display) {
        display.updateMenuMessage("Настройки", settingsOptions);
    }
}
