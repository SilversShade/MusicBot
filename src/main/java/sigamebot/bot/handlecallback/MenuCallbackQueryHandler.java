package sigamebot.bot.handlecallback;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.properties.CallbackPrefix;
import sigamebot.utilities.properties.CommandNames;

import java.util.ArrayList;
import java.util.List;

public class MenuCallbackQueryHandler implements ICallbackQueryHandler {
    private final TelegramBotMessageApi bot;

    public MenuCallbackQueryHandler(TelegramBotMessageApi bot) {
        this.bot = bot;
    }

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, ChatInfo chatInfo) {
        var splitData = callData.split(" ");
        if (SigameBot.getCommandMap().containsKey(splitData[1]))
            SigameBot.getCommandMap().get(splitData[1]).executeCommand(chatInfo);
        else {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            buttons.add(List.of(TelegramBotMessageApi.
                    createInlineKeyboardButton("Меню",
                            CallbackPrefix.MENU + " " + CommandNames.MENU_COMMAND_NAME)));
            bot.editMessage("В разработке", chatInfo.getChatId(), messageId, buttons);
        }
    }
}
