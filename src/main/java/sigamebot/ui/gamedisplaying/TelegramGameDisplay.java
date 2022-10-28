package sigamebot.ui.gamedisplaying;

import sigamebot.bot.userinteraction.ICallbackQueryHandler;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.logic.Player;
import sigamebot.logic.SoloGame;
import sigamebot.logic.scenariologic.Category;
import sigamebot.logic.scenariologic.Question;
import sigamebot.utilities.callbackPrefix;
import sigamebot.utilities.commandString;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.logic.scenariologic.Round;

import java.util.ArrayList;
import java.util.List;

public class TelegramGameDisplay implements IGameDisplay, ICallbackQueryHandler {
    private final ITelegramBot bot;
    public TelegramGameDisplay(ITelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void displayStartMessage(Player player) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        var button = new InlineKeyboardButton();
        button.setText("Начать");
        button.setCallbackData(callbackPrefix.SOLO_MENU + " start");
        buttons.add(List.of(button));
        player.someMessageId.add(bot.sendMessage("Ваша игра готова", player.chatId, buttons));
    }
    @Override
    public void updateQuestionStateView(Question currentQuestion, Player player) {
        String text = currentQuestion.questionTitle
                + "\n\n"
                + currentQuestion.questionDescription
                + "\n\n"
                + "Текущее количество ваших очков: "
                + player.score;
        if(currentQuestion.type == 2){
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            ArrayList<InlineKeyboardButton> raw = new ArrayList<>();
            for(var i = 0; i < currentQuestion.answerOptions.size(); i++) {
                if (i % 2 == 0)
                    raw = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(currentQuestion.answerOptions.get(i));
                button.setCallbackData(callbackPrefix.SOLO_GAME + " " + currentQuestion.answerOptions.get(i));
                raw.add(button);
                if (i % 2 == 1)
                    buttons.add(raw);
            }
            if(player.questionMessageId == -1)
                player.questionMessageId = bot.sendMessage(text, player.chatId, buttons);
            else
                bot.editMessage(text, player.chatId, player.questionMessageId, buttons);
        }
    }
    @Override
    public void updateCategoryStateView(Category currentCategory, int categoryNumber, Player player) {

    }
    @Override
    public void updateRoundStateView(Round currentRound, Player player) {

    }
    @Override
    public void displayEndMessage(Player player) {
        String text = "Игра окончена"
                + "\n\n"
                + "Ваш счет: "
                + player.score;
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Вернуться в меню");
        button.setCallbackData(callbackPrefix.MENU + " /menu");
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(List.of(button));
        if(player.questionMessageId != -1)
            bot.editMessage(text, player.chatId, player.questionMessageId, buttons);
        else
            bot.sendMessage(text, player.chatId, buttons);
        player = null;
    }
    @Override
    public void deleteSomeMessage(Player player){
        for(var i : player.someMessageId)
            bot.deleteMessage(player.chatId, i);
    }
    public static void handleCallbackQuery(ITelegramBot bot, String callData, Integer messageId, Long chatId) {
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(callData.split(" ", 2)[1]);
    }
}
