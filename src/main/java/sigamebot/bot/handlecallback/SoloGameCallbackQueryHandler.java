package sigamebot.bot.handlecallback;

import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.user.ChatInfo;

public class SoloGameCallbackQueryHandler implements ICallbackQueryHandler{

    public static ChatInfo chatInfoGlobal;

    public static String parsedDataGlobal;

    public static boolean nicknameReceived = false;

    @Override
    public void handleCallbackQuery(String callData, Integer messageId, ChatInfo chatInfo){
        var parsedData = callData.split(" ", 2);

        chatInfoGlobal = chatInfo;
        parsedDataGlobal = parsedData[1];

        if (!nicknameReceived) {
            chatInfo.getOngoingSoloGame().gameDisplay.requestNickname();
            return;
        }

        if (!TelegramGameDisplay.isAwaitingNickname)
            chatInfo.getOngoingSoloGame().nextQuestion(parsedData[1]);
    }
}
