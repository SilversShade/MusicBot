package sigamebot.logic;

import sigamebot.bot.settings.AnswerTimer;
import sigamebot.logic.scenariologic.Category;
import sigamebot.ui.gamedisplaying.IGameDisplay;
import sigamebot.utilities.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SoloGame {
    private final Category scenario;
    private final Player player;
    private final long chatId;
    private int currentQuestion;
    public final IGameDisplay gameDisplay;

    private final AnswerTimer answerTimer;
    private final ScheduledExecutorService timer;
    private ScheduledFuture<?> timerSchedulingResult;

    private static final Map<Long, SoloGame> ongoingSoloGames = new HashMap<>();

    private SoloGame(long chatId, Category scenario, IGameDisplay gameDisplay) {
        this.player = new Player("player");
        this.chatId = chatId;
        this.scenario = scenario;
        this.gameDisplay = gameDisplay;
        this.answerTimer = new AnswerTimer(chatId);
        this.timer = Executors.newScheduledThreadPool(1);
        this.currentQuestion = 0;
    }

    public static Map<Long, SoloGame> getOngoingSoloGames() {
        return ongoingSoloGames;
    }

    public static void startNewSoloGame(long chatId,
                                        int packNumberInDirectory,
                                        String pathToPackFolder,
                                        IGameDisplay gameDisplay) {
        Category parsedGame;
        try {
            parsedGame = JsonParser.getGameFromJson(packNumberInDirectory, pathToPackFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        SoloGame.getOngoingSoloGames().put(chatId, new SoloGame(chatId,
                parsedGame,
                gameDisplay));
        SoloGame.getOngoingSoloGames().get(chatId).start();
    }

    public static void startNewSoloGame(long chatId,
                                        Category category,
                                        IGameDisplay gameDisplay) {
        SoloGame.getOngoingSoloGames().put(chatId, new SoloGame(chatId,
                category,
                gameDisplay));
        SoloGame.getOngoingSoloGames().get(chatId).start();
    }

    public void start() {
        //TODO: validate chosen game
        this.gameDisplay.displayStartMessage();
    }

    public void finish(long chatId) {
        ongoingSoloGames.remove(chatId);
    }

    public void nextQuestion(String playerResponse) {
        if (currentQuestion == 0) {
            this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion), this.player);
            currentQuestion++;
            timerSchedulingResult = timer.schedule(answerTimer, AnswerTimer.chatIdToAnswerTimeInSeconds.get(chatId), TimeUnit.SECONDS);
            return;
        }

        if (!timerSchedulingResult.isCancelled())
            timerSchedulingResult.cancel(false);

        var previousQuestion = this.scenario.questions.get(currentQuestion - 1);
        if (playerResponse != null && playerResponse.equals(previousQuestion.correctAnswer))
            this.player.score += previousQuestion.cost;
        else this.player.score -= previousQuestion.cost;

        if (currentQuestion == this.scenario.questions.size()) {
            this.gameDisplay.displayEndMessage(player);
            ongoingSoloGames.get(chatId).finish(chatId);
            return;
        }

        this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion), this.player);
        currentQuestion++;
        timerSchedulingResult = timer.schedule(answerTimer, AnswerTimer.chatIdToAnswerTimeInSeconds.get(chatId), TimeUnit.SECONDS);
    }
}