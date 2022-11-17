package sigamebot.bot.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import sigamebot.exceptions.IncorrectSettingsParameterException;

public class AnswerTimerSettingsOptionTest {

    private final AnswerTimerSettingsOption answerTimerSettingsOption = new AnswerTimerSettingsOption();

    private Message getUserMessage(String messageText) {
        var message = new Message();
        message.setText(messageText);
        return message;
    }

    @Test
    public void processUserResponseThrowsExceptionWhenReceivesNotAnInteger() {
        var userInput = getUserMessage("definitely not an integer");
        Assertions.assertThrows(IncorrectSettingsParameterException.class,
                () -> answerTimerSettingsOption.processUserResponse(userInput));
    }

    @Test
    public void processUserResponseThrowsExceptionWhenReceivesNegativeInteger() {
        var userInput = getUserMessage("-1");
        Assertions.assertThrows(IncorrectSettingsParameterException.class,
                () -> answerTimerSettingsOption.processUserResponse(userInput));
    }
}
