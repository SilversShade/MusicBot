package sigamebot.bot.handlecallback;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import java.util.ArrayList;
import java.util.List;

public class MenuCallbackQueryHandler implements ICallbackQueryHandler{
    private final ITelegramBot bot;
    public MenuCallbackQueryHandler(ITelegramBot bot) {
        this.bot = bot;
    }
    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var display = SigameBot.displays.get(chatId);
        var splitData = callData.split(" ");
        if(SigameBot.commandMap.containsKey(splitData[1])) {
            SigameBot.commandMap.get(splitData[1]).executeCommand(chatId);
        }
        else
        {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(List.of(ITelegramBot.
                    createInlineKeyboardButton("Меню",
                            CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME)));
            bot.editMessage("В разработке", chatId, messageId);

        }
    }
}
