package sigamebot.bot.handlecallback;

import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;

public class MenuCallbackQueryHandler implements ICallbackQueryHandler{
    private final ITelegramBot bot;
    public MenuCallbackQueryHandler(ITelegramBot bot) {
        this.bot = bot;
    }
    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var splitData = callData.split(" ");
        if(SigameBot.commandMap.containsKey(splitData[1])) {
            bot.deleteMessage(chatId, messageId);
            SigameBot.commandMap.get(splitData[1]).executeCommand(chatId);
        }
        else
            bot.editMessage("В разработке", chatId, messageId);
    }
}
