package SiGameBot.GameDisplaying;

import SiGameBot.Logic.Player;
import SiGameBot.Logic.ScenarioLogic.Question;

public interface IGameDisplay {
    void displayStartMessage();
    void updateGameStateView(Question currentQuestion, Player player);

    void displayEndgameMessage(Player player);
}
