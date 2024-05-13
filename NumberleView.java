// NumberleView.java
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Observer;
import javax.swing.border.LineBorder;
import java.awt.geom.RoundRectangle2D;

public class NumberleView implements Observer {
    /**
     * Constructor for creating a new instance.
     *
     * @param model       The INumberleModel model object
     * @param controller  The NumberleController controller object
     */
    private final INumberleModel model;
    private final NumberleController controller;
    private final JFrame frame;
    private ArrayList<String> localGuess;
    private JPanel chessBoard;
    private message messages;
    private ArrayList<JButton> buttonList = new ArrayList<JButton>();

    /**
     * Constructor for creating a new instance of NumberleView.
     *
     * @param model      The INumberleModel model object
     * @param controller The NumberleController controller object
     */
    public NumberleView(INumberleModel model, NumberleController controller) {
        this.frame = new JFrame("Numberle");
        this.controller = controller;
        this.model = model;
        this.controller.startNewGame();
        this.localGuess = new ArrayList<>();
        ((NumberleModel) this.model).addObserver(this);
        chessBoard = createChessBoardPanel();
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel) this.model, null);
        messages = new message();
    }

    /**
     * Initializes the frame by setting up the layout and adding components.
     */
    public void initializeFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation of the frame
        frame.setSize(1000, 1000); // Set the size of the frame to 1000x1000 pixels
        frame.setLayout(new BorderLayout()); // Set the BorderLayout as the layout manager for the frame

        JPanel functionPanel = new JPanel(); // Create a new JPanel for the function buttons
        functionPanel.setLayout(new FlowLayout()); // Set the FlowLayout as the layout manager for the function panel
        functionPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Set an empty border with 20 pixels top margin

        // Create the "Restart" button
        JButton restartButton = createButton("Restart", 200, 30);

        // Create button1
        JButton button1 = createButton("Answer", 200, 30);
        functionPanel.add(button1);

        // Create button2
        JButton button2 = createButton("Random", 200, 30);
        functionPanel.add(button2);

        // Create button3
        JButton button3 = createButton("Wrong Message", 200, 30);
        functionPanel.add(button3);

        functionPanel.add(restartButton); // Add the restart button to the function panel
        frame.add(functionPanel, BorderLayout.SOUTH); // Add the function panel to the south region of the frame

        JPanel center = new JPanel(); // Create a new JPanel for the center region
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS)); // Set the BoxLayout with horizontal axis for the center panel
        center.setBorder(new EmptyBorder(100, 0, 0, 0)); // Set an empty border with 100 pixels top margin
        center.add(new JPanel());

        JPanel chessBoardPanel = this.chessBoard; // Get the chessboard panel
        center.add(chessBoardPanel); // Add the chessboard panel to the center panel
        center.add(new JPanel());
        frame.add(center, BorderLayout.NORTH); // Add the center panel to the north region of the frame

        JPanel buttonPanel = new JPanel(); // Create a new JPanel for the button panel
        buttonPanel.setLayout(new GridBagLayout()); // Set the GridBagLayout as the layout manager for the button panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5); // Set the insets to create spacing between buttons

        // Add number buttons (0-9)
        for (int i = 0; i <= 9; i++) {
            JButton numberButton = createButton(Integer.toString(i), 80, 80); // Create a number button
            buttonList.add(numberButton); // Add the button to the button list
            numberButton.setBorder(createCustomBorder()); // Set a custom border for the button
            gbc.gridx = i;
            gbc.gridy = 0;
            buttonPanel.add(numberButton, gbc); // Add the button to the button panel
        }

        // Add the "Delete" button
        JButton deleteButton = createButton("Delete", 80, 80);
        deleteButton.setBorder(createCustomBorder());
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        buttonPanel.add(deleteButton, gbc);

        // Add "+-x/" buttons
        String[] operators = {"+", "-", "x", "/", "="};
        for (int i = 0; i < operators.length; i++) {
            JButton operatorButton = createButton(operators[i], 80, 80);
            buttonList.add(operatorButton);
            operatorButton.setBorder(createCustomBorder());
            gbc.gridx = i + 2;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            buttonPanel.add(operatorButton, gbc);
        }

        // Add the "Enter" button
        JButton enterButton = createButton("Enter", 160, 80);
        enterButton.setBorder(createCustomBorder());
        gbc.gridx = 7;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        buttonPanel.add(enterButton, gbc);

        frame.add(buttonPanel, BorderLayout.CENTER); // Add the button panel to the center region of the frame
        frame.setVisible(true); // Set the frame as visible
    }

    /**
     * Creates and returns a JPanel representing the chessboard.
     *
     * @return The JPanel representing the chessboard.
     */
    private JPanel createChessBoardPanel() {
        JPanel chessBoardPanel = new JPanel(); // Create a new JPanel for the chessboard
        chessBoardPanel.setLayout(new GridLayout(7, 7)); // Set the GridLayout with 7 rows and 7 columns for the chessboard panel
        chessBoardPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Set an empty border with 20 pixels padding for the chessboard panel

        int cellPadding = 10; // Spacing between each cell

        // Iterate through each row and column to create the cells
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                JPanel cellPanel = new JPanel(); // Create a new JPanel for each cell
                cellPanel.setPreferredSize(new Dimension(80, 80)); // Set the preferred size of each cell to 80x80 pixels
                cellPanel.setBorder(new EmptyBorder(cellPadding, cellPadding, cellPadding, cellPadding)); // Set the empty border with cellPadding for each cell

                // Custom border with rounded corners
                cellPanel.setBorder(new LineBorder(Color.BLACK) {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Shape borderShape = new RoundRectangle2D.Double(x, y, width - 1, height - 1, 20, 20); // Rounded rectangle shape
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.setColor(getLineColor());
                        g2d.draw(borderShape); // Draw the border shape
                        g2d.dispose();
                    }
                });

                cellPanel.setBackground(Color.WHITE); // Set the background color of each cell to white
                chessBoardPanel.add(cellPanel); // Add the cell panel to the chessboard panel
            }
        }

        return chessBoardPanel; // Return the chessboard panel
    }

    /**
     * Creates and returns a JButton with the specified text, width, and height.
     *
     * @param text   The text to display on the button.
     * @param width  The width of the button.
     * @param height The height of the button.
     * @return The JButton with the specified properties.
     */
    private JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text); // Create a new JButton with the specified text
        button.setPreferredSize(new Dimension(width, height)); // Set the preferred size of the button to the specified width and height
        button.setBackground(Color.LIGHT_GRAY); // Set the background color of the button to light gray

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action performed when the button is clicked
                String buttonText = button.getText(); // Get the text of the button

                // Check the text of the button and perform corresponding operations
                if (Objects.equals(buttonText, "Enter")) {
                    Enter(); // Call the Enter() method
                } else if (Objects.equals(buttonText, "Delete")) {
                    Delete(); // Call the Delete() method
                } else if (Objects.equals(buttonText, "Restart")) {
                    if (model.getRemainingAttempts() <= 6) {
                        restart(); // Call the restart() method
                        controller.startNewGame(); // Start a new game by calling the controller's startNewGame() method
                    }
                } else if (Objects.equals(buttonText, "Answer")) {
                    showPopup(controller.getTargetEquation()); // Show a popup with the target equation by calling the showPopup() method
                } else if (Objects.equals(buttonText, "Random")) {
                    if (controller.getIsRandom()) {
                        controller.setIsRandom(); // Turn off random mode by calling the setIsRandom() method
                        showPopup("Random off, Please restart game"); // Show a popup indicating random mode is off
                    } else {
                        showPopup("Random On, Please restart game"); // Show a popup indicating random mode is on
                        controller.setIsRandom(); // Turn on random mode by calling the setIsRandom() method
                    }
                } else if (Objects.equals(buttonText, "Wrong Message")) {
                    // Implement the logic for the "Wrong Message" button
                } else {
                    DisplayCharOnBoard(buttonText); // Display the button text on the board
                }
            }
        });

        return button; // Return the created button
    }


    /**
     * Restarts the game by clearing the chessboard and resetting other variables.
     */
    private void restart() {
        // Clear the text fields in the chessboard
        for (Component component : chessBoard.getComponents()) {
            if (component instanceof JPanel) {
                JPanel cellPanel = (JPanel) component;
                cellPanel.setBackground(Color.WHITE); // Set the background color of the cell panel to white
                cellPanel.removeAll(); // Remove all components from the cell panel
                cellPanel.revalidate(); // Revalidate the cell panel to update its layout
                cellPanel.repaint(); // Repaint the cell panel to reflect the changes
            }
        }

        // Clear the localGuess list
        localGuess.clear();

        // Reset the background color of the buttons
        for (JButton button : buttonList) {
            button.setBackground(Color.lightGray);
        }
    }


    /**
     * Creates and returns a custom Border with rounded corners.
     *
     * @return The custom Border with rounded corners.
     */
    private Border createCustomBorder() {
        return new LineBorder(Color.BLACK) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape borderShape = new RoundRectangle2D.Double(x, y, width - 1, height - 1, 20, 20); // Rounded rectangle shape
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(getLineColor());
                g2d.draw(borderShape); // Draw the border shape
                g2d.dispose();
            }
        };
    }


    /**
     * Displays a character on the chessboard.
     *
     * @param Char The character to display.
     */
    private void DisplayCharOnBoard(String Char) {
        if (localGuess.size() < 7) { // Check if the localGuess list has less than 7 elements

            // Get the cell panel to display the character
            JPanel cellPanel = (JPanel) chessBoard.getComponent((7 - controller.getRemainingAttempts()) * 7 + localGuess.size());

            // Create a label with the character
            JLabel label = new JLabel(Char);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);

            cellPanel.removeAll(); // Remove any existing components from the cell panel
            cellPanel.add(label); // Add the label to the cell panel
            cellPanel.revalidate(); // Revalidate the cell panel to update its layout
            cellPanel.repaint(); // Repaint the cell panel to reflect the changes

            localGuess.add(Char); // Add the character to the localGuess list
        }
    }


    /**
     * Deletes the last character displayed on the chessboard.
     */
    private void Delete() {
        if (localGuess.size() > 0) { // Check if the localGuess list has at least one character
            // Get the cell panel where the last character is displayed
            JPanel cellPanel = (JPanel) chessBoard.getComponent(localGuess.size() - 1 + 7 * (7 - controller.getRemainingAttempts()));

            // Remove all components from the cell panel
            cellPanel.removeAll();

            // Revalidate and repaint the cell panel to update its layout
            cellPanel.revalidate();
            cellPanel.repaint();

            // Remove the last character from the localGuess list
            localGuess.remove(localGuess.size() - 1);
        }
    }


    /**
     * Handles the logic when the user presses the Enter key.
     */
    private void Enter() {
        StringBuilder sb = new StringBuilder();
        for (String element : localGuess) {
            sb.append(element);
        }


        // Validate the equation and get the error number
        int errorNumber = controller.validateEquation(sb.toString());

        if (errorNumber != 4) { // Error occurred in the equation
            // Show a popup with the corresponding error message
            showPopup(messages.getMessages()[errorNumber]);
        } else { // Equation is valid
            StringBuilder temp = new StringBuilder();
            localGuess.forEach(temp::append);

            // Compare the equation with the target and process the input
            controller.compare(temp.toString());
            controller.processInput(sb.toString());

            // Clear the localGuess list
            localGuess.clear();

            if (controller.isGameWon()) { // Game is won
                showPopup("YOU WON!");
            } else if (controller.isGameOver()) { // Game is lost
                showPopup("YOU LOSE");
            } else { // Game is not over yet
                // Show a popup with the remaining attempts
                showPopup("Attempts remaining: " + controller.getRemainingAttempts());
            }
        }
    }


    /**
     * Displays a popup message dialog with the specified content.
     *
     * @param content The content of the popup message.
     */
    public void showPopup(String content) {
        JOptionPane.showMessageDialog(null, content, "message", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Sets the background color of a JPanel based on the specified flag.
     *
     * @param jPanel The JPanel to set the background color.
     * @param flag   The flag indicating the color.
     */
    public void setColor(JPanel jPanel, int flag) {
        switch (flag) {
            case -1:
                jPanel.setBackground(Color.decode("#A4AEC4")); // Set the background color to gray
                break;
            case 1:
                jPanel.setBackground(Color.decode("#2FCEA5")); // Set the background color to green
                break;
            case 0:
                jPanel.setBackground(Color.decode("#F79A6F")); // Set the background color to yellow
                break;
        }
    }
    /**
     * Sets the background color of buttons in the button list based on the button color values from the controller.
     */
    public void setButtonColor() {

        int[] buttonColorList = controller.getButtonColor();

        for (int i = 0; i < buttonList.size(); i++) {
            switch (buttonColorList[i]) {
                case -1:
                    buttonList.get(i).setBackground(Color.decode("#A4AEC4")); // Set the background color to gray
                    break;
                case 0:
                    buttonList.get(i).setBackground(Color.decode("#F79A6F")); // Set the background color to yellow
                    break;
                case 1:
                    buttonList.get(i).setBackground(Color.decode("#2FCEA5")); // Set the background color to green
                    break;
                default:
                    // Do nothing for other cases
                    break;
            }
        }
    }

    @Override
    /**
     * Updates the UI based on changes in the Observable object.
     *
     * @param o   The Observable object.
     * @param arg An optional argument passed to the update method.
     */
    public void update(java.util.Observable o, Object arg) {
        if (controller.getRemainingAttempts() != 7) {
            int[] colors = controller.getColor()[6 - controller.getRemainingAttempts()];
            System.out.println("correct:" + controller.getTargetEquation());

            for (int i = 0; i < 7; i++) {
                JPanel cellPanel = (JPanel) chessBoard.getComponent((6 - controller.getRemainingAttempts()) * 7 + i);
                setColor(cellPanel, colors[i]);
            }

            setButtonColor();
        }
    }
    /**
     * The message class represents a collection of predefined messages used in the application.
     */
    public static class message {
        private String[] messages = new String[10];

        /**
         * Constructs a Message object and initializes the predefined messages.
         */
        public message() {
            messages[0] = "Illegal Equation";
            messages[1] = "Equation must contain an equal sign";
            messages[2] = "Equation must have 7 characters";
            messages[3] = "The left side is not equal to the right side";
        }

        /**
         * Retrieves the array of predefined messages.
         *
         * @return An array containing the predefined messages.
         */
        public String[] getMessages() {
            return messages;
        }
    }
}