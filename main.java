// Written by Priya Sisodia, sisod008 and Sean Swart, swart209

// Import Section
import java.util.Scanner;

/*
 * Provided in this class is the necessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 *
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */

public class main {

    public static void main(String[] args) {

        int rows, columns, flags;
        boolean debugMode, guess = false;

        Scanner in = new Scanner(System.in);
        System.out.println("Choose the difficulty level:");
        System.out.println("– Easy: Rows: 5, Columns: 5, Mines: 5, Flags: 5\n" +
                "– Medium: Rows: 9, Columns: 9, Mines: 12, Flags: 12\n" +
                "– Hard: Rows: 20, Columns: 20, Mines: 40, Flags: 40");
        String level = in.nextLine().toLowerCase();

        System.out.println("Choose the debug mode: 0 for OFF and 1 for ON");
        int debug = Integer.parseInt(in.nextLine());

        if (level.equals("easy")) {
            rows = 5;
            columns = 5;
            flags = 5;
        } else if (level.equals("medium")) {
            rows = 9;
            columns = 9;
            flags = 12;
        } else {
            rows = 20;
            columns = 20;
            flags = 40;
        }

        Minefield minefield = new Minefield(rows, columns, flags);

        if (debug == 0) {
            debugMode = false;
        } else {
            debugMode = true;
        }

        System.out.println("Enter starting coordinates: [x] [y]");
        String[] coordinates = in.nextLine().split(" ");

        int firstX = Integer.parseInt(coordinates[0]);  // to store the x-coordinate [x]
        int firstY = Integer.parseInt(coordinates[1]);  // to store the y-coordinate [y]

        minefield.createMines(firstX, firstY, flags);   // to place mines on an empty field

        minefield.evaluateField();  // change the statuses of each cell to reflect how many mines surround it

        minefield.revealStartingArea(firstX, firstY);   // reveal the cells

        System.out.println(minefield);

        while (!minefield.gameOver()) {
            System.out.println("Enter a coordinate and if you wish to place a flag (Remaining: " + flags +
                    "): [x] [y] ['f' to flag (-1, else)]");
            String[] input = in.nextLine().split(" ");

            int guessX = Integer.parseInt(input[0]);    // to store the x-coordinate [x]
            int guessY = Integer.parseInt(input[1]);    // to store the y-coordinate [y]

            // to check whether coordinates are in bounds
            if (guessX < 0 || guessX > rows - 1 || guessY < 0 || guessY > rows - 1) {
                System.out.println("Coordinates out-of bounds. Try again!");
                System.out.println();
                continue;
            }

            if (input[2].equals("f")) {
                guess = minefield.guess(guessX, guessY, true);
                flags--;

                if (flags <= 0) {
                    flags = 0;
                }
            } else {
                guess = minefield.guess(guessX, guessY, false);
            }

            // Player lost the game
            if (guess) {
                minefield.debug();
                System.out.println("Oh no, you hit a mine!");
                System.out.println("You lost the game!");
                break;
            }

            if (debugMode) {
                minefield.debug();
                System.out.println();
                System.out.println(minefield);
            } else {
                System.out.println(minefield);
            }
        }

        // Player won the game
        if (!guess) {
            System.out.println("----Congratulations----");
            System.out.println("Yay, you won the game!");
        }
    }

}   // end of Main class