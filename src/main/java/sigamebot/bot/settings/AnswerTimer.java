package sigamebot.bot.settings;

import sigamebot.logic.SoloGame;

import java.util.HashMap;
import java.util.Map;

public class AnswerTimer implements Runnable {

    private static final int DEFAULT_ANSWER_TIME_IN_SECONDS = 10;
    //Todo
    public static final Map<Long, Integer> chatIdToAnswerTimeInSeconds = new HashMap<>();
    private final long chatId;

    public AnswerTimer(long chatId) {
        this.chatId = chatId;
        if (!chatIdToAnswerTimeInSeconds.containsKey(chatId))
            chatIdToAnswerTimeInSeconds.put(chatId, DEFAULT_ANSWER_TIME_IN_SECONDS);
    }

    @Override
    public void run() {
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(null);
    }
}
