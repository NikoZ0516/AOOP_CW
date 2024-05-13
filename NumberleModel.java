// NumberleModel.java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The NumberleModel class represents the game model for Numberle.
 * It implements the INumberleModel interface and extends the Observable class.
 */
public class NumberleModel extends Observable implements INumberleModel {

    private String targetEquations; // The target equations that the player needs to solve.
    private StringBuilder currentGuess; // The player's current guess.
    private int remainingAttempts; // The number of remaining attempts.
    private boolean gameWon; // Indicates whether the game has been won.
    private StringBuilder[] matrix = new StringBuilder[7]; // The matrix representing the game board.
    private final int[][] color = new int[7][7]; // The color values for each cell in the matrix.

    private final int[] buttonColor = new int[15]; // The color values for the buttons.
    private boolean isRandom = false; // Indicates whether the game is in random mode.
    final String defaultEquation = "1+3=1+3"; // The default equation for the game.




    @Override
    /**
     * Initializes the game by setting up the initial state.
     * It generates a random target equation, resets the current guess, remaining attempts,
     * button colors, and game won status. It notifies the observers of the model changes.
     */
    public void initialize() {
        targetEquations = getRandomEquation("E:\\大学\\大四\\大四下\\AOOP\\AOOP_CW\\equations.txt"); // Generate a random target equation
        currentGuess = new StringBuilder("       "); // Reset the current guess
        remainingAttempts = MAX_ATTEMPTS; // Reset the remaining attempts
        Arrays.fill(buttonColor, -2); // Reset the button colors
        gameWon = false; // Reset the game won status

        setChanged(); // Set the model as changed
        notifyObservers(); // Notify the observers of the model changes
    }

    @Override
    /**
     * Processes the user input and updates the game state accordingly.
     * It decrements the remaining attempts, marks the model as changed,
     * notifies the observers, and returns true.
     *
     * @param input The user input to be processed.
     * @return True indicating that the input was processed successfully.
     */
    public boolean processInput(String input) {
        remainingAttempts--; // Decrement the remaining attempts
        setChanged(); // Mark the model as changed
        notifyObservers(); // Notify the observers of the model changes
        return true; // Return true indicating successful processing of input
    }
    /**
     * Decrements the remaining attempts by 1.
     */
    public void setRemainingAttempts() {
        this.remainingAttempts--;
    }

    /**
     * Checks if the game is over.
     * @return True if the remaining attempts are less than or equal to 0 or the game has been won, false otherwise.
     */
    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    /**
     * Checks if the game has been won.
     * @return True if the game has been won, false otherwise.
     */
    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Sets the game won status.
     * @param gameWon The game won status to be set.
     */
    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    /**
     * Returns the target equations.
     * @return The target equations.
     */
    @Override
    public String getTargetEquations() {
        return targetEquations;
    }

    /**
     * Sets the target equation.
     * @param equation The target equation to be set.
     */
    @Override
    public void setTargetEquation(String equation) {
        this.targetEquations = equation;
    }

    /**
     * Returns the current guess.
     * @return The current guess.
     */
    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    /**
     * Sets the current guess.
     * @param currentGuess The current guess to be set.
     */
    public void setCurrentGuess(String currentGuess) {
        this.currentGuess = new StringBuilder(currentGuess);
        addMatrix();
    }

    /**
     * Returns the remaining attempts.
     * @return The remaining attempts.
     */
    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    /**
     * Adds the current guess to the matrix.
     */
    public void addMatrix() {
        matrix[7 - getRemainingAttempts()] = getCurrentGuess();
    }

    /**
     * Sets the color for a specific row in the matrix.
     * @param flag The color values to be set for the row.
     */
    public void setColor(int[] flag) {
        color[7 - getRemainingAttempts()] = flag;
    }

    /**
     * Returns the color matrix.
     * @return The color matrix.
     */
    public int[][] getColor() {
        return color;
    }

    /**
     * Returns the matrix.
     * @return The matrix.
     */
    public StringBuilder[] getMatrix() {
        return matrix;
    }

    /**
     * Generates a random equation from a file.
     * @param fileName The file name containing the equations.
     * @return A random equation.
     */
    public String getRandomEquation(String fileName) {
        if (isRandom) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();
                Random random = new Random();
                int index = random.nextInt(lines.size());
                String randomLine = lines.get(index);
                return randomLine;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return defaultEquation;
        }
    }

    /**
     * Validates an equation.
     * @param equation The equation to be validated.
     * @return An integer representing the validation result:
     *         0 - Illegal equation
     *         1 - Equation must contain equal sign and have digits at the beginning and end
     *         2 - Equation must have 7 characters
     *         3 - The left side is not equal to the right side
     *         4 - Equation is valid
     */
    public Integer validateEquation(String equation) {
        equation = equation.replaceAll("\\s+", "");
        if (equation.length() != 7) {
            System.out.println("Too short");
            return 2; // Equation must have 7 characters
        }

        if (!equation.contains("=") || !Character.isDigit(equation.charAt(0)) || !Character.isDigit(equation.charAt(equation.length() - 1))) {
            System.out.println("No equal");
            return 1; // Equation must contain equal sign and have digits at the beginning and end
        }

        for (int i = 0; i < 6; i++) {
            if (!Character.isDigit(equation.charAt(i)) && !Character.isDigit(equation.charAt(i + 1))) {
                System.out.println("Not valid");
                return 0; // Illegal equation
            }
        }

        String[] tokens = equation.replaceAll("\\s+", "").split("=");
        if (!calculate(tokens[0]).equals(calculate(tokens[1]))) {
            System.out.println("The left side is not equal to the right side");
            return 3; // The left side is not equal to the right side
        }

        return 4; // Equation is valid
    }

    /**
     * Evaluates a mathematical expression and returns the result as an integer.
     *
     * @param s The mathematical expression to be evaluated.
     * @return The result of the evaluation as an integer.
     */
    private Integer calculate(String s) {
        List<String> ls1 = new ArrayList<>();
        int i = 0;
        String str = "";
        char c;

        // Tokenize the expression into a list of numbers and operators
        do {
            if ((c = s.charAt(i)) < 48 || (c = s.charAt(i)) > 57) {
                ls1.add(c + "");
                i++;
            } else {
                str = "";
                while (i < s.length() && (c = s.charAt(i)) >= 48 && (c = s.charAt(i)) <= 57) {
                    str += c;
                    i++;
                }
                ls1.add(str);
            }
        } while (i < s.length());

        Stack<String> stack = new Stack<>();
        List<String> ls2 = new ArrayList<>();

        // Convert the expression from infix to postfix notation
        for (String item : ls1) {
            if (item.matches("\\d+")) {
                ls2.add(item);
            } else if (item.equals("(")) {
                stack.push(item);
            } else if (item.equals(")")) {
                while (!stack.peek().equals("(")) {
                    ls2.add(stack.pop());
                }
                stack.pop();
            } else {
                if (!stack.isEmpty() && opers(stack.peek()) >= opers(item)) {
                    ls2.add(stack.pop());
                }
                stack.push(item);
            }
        }

        while (!stack.isEmpty()) {
            ls2.add(stack.pop());
        }

        Stack<String> st1 = new Stack<>();

        // Evaluate the expression using the postfix notation
        for (String item : ls2) {
            if (item.matches("\\d+")) {
                st1.push(item);
            } else {
                int num2 = Integer.parseInt(st1.pop());
                int num1 = Integer.parseInt(st1.pop());
                int res = calculateValue(num1, num2, item);
                st1.push(String.valueOf(res));
            }
        }

        // Check if the expression is valid and return the result
        if (st1.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }

        return Integer.parseInt(st1.pop());
    }

    /**
     * Assigns precedence values to operators.
     *
     * @param s The operator as a string.
     * @return The precedence value of the operator.
     */
    private int opers(String s) {
        int value = 0;
        switch (s) {
            case "+":
            case "-":
                value = 1;
                break;
            case "*":
            case "/":
                value = 2;
                break;
            default:
                break;
        }

        return value;
    }

    /**
     * Performs arithmetic operations on two numbers based on the given operator.
     *
     * @param num1 The first number.
     * @param num2 The second number.
     * @param oper The operator as a string.
     * @return The result of the arithmetic operation.
     */
    private int calculateValue(int num1, int num2, String oper) {
        int res = 0;
        switch (oper) {
            case "+":
                res = num1 + num2;
                break;
            case "-":
                res = num1 - num2;
                break;
            case "*":
                res = num1 * num2;
                break;
            case "/":
                res = num1 / num2;
                break;
            default:
                break;
        }
        return res;
    }

    /**
     * Compares two strings character by character and performs actions based on the comparison.
     *
     * @param CG The first string to compare.
     * @param TG The second string to compare.
     * @return True if the strings are equal, false otherwise.
     */
    public Boolean compare(String CG, String TG) {
        int[] flag = new int[CG.length()]; // Flag array
        int buttonIndex = -1; // Button index

        for (int i = 0; i < CG.length(); i++) {
            char currentChar = CG.charAt(i);

            if (Character.isDigit(currentChar)) {
                buttonIndex = Character.getNumericValue(currentChar);
            } else {
                switch (currentChar) {
                    case '+':
                        buttonIndex = 10;
                        break;
                    case '-':
                        buttonIndex = 11;
                        break;
                    case '*':
                        buttonIndex = 12;
                        break;
                    case '/':
                        buttonIndex = 13;
                        break;
                    case '=':
                        buttonIndex = 14;
                        break;
                    default:
                        System.out.println("not valid char");
                }
            }


            if (TG.charAt(i) == currentChar) {
                flag[i] = 1; // Position is correct, set flag to 1
                setButtonColor(buttonIndex, 1);

            } else if (!TG.contains(String.valueOf(CG.charAt(i)))) {
                flag[i] = -1; // Position does not exist, set flag to -1
                setButtonColor(buttonIndex, -1);

            } else {

                flag[i] = 0; // Position is incorrect, set flag to 0
                setButtonColor(buttonIndex, 0);
            }
        }

        setColor(flag);
        boolean isWin = Arrays.equals(flag, new int[]{1, 1, 1, 1, 1, 1, 1});
        setGameWon(isWin);
        return isWin;
    }


    /**
     * Sets the color of a button at the given index.
     *
     * @param index The index of the button.
     * @param value The color value to set.
     */
    private void setButtonColor(int index, int value) {
        if (value >= buttonColor[index]) {
            buttonColor[index] = value;
        }
    }

    /**
     * Retrieves the color values of the buttons.
     *
     * @return An array containing the color values of the buttons.
     */
    public int[] getButtonColor() {
        return buttonColor;
    }

    /**
     * Toggles the random flag.
     * If the flag is currently true, it will be set to false. If it is false, it will be set to true.
     */
    public void setRandom() {
        isRandom = !isRandom;
    }

    /**
     * Retrieves the value of the random flag.
     *
     * @return True if the random flag is set, false otherwise.
     */
    public boolean getRandom() {
        return isRandom;
    }

    /**
     * Starts a new game by initializing the necessary components.
     */
    @Override
    public void startNewGame() {
        initialize();
    }
}




