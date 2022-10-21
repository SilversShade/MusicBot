package sigamebot.gamedisplaying;

import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.Question;

public interface IGameDisplay {
    void displayStartMessage();
    void updateGameStateView(Question currentQuestion, Player player);

    void displayEndgameMessage(Player player);
}
