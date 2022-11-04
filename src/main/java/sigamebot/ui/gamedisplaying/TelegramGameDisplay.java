package sigamebot.ui.gamedisplaying;

import sigamebot.bot.core.ITelegramBot;
import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.Question;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import java.util.ArrayList;
import java.util.List;

public class TelegramGameDisplay implements IGameDisplay{
    private final ITelegramBot bot;
    private final long chatId;
    private final int messageId;

    public TelegramGameDisplay(ITelegramBot bot, long chatId, int messageId) {
        this.bot = bot;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    @Override
    public void displayStartMessage() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Начать", CallbackPrefix.SOLO_GAME
                + " "
                + "start")));
        this.bot.editMessage("Нажмите кнопку \"Начать\" для старта игры.", chatId, messageId, buttons);
    }
    @Override
    public void updateGameStateView(Question currentQuestion, Player player) {
        List<List<InlineKeyboardButton>> answerOptionsButtons = new ArrayList<>();
        for (var i=0; i<currentQuestion.answerOptions.size(); i++) {
            answerOptionsButtons.add(List.of(ITelegramBot.createInlineKeyboardButton(String.format("%d. %s", i+1,
                            currentQuestion.answerOptions.get(i)),
                    CallbackPrefix.SOLO_GAME + " " + currentQuestion.answerOptions.get(i))));
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
        var button = ITelegramBot.createInlineKeyboardButton("Вернуться в меню",
                CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME);
        buttons.add(List.of(button));
        this.bot.editMessage("Игра окончена. Финальный счёт игрока: " + player.score,
                chatId,
                messageId,
                buttons);
    }
}
