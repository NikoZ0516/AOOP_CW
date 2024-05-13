import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the NumberleModel class.
 * This class contains tests for the initialization, compare, and validateEquation methods of the NumberleModel class.
 */
public class ModelTest {

    private NumberleModel numberleModel;

    /**
     * Sets up the NumberleModel instance before each test.
     */
    @Before
    public void setUp() {
        numberleModel = new NumberleModel();
        numberleModel.initialize();
    }

    /**
     * Tests the initialization method of NumberleModel.
     *
     * This test verifies if the initialization method correctly sets the initial state of the model.
     * It uses assertions to validate the correctness of the initial state.
     *
     * @pre The NumberleModel instance is created.
     * @post The target equations are set to the default equation ("1+3=1+3").
     *       The current guess is an empty string.
     *       The remaining attempts are set to the maximum attempts defined in the model.
     *       The game won flag is false.
     */
    @Test
    public void testInitialize() {
        assertEquals("1+3=1+3", numberleModel.getTargetEquations());
        assertEquals("       ", numberleModel.getCurrentGuess().toString());
        assertEquals(NumberleModel.MAX_ATTEMPTS, numberleModel.getRemainingAttempts());
        assertFalse(numberleModel.isGameWon());
    }

    /**
     * Tests the compare method of NumberleModel.
     *
     * This test checks the functionality of the compare method.
     * It uses assertions to validate the expected results of the compare method.
     *
     * @pre The NumberleModel instance is created.
     * @post The compare method correctly compares the provided equations and returns the expected result.
     */
    @Test
    public void testCompare() {
        // Equal equations
        assertTrue(numberleModel.compare("1+3=1+3", "1+3=1+3"));

        // Not equal equations
        assertFalse(numberleModel.compare("3+1=3+1", "1+3=1+3"));
        assertFalse(numberleModel.compare("2+4=6+0", "1+3=1+3"));
        assertFalse(numberleModel.compare("7/1=3+4", "1+3=1+3"));
    }

    /**
     * Tests the validateEquation method of NumberleModel.
 evaluates the functionality of the validateEquation method.
     * It uses assertions to validate the expected results returned by the validateEquation method.
     *
     * @pre The NumberleModel instance is created.
     * @post The validateEquation method correctly validates the provided equation and returns the expected result.
     */
    @Test
    public void testValidateEquation() {
        // Valid equations
        assertEquals(Integer.valueOf(4), numberleModel.validateEquation("1+3=1+3"));
        assertEquals(Integer.valueOf(4), numberleModel.validateEquation("2+4=6-0"));
        assertEquals(Integer.valueOf(4), numberleModel.validateEquation("7/1=2+5"));

        // Invalid equations
        assertEquals(Integer.valueOf(0), numberleModel.validateEquation("1+3+=49"));
        assertEquals(Integer.valueOf(1), numberleModel.validateEquation("2+46+12"));
        assertEquals(Integer.valueOf(2), numberleModel.validateEquation("7*8=56"));
        assertEquals(Integer.valueOf(3), numberleModel.validateEquation("1+2=3+4"));
    }


}