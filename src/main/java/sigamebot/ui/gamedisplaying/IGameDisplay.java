package sigamebot.ui.gamedisplaying;

import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.*;

public interface IGameDisplay {
    void displayStartMessage(Player player);
    void updateQuestionStateView(Question currentQuestion, Player player);
    void updateCategoryStateView(Category currentCategory, int categoryNumber, Player player);
    void updateRoundStateView(Round currentRound, Player player);
    void displayEndMessage(Player player);
    void deleteSomeMessage(Player player);
}
