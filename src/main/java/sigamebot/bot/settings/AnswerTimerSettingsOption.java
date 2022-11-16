package sigamebot.bot.settings;

import sigamebot.bot.commands.MenuCommand;
import sigamebot.bot.core.SigameBot;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;

import java.util.List;

public class AnswerTimerSettingsOption implements ISettingsOption{
    @Override
    public void handle(long chatId) {
        SigameBot.displays.get(chatId).updateMenuMessage("Введите время, отведенное на ответ (в секундах).", List.of(MenuCommand.BACK_BUTTON));
    }
}
