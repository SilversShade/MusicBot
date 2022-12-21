package sigamebot.ui.gamedisplaying;

import sigamebot.bot.botstate.classes.SigameBotState;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.bot.testbuilder.SoloTestBuilder;
import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.Question;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import java.util.ArrayList;
import java.util.List;

public class TelegramGameDisplay implements IGameDisplay {
    private final TelegramBotMessageApi bot;
    private final long chatId;
    private int messageId;
    public SigameBotState currentBotState = new SigameBotState();
    public SoloTestBuilder soloTestBuilder = new SoloTestBuilder();

    public TelegramGameDisplay(TelegramBotMessageApi bot, long chatId, int messageId) {
        this.bot = bot;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    @Override
    public void displayStartMessage() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(TelegramBotMessageApi.createInlineKeyboardButton("Начать", CallbackPrefix.SOLO_GAME + " start")));
        this.bot.editMessage("Нажмите кнопку \"Начать\" для старта игры.", chatId, messageId, buttons);
    }

    private String getAnswerTimeMessage(int answerTimeInSeconds) {
        return answerTimeInSeconds == 0 ? "Время на ответ не ограничено." : "Время на ответ (в секундах): " + answerTimeInSeconds;
    }

    @Override
    public void updateGameStateView(Question currentQuestion, Player player, int answerTimeInSeconds) {
        List<List<InlineKeyboardButton>> answerOptionsButtons = new ArrayList<>();
        for (var i = 0; i < currentQuestion.answerOptions.size(); i++) {
            answerOptionsButtons.add(List.of(TelegramBotMessageApi.createInlineKeyboardButton(String.format("%d. %s", i + 1,
                            currentQuestion.answerOptions.get(i)),
                    CallbackPrefix.SOLO_GAME + " " + currentQuestion.answerOptions.get(i))));
        }
        this.bot.editMessage(currentQuestion.questionTitle
                        + "\n\n"
                        + currentQuestion.questionDescription
                        + "\n\n"
                        + getAnswerTimeMessage(answerTimeInSeconds)
                        + "\n\n"
                        + "Текущее количество очков игрока: "
                        + player.score,
                chatId,
                messageId,
                answerOptionsButtons);
    }

    @Override
    public void updateMenuMessage(String text, List<List<InlineKeyboardButton>> buttons) {
        if (!this.bot.editMessage(text, chatId, messageId, buttons))
            messageId = this.bot.sendMessage(text, chatId, buttons);
    }

    @Override
    public void updateMenuMessage(String text) {
        if (!this.bot.editMessage(text, chatId, messageId))
            messageId = this.bot.sendMessage(text, chatId);
    }

    @Override
    public void displayEndMessage(Player player) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        var button = TelegramBotMessageApi.createInlineKeyboardButton("Вернуться в меню",
                CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME);
        buttons.add(List.of(button));
        this.bot.editMessage("Игра окончена. Финальный счёт игрока: " + player.score,
                chatId,
                messageId,
                buttons);
    }
}
