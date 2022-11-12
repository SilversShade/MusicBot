package sigamebot.bot.core;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface ITelegramBot {
    int sendMessage(String text, long chatId);

    int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons);

    int sendDocument(InputFile document, long chatId);

    boolean editMessage(String text, long chatId, int messageId);

    boolean editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons);

    int deleteMessage(long chatId, int messageId);
    static InlineKeyboardButton createInlineKeyboardButton(String text, String callBackData) {
        var button = new InlineKeyboardButton();
        button.setCallbackData(callBackData);
        button.setText(text);
        return button;
    }
}
