package SiGameBot.GameDisplaying;

import SiGameBot.Logic.ScenarioLogic.Question;

public interface IGameDisplay {
    void displayStartMessage();
    void updateGameStateView(Question currentQuestion);
}
