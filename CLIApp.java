import java.io.IOException;
import java.util.*;

/**
 * The CLIApp class represents a command-line interface for the Numberle game.
 */

public class CLIApp {
    public static void main(String[] args) throws IOException {

        NumberleModel model = new NumberleModel();
        Scanner scan = new Scanner(System.in);
        model.initialize();
        System.out.println("Answer is: " + model.getTargetEquations());


        while(!model.isGameOver()){
            System.out.print("Enter your guess: ");
            String currentGuess = null;
        if (scan.hasNext()) {
            currentGuess = scan.next();
            System.out.println("current guess：" + currentGuess);
        }
        // receive guess from cli
        System.out.print("e: Enter \nd: Delete 1 charter:");
        String options = scan.next();
        ArrayList<Integer> Green = new ArrayList<>();
        ArrayList<Integer> Orange = new ArrayList<>();
        ArrayList<Integer> Gray = new ArrayList<>();
        // 保存颜色信息
            switch (options) {
                case "e" -> {
                    assert currentGuess != null;
                    if (model.validateEquation(currentGuess) == 4) {
                        model.setCurrentGuess(currentGuess);
                        model.addMatrix();
                        System.out.println("Enter");
                        model.compare(currentGuess,model.getTargetEquations());
                        System.out.println(Arrays.deepToString(model.getColor()));
                        int[] flag = model.getColor()[7-model.getRemainingAttempts()];
                        for (int i = 0; i < 7; i++) {
                            if (flag[i] == 1) {
                                Green.add(i + 1);
                            } else if (flag[i] == -1) {
                                Gray.add(i + 1);
                            } else Orange.add(i + 1);
                        }
                        System.out.println("Right number: " + Green.toString() + "\n"
                                + "Wrong Number" + Gray.toString() + "\n" + "Wrong Option:" + Orange.toString());
                        model.setColor(flag);
                        model.addMatrix();
                        System.out.println(Arrays.toString(model.getMatrix()));
                        model.setRemainingAttempts();
                        System.out.println("Remain Attempts: "+ model.getRemainingAttempts());
                    }
                }
                // CG: current Guess TG: target Guess

                case "d" -> System.out.println("Delete");
                default -> System.out.println("Wrong option");
            }
    }
        if(model.isGameWon() ){
            System.out.println("GameWon");
        }
        else System.out.println("you lose");
}
}




