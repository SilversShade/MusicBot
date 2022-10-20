package SiGameBot.Logic;

import SiGameBot.GameDisplaying.IGameDisplay;
import SiGameBot.Logic.ScenarioLogic.Category;
import SiGameBot.SigameBot;

public class SoloGame {
    private final SigameBot bot;
    private final Category scenario;
    private final Player player;

    private int currentQuestion;
    public final IGameDisplay gameDisplay;
    public SoloGame(SigameBot bot, long chatId, Category scenario, IGameDisplay gameDisplay){
        this.bot = bot;
        player = new Player("player", chatId);
        this.scenario = scenario;
        this.gameDisplay = gameDisplay;
        this.currentQuestion = 0;
    }
    public void start(){
        this.gameDisplay.displayStartMessage();
    }

    public void nextQuestion(String playerResponse) {
        if (currentQuestion == 0) {
            this.gameDisplay.updateGameStateView(scenario.questions.get(currentQuestion));
            return;
        }

    }
}