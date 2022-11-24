package sigamebot.user;

import sigamebot.bot.settings.AnswerTimer;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;

public class ChatInfo {
    private final long chatId;
    private int answerTimeInSeconds = AnswerTimer.DEFAULT_ANSWER_TIME_IN_SECONDS;
    private final TelegramGameDisplay gameDisplay;

    private SoloGame ongoingSoloGame;

    public ChatInfo(long chatId, TelegramGameDisplay gameDisplay) {
        this.chatId = chatId;
        this.gameDisplay = gameDisplay;
    }

    public SoloGame getOngoingSoloGame() {
        return ongoingSoloGame;
    }

    public void setOngoingSoloGame(SoloGame game) {
        ongoingSoloGame = game;
    }

    public long getChatId() {
        return chatId;
    }

    public int getAnswerTimeInSeconds() {
        return answerTimeInSeconds;
    }

    public void setAnswerTimeInSeconds(int timeInSeconds) {
        answerTimeInSeconds = timeInSeconds;
    }

    public TelegramGameDisplay getGameDisplay() {
        return gameDisplay;
    }
}
