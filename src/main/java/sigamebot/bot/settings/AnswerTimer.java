package sigamebot.bot.settings;

import sigamebot.bot.commands.MenuCommand;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerTimer implements Runnable {

    public static final Map<Long, Integer> chatIdToAnswerTimeInSeconds = new HashMap<>();
    private final long chatId;

    public AnswerTimer(long chatId, int answerTime) {
        this.chatId = chatId;
        chatIdToAnswerTimeInSeconds.put(chatId, answerTime);
    }

    @Override
    public void run() {
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(null);
    }
}
