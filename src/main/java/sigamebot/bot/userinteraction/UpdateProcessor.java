package sigamebot.bot.userinteraction;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import sigamebot.bot.botstate.BotStates;
import sigamebot.bot.botstate.classes.SigameBotState;
import sigamebot.bot.commands.AdminCommand;
import sigamebot.bot.core.SigameBot;
import sigamebot.bot.core.TelegramBotMessageApi;
import sigamebot.bot.handlecallback.ICallbackQueryHandler;
import sigamebot.bot.handlecallback.SoloGameCallbackQueryHandler;
import sigamebot.bot.settings.Settings;
import sigamebot.bot.userinteraction.filehandlers.UserFileHandler;
import sigamebot.exceptions.IncorrectSettingsParameterException;
import sigamebot.exceptions.UserPackHandlerException;
import sigamebot.ui.gamedisplaying.TelegramGameDisplay;
import sigamebot.user.Admin;
import sigamebot.user.ChatInfo;
import sigamebot.utilities.properties.CallbackPrefix;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private boolean isBotBuilding(Message message) {
        return chatInfoMap.containsKey(message.getChatId())
                && chatInfoMap.get(message.getChatId()).getGameDisplay().currentBotState.getState() == BotStates.TEST_DESIGN;
    }


    public void handleReceivedUpdates(Update update, Map<String, ICallbackQueryHandler> queryHandlerMap) {
        if (!update.hasMessage() && TelegramGameDisplay.isAwaitingNickname)
            return;

        if (update.hasMessage() && TelegramGameDisplay.isAwaitingNickname)
            processNickname(update.getMessage());

        if (update.hasCallbackQuery())
            processCallbackQuery(update, queryHandlerMap);

        if (update.hasMessage()) {
            initializeChatInfo(update.getMessage());
            processMessage(update.getMessage(), queryHandlerMap);
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

    private void processNickname(Message message) {
        if (!TelegramGameDisplay.isAwaitingNickname)
            return;

        Admin.lastNickname = message.getText();
        SoloGameCallbackQueryHandler.nicknameReceived = true;
        TelegramGameDisplay.isAwaitingNickname = false;
        bot.deleteMessage(message.getChatId(), message.getMessageId());

        SoloGameCallbackQueryHandler.chatInfoGlobal.getOngoingSoloGame()
                .nextQuestion(SoloGameCallbackQueryHandler.parsedDataGlobal);
    }

    private void processBuilder(@NotNull Message message, Map<String, ICallbackQueryHandler> queryHandlerMap) {
        if (isBotBuilding(message)) {
            queryHandlerMap.get(CallbackPrefix.SOLO_BUILDER).handleCallbackQuery(
                    CallbackPrefix.SOLO_BUILDER + " " + message.getText(), message.getMessageId(),
                    chatInfoMap.get(message.getChatId()));
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

    private void processMessage(@NotNull Message message, Map<String, ICallbackQueryHandler> queryHandlerMap) {
        processSettingsAdjustment(message);
        processBuilder(message, queryHandlerMap);
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

    private void processCommands(Message message) {
        var text = message.getText();

        if (text.startsWith("/admin")) {
            var parsed = text.split(" ");
            if (parsed.length != 2) {
                this.bot.sendMessage("Команда /admin должна принимать ключ доступа первым аргументом.", message.getChatId());
                return;
            }
            if (!Objects.equals(parsed[1], AdminCommand.adminAccessKey)) {
                this.bot.sendMessage("Неверный ключ доступа.", message.getChatId());
                return;
            }

            text = "/admin";
        }

        if (SigameBot.getCommandMap().containsKey(text)) {
            SigameBot.getCommandMap().get(text).executeCommand(chatInfoMap.get(message.getChatId()));
            bot.deleteMessage(message.getChatId(), message.getMessageId());
        }
    }
}
