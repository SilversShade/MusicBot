package sigamebot.ui.gamedisplaying;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.Question;

import java.util.List;

public interface IGameDisplay {
    void displayStartMessage();
    void updateGameStateView(Question currentQuestion, Player player, int answerTimeInSeconds);
    void updateMenuMessage(String text, List<List<InlineKeyboardButton>> buttons);
    void updateMenuMessage(String text);
    void displayEndMessage(Player player);

    void requestNickname();
}
