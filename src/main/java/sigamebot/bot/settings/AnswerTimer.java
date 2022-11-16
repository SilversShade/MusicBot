package sigamebot.bot.settings;

import sigamebot.logic.SoloGame;

public class AnswerTimer implements Runnable {

    public static int timeForAnswerInSeconds = 5;
    private final long chatId;

    public AnswerTimer(long chatId) {
        this.chatId = chatId;
    }

    @Override
    public void run() {
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(null);
    }
}
