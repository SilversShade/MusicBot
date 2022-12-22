package sigamebot.bot.testbuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sigamebot.bot.botstate.AnswerBuilderStates;
import sigamebot.bot.botstate.QuestionBuilderStates;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionBuilderTest {

    public final QuestionBuilder questionBuilder = new QuestionBuilder();

    private void changeStateAndCallGetText(QuestionBuilderStates state, String text) {
        questionBuilder.state = state;
        questionBuilder.getText(text);
    }

    @ParameterizedTest
    @CsvSource({"NONE, TITLE", "TITLE, DESC", "DESC, COST", "COST, ANSWER"})
    public void assertGetTextChangesStatesCorrectly(QuestionBuilderStates initialState, QuestionBuilderStates afterGetTextCall) {
        changeStateAndCallGetText(initialState, "random text");
        assertEquals(afterGetTextCall, questionBuilder.state);
    }

    @Test
    public void assertGetTextChangesStateCorrectlyInAnswerCase() {
        final var aBuilder = new AnswerBuilder();
        aBuilder.state = AnswerBuilderStates.END;
        final var qBuilder = new QuestionBuilder(aBuilder);
        qBuilder.state = QuestionBuilderStates.ANSWER;
        qBuilder.getText("нет");
        assertEquals(QuestionBuilderStates.END, qBuilder.state);
    }

    @Test
    public void assertGetTextChangesStateCorrectlyInEndCase() {
        changeStateAndCallGetText(QuestionBuilderStates.END, "нет");
        assertEquals(QuestionBuilderStates.NONE, questionBuilder.state);
        changeStateAndCallGetText(QuestionBuilderStates.END, "да");
        assertEquals(QuestionBuilderStates.TITLE, questionBuilder.state);
    }

    private void assertGetButtonsReturnsCorrectSizedList(QuestionBuilderStates state, int size) {
        questionBuilder.state = state;
        assertEquals(size, questionBuilder.getButtons().size(), size);
    }

    @Test
    public void assertGetButtonsReturnsListOfSizeTwoWhenStateIsEnd() {
       assertGetButtonsReturnsCorrectSizedList(QuestionBuilderStates.END, 2);
    }

    @ParameterizedTest
    @CsvSource({"NONE", "TITLE", "DESC", "COST", "FINISH"})
    public void assertGetButtonsReturnsEmptyListWhenStateIsNotMatching(QuestionBuilderStates state) {
        assertGetButtonsReturnsCorrectSizedList(state, 0);
    }

    @ParameterizedTest
    @CsvSource(value = {"123, 123", "0, 0", "-123, -123"})
    public void assertTryToParseParsesValidStringsCorrectly(String text, int expected) {
        assertEquals(expected, questionBuilder.tryToParse(text));
    }

    @Test
    public void assertTryToParseReturns100WhenLargeNumberIsPassed() {
        assertEquals(100, questionBuilder.tryToParse("972364782678462378468234"));
    }

    @Test
    public void assertTryToParseReturns100WhenNonIntegerPassed() {
        assertEquals(100, questionBuilder.tryToParse("not an integer"));
    }
}
