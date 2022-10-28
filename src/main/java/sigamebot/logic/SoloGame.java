package sigamebot.logic;

import sigamebot.bot.core.SigameBot;
import sigamebot.ui.gamedisplaying.IGameDisplay;
import sigamebot.logic.scenariologic.Category;

import java.util.HashMap;
import java.util.Map;

public class SoloGame {
    private static final Map<Long, SoloGame> ongoingSoloGames = new HashMap<>();
    private final Category scenario;
    private final Player player;
    private int currentQuestion;
    public static Map<Long, SoloGame> getOngoingSoloGames() {
        return ongoingSoloGames;
    }
    public SoloGame(long chatId, Category scenario){
        player = new Player("player", chatId);
        this.scenario = scenario;
        this.currentQuestion = 0;
    }
    public void start(){
        ongoingSoloGames.put(player.chatId, this);
        SigameBot.gameDisplay.displayStartMessage(player);
    }
    public void finish(long chatId){
        ongoingSoloGames.remove(chatId);
    }
    public void nextQuestion(String playerResponse) {
        if (currentQuestion == 0) {
            SigameBot.gameDisplay.deleteSomeMessage(player);
            SigameBot.gameDisplay.updateQuestionStateView(scenario.questions.get(currentQuestion), this.player);
            currentQuestion++;
            return;
        }

        var previousQuestion = this.scenario.questions.get(currentQuestion-1);
        if (playerResponse.equals(previousQuestion.correctAnswer))
            this.player.score += previousQuestion.cost;
        else this.player.score -= previousQuestion.cost;

        if (currentQuestion == this.scenario.questions.size()) {
            SigameBot.gameDisplay.displayEndMessage(player);
            ongoingSoloGames.get(this.player.chatId).finish(this.player.chatId);
            return;
        }

        SigameBot.gameDisplay.updateQuestionStateView(scenario.questions.get(currentQuestion), this.player);
        currentQuestion++;
    }
}