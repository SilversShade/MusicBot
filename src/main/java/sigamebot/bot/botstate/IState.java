package sigamebot.bot.botstate;

public interface IState {
    IState switchToNextState(IState nextState);
}
