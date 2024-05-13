/**
 * The controller class for the Numberle game, responsible for coordinating interactions between the model and the view.
 */
public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    /**
     * Constructs a NumberleController with the specified model.
     *
     * @param model The model to associate with the controller.
     */
    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    /**
     * Sets the view for the controller.
     *
     * @param view The view to set.
     */
    public void setView(NumberleView view) {
        this.view = view;
    }

    /**
     * Processes the user input by passing it to the model for further processing.
     *
     * @param input The user input to process.
     */
    public void processInput(String input) {
        model.processInput(input);
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return model.isGameOver();
    }

    /**
     * Checks if the game is won.
     *
     * @return True if the game is won, false otherwise.
     */
    public boolean isGameWon() {
        return model.isGameWon();
    }

    /**
     * Retrieves the target equation for the game.
     *
     * @return The target equation.
     */
    public String getTargetEquation() {
        return model.getTargetEquations();
    }

    /**
     * Retrieves the current guess made by the player.
     *
     * @return The current guess.
     */
    public StringBuilder getCurrentGuess() {
        return model.getCurrentGuess();
    }

    /**
     * Retrieves the color values of the game components.
     *
     * @return A two-dimensional array containing the color values.
     */
    public int[][] getColor() {
        return model.getColor();
    }

    /**
     * Validates the given equation.
     *
     * @param equation The equation to validate.
     * @return The result of the validation. Returns null if the equation is valid, otherwise returns an error code.
     */
    public Integer validateEquation(String equation) {
        return model.validateEquation(equation);
    }

    /**
     * Compares the given equation with the target equation and updates the game state accordingly.
     *
     * @param equation The equation to compare.
     */
    public void compare(String equation) {
        model.compare(equation, model.getTargetEquations());
    }

    /**
     * Retrieves the button color values.
     *
     * @return An array containing the button color values.
     */
    public int[] getButtonColor() {
        return model.getButtonColor();
    }

    /**
     * Checks if the game is currently in random mode.
     *
     * @return True if the game is in random mode, false otherwise.
     */
    public boolean getIsRandom() {
        return model.getRandom();
    }

    /**
     * Toggles the random mode of the game.
     */
    public void setIsRandom() {
        model.setRandom();
    }

    /**
     * Retrieves the remaining attempts in the game.
     *
     * @return The number of remaining attempts.
     */
    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }

    /**
     * Starts a new game by initializing the model.
     */
    public void startNewGame() {
        model.startNewGame();
    }
}