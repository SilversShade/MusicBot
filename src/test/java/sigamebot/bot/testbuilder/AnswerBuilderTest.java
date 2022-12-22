package sigamebot.bot.testbuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sigamebot.bot.botstate.AnswerBuilderStates;

public class AnswerBuilderTest {

    public final AnswerBuilder answerBuilder = new AnswerBuilder();

    private void changeStateAndCallGetText(AnswerBuilderStates toState, String text) {
        answerBuilder.state = toState;
        answerBuilder.getText(text);
    }

    @ParameterizedTest
    @CsvSource({"NONE, CORRECT", "CORRECT, ANSWER", "ANSWER, END"})
    public void assertGetTextChangesStateCorrectly(AnswerBuilderStates initialState, AnswerBuilderStates afterGetTextCall) {
        changeStateAndCallGetText(initialState, "random text");
        assertEquals(afterGetTextCall, answerBuilder.state);
    }

    @Test
    public void assertGetTextHandlesEndStateCorrectly() {
        changeStateAndCallGetText(AnswerBuilderStates.END, "нет");
        assertEquals(AnswerBuilderStates.NONE, answerBuilder.state);
        changeStateAndCallGetText(AnswerBuilderStates.END, "да");
        assertEquals(AnswerBuilderStates.ANSWER, answerBuilder.state);
    }

    private void assertGetButtonsReturnsListOfCorrectSize(AnswerBuilderStates state, int size) {
        answerBuilder.state = state;
        assertEquals(size, answerBuilder.getButtons().size());
    }

    @Test
    public void assertGetButtonsReturnsListOfSizeTwoWhenStateIsEnd() {
        assertGetButtonsReturnsListOfCorrectSize(AnswerBuilderStates.END, 2);
    }

    @ParameterizedTest
    @CsvSource({"CORRECT", "ANSWER", "NONE"})
    public void assertGetButtonsReturnsEmptyListWhenStateIsNotEnd(AnswerBuilderStates state) {
        assertGetButtonsReturnsListOfCorrectSize(state, 0);
    }


}
