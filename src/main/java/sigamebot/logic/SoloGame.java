package sigamebot.logic;

import sigamebot.gamedisplaying.IGameDisplay;
import sigamebot.logic.scenariologic.Category;

import java.util.HashMap;
import java.util.Map;

public class SoloGame {
    private final Category scenario;
    private final Player player;
    private int currentQuestion;
    public final IGameDisplay gameDisplay;
    private static final Map<Long, SoloGame> ongoingSoloGames = new HashMap<>();
    public SoloGame(long chatId, Category scenario, IGameDisplay gameDisplay){
        player = new Player("player", chatId);
        this.scenario = scenario;
        this.gameDisplay = gameDisplay;
        this.currentQuestion = 0;
    }

    public static Map<Long, SoloGame> getOngoingSoloGames() {
        return ongoingSoloGames;
    }

    public void start(){
        this.gameDisplay.displayStartMessage();
    }

    public void finish(long chatId){
        ongoingSoloGames.remove(chatId);
    }

    public void nextQuestion(String playerResponse) {
        if (currentQuestion == 0) {
            this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion), this.player);
            currentQuestion++;
            return;
        }

        var previousQuestion = this.scenario.questions.get(currentQuestion-1);
        if (playerResponse.equals(previousQuestion.correctAnswer))
            this.player.score += previousQuestion.cost;
        else this.player.score -= previousQuestion.cost;

        if (currentQuestion == this.scenario.questions.size()) {
            this.gameDisplay.displayEndgameMessage(player);
            return;
        }

        this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion), this.player);
        currentQuestion++;
    }
}