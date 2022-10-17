package SiGameBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class bot extends TelegramLongPollingBot {

    int messageId;

    @Override
    public void onUpdateReceived(Update update) {
        // Тестовая программа
        /*
        if(update.hasMessage() && update.getMessage().getText().equals("test")){
            long chatId = update.getMessage().getChatId();
            messageId = sendMessage("Test", chatId);
            if(messageId == -1) System.out.print("Error");
        }
        if(update.hasMessage() && update.getMessage().getText().equals("edit")){
            long chatId = update.getMessage().getChatId();
            if(editMessage("Test2", chatId, messageId) == -1) System.out.print("Error");
        }
        if(update.hasMessage() && update.getMessage().getText().equals("delete")){
            long chatId = update.getMessage().getChatId();
            if(deleteMessage(chatId, messageId) == -1) System.out.print("Error");
        }*/
    }

    @Override
    public String getBotUsername() {
        return "SIGame";
    }

    @Override
    public String getBotToken() {
        return "5723529211:AAFYshIIkyFwyTI7RhH_Vjkr9zl8bmJYJW8";
    }

    // Отправка сообщений
    public int sendMessage(String text, long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int sendMessage(String text, long chatId, List<List<InlineKeyboardButton>> buttons){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }
    }
    public int sendMessage(String text, long chatId, InlineKeyboardMarkup keyboard){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    // Редактирование сообщения
    public int editMessage(String text, long chatId, int messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int editMessage(String text, long chatId, int messageId, List<List<InlineKeyboardButton>> buttons){
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(buttons);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
    public int editMessage(String text, long chatId, int messageId, InlineKeyboardMarkup keyboard){
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(text);
        message.setMessageId(messageId);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }

    // Удаление сообщения
    public int deleteMessage(long chatId, int messageId){
        DeleteMessage message = new DeleteMessage();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        try {
            execute(message);
            return 0;
        } catch (TelegramApiException e) {
            return -1;
        }

    }
}

