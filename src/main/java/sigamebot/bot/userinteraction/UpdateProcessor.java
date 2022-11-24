package sigamebot.bot.userinteraction;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.botstate.classes.SigameBotState;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.bot.handlecallback.ICallbackQueryHandler;
import sigamebot.bot.settings.Settings;
import sigamebot.bot.userinteraction.filehandlers.UserFileHandler;
import sigamebot.exceptions.IncorrectSettingsParameterException;
import sigamebot.exceptions.UserPackHandlerException;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.user.ChatInfo;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class UpdateProcessor {

    private final TelegramBotMessageApi bot;

    private final Map<Long, ChatInfo> chatInfoMap = new HashMap<>();

    public UpdateProcessor(TelegramBotMessageApi bot) {
        this.bot = bot;
    }

    private boolean isBotAwaitingSettingUp(Message message) {
        return chatInfoMap.containsKey(message.getChatId())
                && chatInfoMap.get(message.getChatId()).getGameDisplay().currentBotState.getState() == BotStates.SETTING_UP
                && Settings.userResponseDelegator.containsKey(SigameBotState.currentSettingsOption);
    }


    public void handleReceivedUpdates(Update update, Map<String, ICallbackQueryHandler> queryHandlerMap) {
        if (update.hasCallbackQuery())
            processCallbackQuery(update, queryHandlerMap);

        if (update.hasMessage()) {
            initializeChatInfo(update.getMessage());
            processMessage(update.getMessage());
        }
    }

    private void processSettingsAdjustment(@NotNull Message message) {
        if (isBotAwaitingSettingUp(message)) {
            try {
                Settings.userResponseDelegator.get(SigameBotState.currentSettingsOption)
                        .processUserResponse(message, chatInfoMap.get(message.getChatId()));
            } catch (IncorrectSettingsParameterException e) {
                bot.sendMessage(e.getMessage(), message.getChatId());
            }
            bot.deleteMessage(message.getChatId(), message.getMessageId());
        }
    }

    private void initializeChatInfo(@NotNull Message message) {
        if (!chatInfoMap.containsKey(message.getChatId())) {
            var messageId = bot.sendMessage("Start message", message.getChatId());
            chatInfoMap.put(message.getChatId(),
                    new ChatInfo(message.getChatId(), new TelegramGameDisplay(bot, message.getChatId(), messageId)));
        }
    }
    private void processMessage(@NotNull Message message) {
        processSettingsAdjustment(message);
        processCommands(message);
        processDocuments(message);
    }

    private void processDocuments(@NotNull Message message) {
        if (message.hasDocument()) {
            try {
                UserFileHandler.handleUserFiles(bot, message, chatInfoMap.get(message.getChatId()));
                bot.deleteMessage(message.getChatId(), message.getMessageId());
            } catch (UserPackHandlerException e) {
                bot.sendMessage(e.getMessage(), message.getChatId());
            }
        }
    }

    private void processCallbackQuery(Update update,
                                             Map<String, ICallbackQueryHandler> queryHandlerMap) {
        var callData = update.getCallbackQuery().getData();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var chatId = update.getCallbackQuery().getMessage().getChatId();

        var callbackPrefix = callData.split(" ")[0];
        if (queryHandlerMap.containsKey(callbackPrefix))
            queryHandlerMap.get(callbackPrefix).handleCallbackQuery(callData, messageId, chatInfoMap.get(chatId));

    }

    private void processCommands(@NotNull Message message){
        if (message.getText() != null && SigameBot.getCommandMap().containsKey(message.getText())) {
            SigameBot.getCommandMap().get(message.getText()).executeCommand(chatInfoMap.get(message.getChatId()));
            bot.deleteMessage(message.getChatId(), message.getMessageId());
        }
    }
}
