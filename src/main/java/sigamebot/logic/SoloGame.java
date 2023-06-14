package sigamebot.logic;

import sigamebot.bot.settings.AnswerTimer;
import sigamebot.logic.scenariologic.Category;
import sigamebot.ui.gamedisplaying.IGameDisplay;
import sigamebot.user.Admin;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.JsonParser;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SoloGame {
    private final Category scenario;
    private final Player player;
    private int currentQuestion;
    public final IGameDisplay gameDisplay;

    private final AnswerTimer answerTimer;
    private final ScheduledExecutorService timer;
    private ScheduledFuture<?> timerSchedulingResult;

    private final ChatInfo chatInfo;

    private SoloGame(ChatInfo chatInfo, Category scenario, IGameDisplay gameDisplay) {
        this.player = new Player("player");
        this.scenario = scenario;
        this.gameDisplay = gameDisplay;
        this.chatInfo = chatInfo;
        this.answerTimer = new AnswerTimer(chatInfo);
        this.timer = Executors.newScheduledThreadPool(1);
        this.currentQuestion = 0;
    }

    public static void startNewSoloGame(ChatInfo chatInfo,
                                        int packNumberInDirectory,
                                        String pathToPackFolder,
                                        IGameDisplay gameDisplay) {
        Category parsedGame;
        try {
            parsedGame = JsonParser.getGameFromJson(packNumberInDirectory, pathToPackFolder);
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }

        chatInfo.setOngoingSoloGame(new SoloGame(chatInfo, parsedGame, gameDisplay));
        chatInfo.getOngoingSoloGame().start();
    }

    public static void startNewSoloGame(ChatInfo chatInfo,
                                        Category category,
                                        IGameDisplay gameDisplay) {
        chatInfo.setOngoingSoloGame(new SoloGame(chatInfo, category, gameDisplay));
        chatInfo.getOngoingSoloGame().start();
    }

    public void start() {
        this.gameDisplay.displayStartMessage();
    }

    public void finish() {
        Admin.results.add(new TestResult(Admin.lastNickname, Admin.lastGameName, player.score));
        chatInfo.setOngoingSoloGame(null);
    }

    public void nextQuestion(String playerResponse) {
        if (currentQuestion == 0) {
            this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion), this.player, chatInfo.getAnswerTimeInSeconds());
            currentQuestion++;
            if (chatInfo.getAnswerTimeInSeconds() != 0)
                timerSchedulingResult = timer.schedule(answerTimer, chatInfo.getAnswerTimeInSeconds(), TimeUnit.SECONDS);
            return;
        }

        if (timerSchedulingResult != null && !timerSchedulingResult.isCancelled())
            timerSchedulingResult.cancel(false);

        var previousQuestion = this.scenario.questions.get(currentQuestion - 1);
        if (playerResponse != null && playerResponse.equals(previousQuestion.correctAnswer))
            this.player.score += previousQuestion.cost;


        if (currentQuestion == this.scenario.questions.size()) {
            this.gameDisplay.displayEndMessage(player);
            chatInfo.getOngoingSoloGame().finish();
            return;
        }

        this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion), this.player, chatInfo.getAnswerTimeInSeconds());
        currentQuestion++;
        if (chatInfo.getAnswerTimeInSeconds() != 0)
            timerSchedulingResult = timer.schedule(answerTimer, chatInfo.getAnswerTimeInSeconds(), TimeUnit.SECONDS);
    }
}