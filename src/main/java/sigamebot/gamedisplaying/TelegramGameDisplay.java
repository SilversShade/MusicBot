package sigamebot.gamedisplaying;

import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.Question;
import sigamebot.logic.SoloGame;
import sigamebot.SigameBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TelegramGameDisplay implements IGameDisplay{

    private final SigameBot bot;

    private final long chatId;

    private int messageId;
    public TelegramGameDisplay(SigameBot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    @Override
    public void displayStartMessage() {
        InlineKeyboardButton startButton = new InlineKeyboardButton();
        startButton.setText("Начать");
        startButton.setCallbackData("solo start");
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(startButton));
        messageId = this.bot.sendMessage("Нажмите кнопку \"Начать\" для старта игры.", chatId, buttons);
    }

    @Override
    public void updateGameStateView(Question currentQuestion, Player player) {
        List<List<InlineKeyboardButton>> answerOptionsButtons = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (var i=0; i<currentQuestion.answerOptions.size(); i++) {
            var option = new InlineKeyboardButton();
            option.setText(String.format("%d. %s", i+1, currentQuestion.answerOptions.get(i)));
            option.setCallbackData("solo " + currentQuestion.answerOptions.get(i));
            if (i%2 == 0)
                row = new ArrayList<>();
            row.add(option);
            if (i%2 != 0)
                answerOptionsButtons.add(row);
        }
        this.bot.editMessage("Вопрос номер "
                + (currentQuestion.questionNumber+1)
                + ".\n\n"
                + currentQuestion.questionTitle
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
    public void displayEndgameMessage(Player player) {
        this.bot.editMessage("Игра окончена. Финальный счёт игрока: " + player.score, chatId, messageId);
        SoloGame.getOngoingSoloGames().get(chatId).finish(chatId);
    }
}
