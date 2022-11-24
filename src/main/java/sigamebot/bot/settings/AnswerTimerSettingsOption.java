package sigamebot.bot.settings;

import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.botstate.classes.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.exceptions.IncorrectSettingsParameterException;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.properties.CommandNames;

public class AnswerTimerSettingsOption implements ISettingsOption {
    @Override
    public void handleSettingsOption(ChatInfo chatInfo) {
        chatInfo.getGameDisplay().updateMenuMessage("Введите время, отведенное на ответ (в секундах)." +
                " Введите 0 для отключения ограничения времени на ответ.");
        chatInfo.getGameDisplay().currentBotState.next(BotStates.SETTING_UP);
        SigameBotState.currentSettingsOption = SettingsOptions.ANSWER_TIMER;
    }

    @Override
    public void processUserResponse(Message userMessage, ChatInfo chatInfo) {
        if (userMessage.getText().equals(CommandNames.CANCEL_COMMAND_NAME)) {
            chatInfo.getGameDisplay().currentBotState.next(BotStates.DEFAULT_STATE);
            SigameBotState.currentSettingsOption = SettingsOptions.NONE;
            SigameBot.getCommandMap().get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(chatInfo);
            return;
        }
        int timeInSeconds;
        try {
            timeInSeconds = Math.toIntExact(Long.parseLong(userMessage.getText()));
        } catch (NumberFormatException e) {
            throw new IncorrectSettingsParameterException("Необходимо ввести неотрицательное целое число, не превосходящее " + Integer.MAX_VALUE);
        }
        catch (ArithmeticException e) {
            throw new IncorrectSettingsParameterException("Введенное число слишком большое.");
        }

        if (timeInSeconds < 0)
            throw new IncorrectSettingsParameterException("Число должно быть неотрицательным");

        chatInfo.setAnswerTimeInSeconds(timeInSeconds);

        chatInfo.getGameDisplay().currentBotState.next(BotStates.DEFAULT_STATE);
        SigameBotState.currentSettingsOption = SettingsOptions.NONE;
        SigameBot.getCommandMap().get(CommandNames.SOLO_MENU_COMMAND_NAME).executeCommand(chatInfo);
    }
}
