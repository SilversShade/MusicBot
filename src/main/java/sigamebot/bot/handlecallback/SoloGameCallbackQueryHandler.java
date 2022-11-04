package sigamebot.bot.handlecallback;

import sigamebot.logic.SoloGame;

public class SoloGameCallbackQueryHandler implements ICallbackQueryHandler{

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, Long chatId){
        var parsedData = callData.split(" ", 2);
        SoloGame.getOngoingSoloGames().get(chatId).nextQuestion(parsedData[1]);
    }
}
