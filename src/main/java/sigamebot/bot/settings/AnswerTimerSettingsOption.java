package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.botstate.classes.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.exceptions.IncorrectSettingsParameterException;
import sigamebot.utilities.properties.CommandNames;

public class AnswerTimerSettingsOption implements ISettingsOption {
    @Override
    public void handleSettingsOption(long chatId) {
        SigameBot.displays.get(chatId).updateMenuMessage("Введите время, отведенное на ответ (в секундах).");
        SigameBot.displays.get(chatId).currentBotState.next(BotStates.SETTING_UP);
        SigameBotState.currentSettingsOption = SettingsOptions.ANSWER_TIMER;
    }

    @Override
    public void processUserResponse(Message userMessage) {
        if (userMessage.getText().equals(CommandNames.CANCEL_COMMAND_NAME)) {
            SigameBot.displays.get(userMessage.getChatId()).currentBotState.next(BotStates.DEFAULT_STATE);
            SigameBotState.currentSettingsOption = SettingsOptions.NONE;
            SigameBot.commandMap.get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(userMessage.getChatId());
            return;
        }
        int timeInSeconds;
        try {
            timeInSeconds = Integer.parseInt(userMessage.getText());
        } catch (NumberFormatException e) {
            throw new IncorrectSettingsParameterException("Необходимо ввести неотрицательное целое число.");
        }
        if (timeInSeconds < 0)
            throw new IncorrectSettingsParameterException("Число должно быть неотрицательным");

        AnswerTimer.chatIdToAnswerTimeInSeconds.put(userMessage.getChatId(), timeInSeconds);

        SigameBot.displays.get(userMessage.getChatId()).currentBotState.next(BotStates.DEFAULT_STATE);
        SigameBotState.currentSettingsOption = SettingsOptions.NONE;
        SigameBot.commandMap.get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(userMessage.getChatId());
    }
}
