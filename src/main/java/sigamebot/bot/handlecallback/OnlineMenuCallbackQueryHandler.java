package sigamebot.bot.handlecallback;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.core.*;
import sigamebot.utilities.properties.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnlineMenuCallbackQueryHandler implements ICallbackQueryHandler {
    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var display = SigameBot.displays.get(chatId);
        var splitData = callData.split(" ");
        if(Objects.equals(splitData[1], "create")){
            if(Objects.equals(splitData[2], "name")){
                var text = "Введите название игры";
                List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                buttons.add(List.of(ITelegramBot.createInlineKeyboardButton("Назад",
                        CallbackPrefix.MENU + CommandNames.ONLINE_MENU_COMMAND_NAME)));
                display.updateMenuMessage(text, buttons);
            }
        }
    }
}
