package sigamebot.bot.core;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public abstract class TelegramBotMessageApi extends TelegramLongPollingBot {
    public abstract int sendMessage(String text, long chatId);

    public abstract int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons);

    public abstract boolean editMessage(String text, long chatId, int messageId);

    public abstract boolean editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons);

    public abstract void deleteMessage(long chatId, int messageId);

    public static InlineKeyboardButton createInlineKeyboardButton(String text, String callBackData) {
        var button = new InlineKeyboardButton();
        button.setCallbackData(callBackData);
        button.setText(text);
        return button;
    }
}
