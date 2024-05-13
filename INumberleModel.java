/**
 * The INumberleModel interface defines the contract for the Numberle game model.
 */
public interface INumberleModel {
    int MAX_ATTEMPTS = 7;

    /**
     * Initializes the game model.
     */
    void initialize();

    /**
     * Processes the user input.
     *
     * @param input The user input to process.
     * @return True if the input was successfully processed, false otherwise.
     */
    boolean processInput(String input);

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    boolean isGameOver();

    /**
     * Checks if the game is won.
     *
     * @return True if the game is won, false otherwise.
     */
    boolean isGameWon();

    /**
     * Retrieves the target equations for the game.
     *
     * @return The target equations.
     */
    String getTargetEquations();

    /**
     * Retrieves the current guess made by the player.
     *
     * @return The current guess.
     */
    StringBuilder getCurrentGuess();

    /**
     * Retrieves the number of remaining attempts in the game.
     *
     * @return The number of remaining attempts.
     */
    int getRemainingAttempts();

    /**
     * Starts a new game by resetting the game state.
     */
    void startNewGame();

    /**
     * Sets the target equation for the game.
     *
     * @param equation The target equation to set.
     */
    void setTargetEquation(String equation);

    /**
     * Generates a random equation based on the given input.
     *
     * @param input The input used to generate the random equation.
     * @return The randomly generated equation.
     */
    String getRandomEquation(String input);

    /**
     * Retrieves the color values of the game components.
     *
     * @return A two-dimensional array containing the color values.
     */
    int[][] getColor();

    /**
     * Validates the given equation.
     *
     * @param equation The equation to validate.
     * @return The result of the validation. Returns null if the equation is valid, otherwise returns an error code.
     */
    Integer validateEquation(String equation);

    /**
     * Compares the given equation with the target equations and updates the game state accordingly.
     *
     * @param equation         The equation to compare.
     * @param targetEquations The target equations to compare against.
     * @return True if the equation matches the target equations, false otherwise.
     */
    Boolean compare(String equation, String targetEquations);

    /**
     * Retrieves the button color values.
     *
     * @return An array containing the button color values.
     */
    int[] getButtonColor();

    /**
     * Checks if the game is currently in random mode.
     *
     * @return True if the game is in random mode, false otherwise.
     */
    boolean getRandom();

    /**
     * Toggles the random mode of the game.
     */
    void setRandom();
}