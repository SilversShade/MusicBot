package sigamebot.ui.gamedisplaying;

import sigamebot.bot.handlecallback.ICallbackQueryHandler;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.logic.Player;
import sigamebot.logic.SoloGame;
import sigamebot.logic.scenariologic.Question;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.CallbackPrefix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramGameDisplay implements IGameDisplay{
    private final ITelegramBot bot;
    private final long chatId;
    private int messageId;

    public TelegramGameDisplay(ITelegramBot bot, long chatId, int messageId) {
        this.bot = bot;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    @Override
    public void displayStartMessage() {
        var startButton = new InlineKeyboardButton();
        startButton.setText("Начать");
        startButton.setCallbackData(CallbackPrefix.SOLO_GAME + " start");
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(startButton));
        this.bot.editMessage("Нажмите кнопку \"Начать\" для старта игры.", chatId, messageId, buttons);
    }
    @Override
    public void updateGameStateView(Question currentQuestion, Player player) {
        List<List<InlineKeyboardButton>> answerOptionsButtons = new ArrayList<>();
        for (var i=0; i<currentQuestion.answerOptions.size(); i++) {
            var option = new InlineKeyboardButton();
            option.setText(String.format("%d. %s", i+1, currentQuestion.answerOptions.get(i)));
            option.setCallbackData(CallbackPrefix.SOLO_GAME + " " + currentQuestion.answerOptions.get(i));
            answerOptionsButtons.add(List.of(option));
        }
        this.bot.editMessage(currentQuestion.questionTitle
                + "\n\n"
                + currentQuestion.questionDescription
                + "\n\n"
                + "Текущее количество очков игрока: "
                + player.score,
                chatId,
                messageId,
                answerOptionsButtons);
    }
    @Override
    public void displayEndMessage(Player player) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        var button = this.bot.createButton("Вернуться в меню",
                CallbackPrefix.MENU + " /menu");
        buttons.add(List.of(button));
        this.bot.editMessage("Игра окончена. Финальный счёт игрока: " + player.score,
                chatId,
                messageId,
                buttons);
    }
}
