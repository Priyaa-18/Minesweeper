// Written by Priya Sisodia, sisod008 and Sean Swart, swart209

import java.util.Random;

public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /**
     * Class Variable Section
    */
    Cell[][] minefield; // 2-d cell array representing our minefield
    private int rows;
    private int columns;
    private int flags;  // equal to mines

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     *
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        this.rows = rows;
        this.columns = columns;
        this.flags = flags;
        minefield = new Cell[rows][columns];

        // Initializing each cell in the minefield
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = new Cell(false, "-");
            }
        }
    }

    /**
     * evaluateField
     *
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     *
     */
    public void evaluateField() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int counter = 0;    // to keep track of the number of mines

                if (!minefield[i][j].getStatus().equals("M")) {
                    for (int a = -1; a <= 1; a++) {
                        for (int b = -1; b <= 1; b++) {
                            int neighborX = a + i;
                            int neighborY = b + j;

                            // Check if the neighbor is in bounds
                            if (neighborX >= 0 && neighborX < rows && neighborY >= 0 && neighborY < columns) {
                                if (minefield[neighborX][neighborY].getStatus().equals("M")) {
                                    counter++;  // mine detected
                                }
                            }
                        }
                    }
                    minefield[i][j].setStatus(Integer.toString(counter));   // setting the current cell with a new status
                }
            }
        }
    }

    /**
     * createMines
     *
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        while (mines > 0) {
            Random rand = new Random();
            int mineX = rand.nextInt(0, rows);  // generate random coordinate [x]
            int mineY = rand.nextInt(0, columns);   // generate random coordinate [y]
            if (!minefield[x][y].getRevealed() || !minefield[x][y].getStatus().equals("M") || (mineX != x && mineY != y)) {
                minefield[mineX][mineY].setStatus("M"); // setting the cell to be a mine
                mines--;
            }
        }
    }

    /**
     * guess
     *
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        minefield[x][y].setRevealed(true);  // revealing the cell to the user
        if (flag && flags > 0) {
            minefield[x][y].setStatus("F"); // placing a flag on the designated cell
            flags--;
            if (minefield[x][y].getStatus().equals("M")) {
                return true;    // end the game if the cell has a mine
            }
        } else {
            if (minefield[x][y].getStatus().equals("0")) {
                revealZeroes(x, y);
            }
            else if (minefield[x][y].getStatus().equals("M")) {
                return true;    // end the game if the cell has a mine
            }
        }
        return false;
    }

    /**
     * gameOver
     *
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otherwise return true.
     */
    public boolean gameOver() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // game is not over and squares have yet to be revealed
                if (!minefield[i][j].getRevealed()) {
                    return false;
                }
            }
        }
        return true;    // game over
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the pseudocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<int[]> stack = new Stack1Gen<>();
        stack.push(new int[]{x, y});

        while (!stack.isEmpty()) {
            int[] currentCoordinates = stack.pop();
            int currentX = currentCoordinates[0];
            int currentY = currentCoordinates[1];

            // Check if the cell has a non-zero status
            if (minefield[currentX][currentY].getStatus().equals("0")) {
                minefield[currentX][currentY].setRevealed(true);    // Set the current cell as revealed

                // Explore neighbors
                for (int a = -1; a <= 1; a++) {
                    for (int b = -1; b <= 1; b++) {
                        int neighborX = currentX + a;
                        int neighborY = currentY + b;

                        // Check if the neighbor is in bounds
                        if (neighborX >= 0 && neighborX < rows && neighborY >= 0 && neighborY < columns) {
                            // Check if the neighbor has not been revealed
                            if (!minefield[neighborX][neighborY].getRevealed() && minefield[neighborX][neighborY].getStatus().equals("0")) {
                                // Push the neighbor's coordinates onto the stack
                                stack.push(new int[]{neighborX, neighborY});
                            }
                        }
                    }
                }   // end of nested for loop
            }
        }   // end of while loop
    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the initial cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     *
     * This method should follow the pseudocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{x, y});

        while (queue.length() > 0) {
            int[] currentCoordinates = queue.remove();
            int currentX = currentCoordinates[0];
            int currentY = currentCoordinates[1];

            minefield[currentX][currentY].setRevealed(true);    // Set the current cell as revealed

            // If the current cell is a mine, break from the loop
            if (minefield[currentX][currentY].getStatus().equals("M")) {
                break;
            }

            // Enqueue all reachable neighbors
            for (int a = -1; a <= 1; a++) {
                for (int b = -1; b <= 1; b++) {
                    int neighborX = currentX + a;
                    int neighborY = currentY + b;

                    // Check if the neighbor is in bounds
                    if (neighborX >= 0 && neighborX < rows && neighborY >= 0 && neighborY < columns) {
                        // Check if the neighbor has not been visited
                        if (!minefield[neighborX][neighborY].getRevealed()) {
                            // Enqueue the neighbor's coordinates
                            queue.add(new int[]{neighborX, neighborY});
                        }
                    }
                }
            }
        }   // end of while loop
    }

    /**
     * For both printing methods utilize the ANSI colour codes provided!
     *
     *
     *
     *
     *
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void debug() {
        System.out.print("  ");
        for (int i = 0; i < columns; i++) {
            System.out.printf("%3d", i);
        }

        System.out.println();
        for (int i = 0; i < rows; i++) {
            System.out.printf("%2d", i);
            System.out.print("  ");
            for (int j = 0; j < columns; j++) {
                String status = minefield[i][j].getStatus();

                switch (status) {
                    case "F":
                        System.out.print(ANSI_GREEN + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "M":
                        System.out.print(ANSI_RED_BRIGHT + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "0":
                        System.out.print(ANSI_YELLOW + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "1":
                        System.out.print(ANSI_BLUE + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "2":
                        System.out.print(ANSI_GREEN + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "3":
                        System.out.print(ANSI_RED + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "4":
                        System.out.print(ANSI_BLUE_BRIGHT + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "5":
                        System.out.print(ANSI_PURPLE + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "6":
                        System.out.print(ANSI_CYAN + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "7":
                        System.out.print(ANSI_WHITE_BACKGROUND + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "8":
                        System.out.print(ANSI_PURPLE_BACKGROUND + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    case "9":
                        System.out.print(ANSI_YELLOW_BRIGHT + status + "  " + ANSI_GREY_BACKGROUND);
                        break;
                    default:
                        System.out.print(ANSI_GREY_BACKGROUND + status + "  " + ANSI_GREY_BACKGROUND);
                }
            }
            System.out.println();
        }
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("  ");
        for (int i = 0; i < columns; i++) {
            str.append(String.format("%3d", i));
        }

        str.append("\n");
        for (int i = 0; i < rows; i++) {
            str.append(String.format("%2d", i));
            str.append("  ");
            for (int j = 0; j < columns; j++) {
                if (minefield[i][j].getRevealed()) {
                    String status = minefield[i][j].getStatus();

                    switch (status) {
                        case "F":
                            str.append(ANSI_GREEN).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "M":
                            str.append(ANSI_RED_BRIGHT).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "0":
                            str.append(ANSI_YELLOW).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "1":
                            str.append(ANSI_BLUE).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "2":
                            str.append(ANSI_GREEN).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "3":
                            str.append(ANSI_RED).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "4":
                            str.append(ANSI_BLUE_BRIGHT).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "5":
                            str.append(ANSI_PURPLE).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "6":
                            str.append(ANSI_CYAN).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "7":
                            str.append(ANSI_WHITE_BACKGROUND).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "8":
                            str.append(ANSI_PURPLE_BACKGROUND).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        case "9":
                            str.append(ANSI_YELLOW_BRIGHT).append(status).append(ANSI_GREY_BACKGROUND);
                            break;
                        default:
                            str.append(ANSI_GREY_BACKGROUND).append(status).append(ANSI_GREY_BACKGROUND);
                    }
                    str.append("  ");
                } else {
                    str.append("-  ");
                }
            }
            str.append("\n");
        }
        return str.toString();
    }

}   // end of Minefield class