package sigamebot.bot.botstate;

public enum SigameBotState implements ITelegramBotState {
    PACK_REQUESTED {
        @Override
        public ITelegramBotState nextState() {
            return DEFAULT_STATE;
        }
    },
    DEFAULT_STATE {
        @Override
        public ITelegramBotState nextState() {
            return PACK_REQUESTED;
        }
    }
}
