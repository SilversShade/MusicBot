package sigamebot.bot.settings;

import sigamebot.bot.commands.MenuCommand;
import sigamebot.logic.SoloGame;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;

import java.util.List;

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
