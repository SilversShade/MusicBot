package sigamebot.bot.core;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface ITelegramBot {
    int sendMessage(String text, long chatId);

    int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons);

    int editMessage(String text, long chatId, int messageId);

    int editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons);

    int deleteMessage(long chatId, int messageId);
}
