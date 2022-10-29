package sigamebot.bot.handlecallback;

import sigamebot.bot.core.ITelegramBot;
import sigamebot.bot.core.SigameBot;

public class MenuCallbackQueryHandler implements ICallbackQueryHandler{
    private ITelegramBot bot;
    public MenuCallbackQueryHandler(ITelegramBot bot){ this.bot = bot; }
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var splitedData = callData.split(" ");
        if(SigameBot.commandMap.containsKey(splitedData[1])) {
            bot.deleteMessage(chatId, messageId);
            SigameBot.commandMap.get(splitedData[1]).executeCommand(chatId);
        }
        else
            bot.editMessage("В разработке", chatId, messageId);
    }
}
