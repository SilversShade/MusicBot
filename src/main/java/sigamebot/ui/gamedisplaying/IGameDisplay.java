package sigamebot.ui.gamedisplaying;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.logic.scenariologic.Question;

import java.util.List;

public interface IGameDisplay {
    void displayStartMessage();
    void updateSoloGameStateView(Question currentQuestion, int score);
    void updateMenuMessage(String text, List<List<InlineKeyboardButton>> buttons);
    void displayEndMessage(int score);
}
