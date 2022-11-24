package sigamebot.bot.handlecallback;

import sigamebot.user.ChatInfo;

public class SoloGameCallbackQueryHandler implements ICallbackQueryHandler{

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, ChatInfo chatInfo){
        var parsedData = callData.split(" ", 2);
        chatInfo.getOngoingSoloGame().nextQuestion(parsedData[1]);
    }
}
