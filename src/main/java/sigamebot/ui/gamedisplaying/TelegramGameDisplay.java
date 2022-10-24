package sigamebot.ui.gamedisplaying;

import sigamebot.bot.ICallbackQueryHandler;
import sigamebot.bot.ITelegramBot;
import sigamebot.logic.Player;
import sigamebot.logic.SoloGame;
import sigamebot.logic.scenariologic.Question;
import sigamebot.bot.SigameBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class TelegramGameDisplay implements IGameDisplay, ICallbackQueryHandler {
    private final ITelegramBot bot;
    private final long chatId;
    private int messageId;

    public static final String SOLO_GAME_CALLBACK_PREFIX = "solo";

    public TelegramGameDisplay(ITelegramBot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    @Override
    public void displayStartMessage() {
        var startButton = new InlineKeyboardButton();
        startButton.setText("Начать");
        startButton.setCallbackData(SOLO_GAME_CALLBACK_PREFIX + " start");
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(startButton));
        messageId = this.bot.sendMessage("Нажмите кнопку \"Начать\" для старта игры.", chatId, buttons);
    }
    @Override
    public void updateGameStateView(Question currentQuestion, Player player) {
        List<List<InlineKeyboardButton>> answerOptionsButtons = new ArrayList<>();
        for (var i=0; i<currentQuestion.answerOptions.size(); i++) {
            var option = new InlineKeyboardButton();
            option.setText(String.format("%d. %s", i+1, currentQuestion.answerOptions.get(i)));
            option.setCallbackData(SOLO_GAME_CALLBACK_PREFIX + " " + currentQuestion.answerOptions.get(i));
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
        this.bot.editMessage("Игра окончена. Финальный счёт игрока: " + player.score, chatId, messageId);
    }


    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {
        var parsedData = callData.split(" ", 2);
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(parsedData[1]);
    }
}
