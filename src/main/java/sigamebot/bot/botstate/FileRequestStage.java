package sigamebot.bot.botstate;

public enum FileRequestStage implements ITelegramBotStage {
    PACK_REQUESTED {
        @Override
        public ITelegramBotStage nextState() {
            return DEFAULT_STATE;
        }
    },
    DEFAULT_STATE {
        @Override
        public ITelegramBotStage nextState() {
            return PACK_REQUESTED;
        }
    }
}
