package sigamebot.ui.gamedisplaying;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.settings.AnswerTimer;
import sigamebot.logic.Player;
import sigamebot.logic.scenariologic.Question;

import java.util.List;
import java.util.Timer;

public interface IGameDisplay {
    void displayStartMessage();
    void updateGameStateView(Question currentQuestion, Player player);
    void updateMenuMessage(String text, List<List<InlineKeyboardButton>> buttons);
    void displayEndMessage(Player player);
}
