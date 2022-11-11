package sigamebot.bot.botstate;

public enum LineRequestStage implements ITelegramBotStage {
    LINE_REQUESTED {
        @Override
        public ITelegramBotStage nextState() {
            return DEFAULT_STATE;
        }
    },
    DEFAULT_STATE {
        @Override
        public ITelegramBotStage nextState() {
            return LINE_REQUESTED;
        }
    }
}
