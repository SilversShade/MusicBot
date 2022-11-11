package sigamebot.bot.botstate.automats;

import sigamebot.bot.botstate.ITelegramBotStage;

public interface IAutomats {
    ITelegramBotStage getStage();
    void next();
}
