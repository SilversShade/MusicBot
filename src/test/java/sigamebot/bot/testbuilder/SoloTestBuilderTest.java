package sigamebot.bot.testbuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import sigamebot.bot.botstate.BuilderStates;

public class SoloTestBuilderTest {

    public final SoloTestBuilder testBuilder = new SoloTestBuilder();

    @ParameterizedTest
    @CsvSource({"DEFAULT_STATE, CATEGORY", "CATEGORY, QUESTION", "FINISH, DEFAULT_STATE"})
    public void assertNextStepChangesStatesCorrectly(BuilderStates initialState, BuilderStates afterNextStepCall) {
        testBuilder.state = initialState;
        testBuilder.nextStep("random text");
        assertEquals(afterNextStepCall, testBuilder.state);
    }
}
