package sigamebot.bot.botstate;

public enum QuestionBuilderStates implements IState{
    NONE,
    TITLE,
    DESC,
    COST,
    ANSWER,
    END,
    FINISH,
}
