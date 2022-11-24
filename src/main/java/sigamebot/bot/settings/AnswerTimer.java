package sigamebot.bot.settings;

import sigamebot.user.ChatInfo;

public class AnswerTimer implements Runnable {

    public static final int DEFAULT_ANSWER_TIME_IN_SECONDS = 10;

    private final ChatInfo chatInfo;

    public AnswerTimer(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }

    @Override
    public void run() {
        chatInfo.getOngoingSoloGame().nextQuestion(null);
    }
}
