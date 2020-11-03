package puzzle;

/**
 * This package is comprised of two classes.
 * NPuzzleV5 is the main class, and Tile is a helper class.
 *
 * @since 2.0
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Integer.valueOf;
import static java.lang.Math.sqrt;

/**
 * This is a simple n-puzzle game, just compile and run. Instructions print on
 * screen. Private methods of NPuzzleV5 class are documented for personal
 * reference.
 * 
 * @author A.M.S., 2020
 * @version 5.0
 */
public class NPuzzleV5 {
    /**
     * main method, doesn't accept any parameters
     * 
     * @param foo N/A
     * @see #puzzle(int)
     */
    public static void main(String[] foo) {
        boolean puzzleExec = false;
        int size = 0;
        Scanner scan = new Scanner(System.in);

        System.out.println("What size puzzle (n * n) would you like to solve?");
        System.out.print("Enter an integer n>1: ");
        
        while(!puzzleExec){
            try {
                size = scan.nextInt();
                if (size > 1){
                    puzzle(size);
                    puzzleExec = true;
                } else {
                    System.out.print("Invalid input! Enter an integer n>1: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input! Enter an integer n>1: ");
                scan.next();
            }
        }
        scan.close();
    }

    /**
     * method that generates an n*n puzzle and answer key, then starts the game
     * @param num   the int that controls the generated square puzzle's dimensions
     * @see #runGame(ArrayList, ArrayList, int[][], int[][], int)
     */
    private static void puzzle(int num) {

        final int DIM = valueOf(num);

        // Generates a puzzle and a solved version to check it against.
        ArrayList<puzzle.Tile> tiles = generate(DIM);
        ArrayList<puzzle.Tile> ansKey = getKey(DIM);

        int[][] board = new int[DIM][DIM];
        int[][] key = new int[DIM][DIM];

        update(board, tiles);
        update(key, ansKey);

        // Instructions
        System.out.println("Welcome to the " + (DIM * DIM - 1) + " Puzzler! \n\n RULES: \n 1) Move the numbered tiles on the board \n\t until they are in order from 1-" + (DIM * DIM - 1) + ". \n\t It will look like this: ");
        printBoard(key);
        System.out.println(" 2) You can only move tiles into an \n\t empty space. You cannot move \n\t diagonally. \n\n 3) Input the number of the tile you \n\t wish to move and press Enter. \n");
        System.out.println(" \n Other commands: \n\t help - show these rules again \n");
        printBoard(board);

        runGame(tiles, ansKey, board, key, DIM * DIM - 1);

        // Display random celebration comment after game is won
        System.out.println(celebrate().get(0));

    }

    /**
     * Creates n*n - 1 Tile objects for the puzzle (see Tile class). If the given combination of coordinates makes an unsolvable puzzle, a new set is generated.
     * @param n the int which represents the dimensions of the puzzle generated, and controls the iterations of Tile creation; passed down from puzzle()
     * @return  an ArrayList of Tiles which will store the data for each Tile object in the puzzle
     * @see #chkInversions(ArrayList, int)
     */
    private static ArrayList<puzzle.Tile> generate(int n) {
        ArrayList<int[]> possib = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[] coord = new int[2];
                coord[0] = i;
                coord[1] = j;
                possib.add(coord);
            }
        }
        ArrayList<puzzle.Tile> tiles;
        do {
            Collections.shuffle(possib);
            tiles = new ArrayList<>();
            for (int z = 0; z < n * n - 1; z++) {
                tiles.add(new puzzle.Tile(z + 1, n, (possib.get(z))));
            }
        } while (!chkInversions(tiles, n));
        return tiles;
    }

    /**
     * method that determines if the generated puzzle is solvable by calculating inversions
     * @param tiles the ArrayList of Tile objects being checked for solvability
     * @param n int used to determine if number of rows is even or odd
     * @return true if number of inversions in puzzle makes it solvable
     */
    private static boolean chkInversions(ArrayList<puzzle.Tile> tiles, int n) {
        // If matrix width is odd
        if (n % 2 != 0) {
            return inversionCount(tiles) % 2 == 0;
        } 
        // If matrix width is even
        else {
            // Populate the set of all possible square that can hold a Tile
            ArrayList<Integer> allSquares = new ArrayList<>();
            for (int i = 1; i <= (n * n); i++){
                    allSquares.add((Integer) i);
                }

            // Populate the set of squares that actually hold a Tile
            ArrayList<Integer> realSquares = new ArrayList<>();
            for (puzzle.Tile t : tiles) {
                int num = t.getSqNum();
                realSquares.add((Integer) num);
            }
            
            // Find the empty square by removing the overlap between realSquares and allSquares
            int emptySqNum = 0;
            allSquares.removeAll(realSquares);
            emptySqNum = allSquares.get(0);

            // Find the x coordinate of the empty square to determine the first condition for solvability
            int emptyX = 0;
            if (emptySqNum % n != 0){
                emptyX = emptySqNum/n;
            } else {
                emptyX = (emptySqNum/n - 1);
            }

            // If empty spot is on an even row from the bottom, return true if there is an odd # of inversions
            if ((n - emptyX) % 2 == 0) {
                return inversionCount(tiles) % 2 != 0;
            } 
            // If empty spot is on an odd row from the bottom, return true if there is an even # of inversions
            else {
                return inversionCount(tiles) % 2 == 0;
            }
        }
    }

    /**
     * counts the number of inversions in an ArrayList<Tile> object
     * @param tiles ArrayList where the count is being implemented
     * @return integer value of inversion count
     * @see #chkInversions(ArrayList, int)
     */
    private static int inversionCount(ArrayList<puzzle.Tile> tiles) {
        int invs = 0;
        for (int i = 0; i < (tiles.size() - 1); i++) {
            for (int j = i + 1; j < tiles.size(); j++) {
                int tile1SqNum = (tiles.get(i)).getSqNum();
                int tile2SqNum = (tiles.get(j)).getSqNum();
                if (tile1SqNum > tile2SqNum) {
                    invs++;
                }
            }
        }
        return invs;
    }

    /**
     * generates a solved puzzle of size n*n to use as an answer key
     * @param n int dimensions of answer key puzzle
     * @return an ArrayList of Tile objects containing the coordinates they will each have when puzzle is solved
     * @see #solved(ArrayList, ArrayList, int)
     */
    private static ArrayList<puzzle.Tile> getKey(int n) {
        ArrayList<puzzle.Tile> tiles = new ArrayList<>();
        for (int num = 1; num <= n * n - 1; num++) {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    int[] coords = {x, y};
                    tiles.add(new puzzle.Tile(num, n, coords));
                    num++;
                }
            }
        }
        tiles.remove(tiles.size() - 1);
        return tiles;
    }

    /**
     * method is run after every legal move input by player to sync new coordinates to Tile objects and displayed puzzle matrix
     * @param board int[][] being updated to visualize the current coordinates of Tiles
     * @param tiles ArrayList of Tile objects where coordinates are synced to
     * @see #runGame(ArrayList, ArrayList, int[][], int[][], int)
     */
    private static void update(int[][] board, ArrayList<puzzle.Tile> tiles) {
        for (puzzle.Tile t : tiles) {
            board[t.x()][t.y()] = t.getNum();
            t.updateLay(board);
        }
    }

    /**
     * visualize the coordinates of Tile objects on a 2D array
     * @param board int[][] which is being formatted for display
     * @see #runGame(ArrayList, ArrayList, int[][], int[][], int)
     */
    private static void printBoard(int[][] board) {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0)
                    System.out.print("   ");
                else
                    System.out.print(" " + board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * <p>
     * loop which accepts user input for moves until puzzle is solved.
     * The user inputs the number of the Tile they wish to move, and if there is an empty adjacent square,
     * the Tile is moved into that position.
     * </p>
     * @param tiles ArrayList of Tile objects which store puzzle coordinate data
     * @param ansKey ArrayList of Tile objects storing solved puzzle coordinates, to determine if puzzle has been solved
     * @param board int[][] to visualize puzzle coordinates
     * @param key   int[][] which is used to display how solved puzzle will look
     * @param tileMax   int passed to solved() to limit number of iterations; represents highest Tile number
     */
    private static void runGame(ArrayList<puzzle.Tile> tiles, ArrayList<puzzle.Tile> ansKey, int[][] board, int[][] key, int tileMax) {
        int DIM = (int)sqrt(tileMax+1);
        Scanner readIn = new Scanner(System.in);
        do {
            System.out.print("Input: ");
            String input = readIn.nextLine();
            try {
                int in = Integer.parseInt(input);
                if (in >= 1 && in <= tileMax) {
                    int oldVal = board[(tiles.get(in - 1)).x()][(tiles.get(in - 1)).y()];
                    board[(tiles.get(in - 1)).x()][(tiles.get(in - 1)).y()] = 0;
                    if ((tiles.get(in - 1)).move()) {
                        update(board, tiles);
                        printBoard(board);
                    } else {
                        board[(tiles.get(in - 1)).x()][(tiles.get(in - 1)).y()] = oldVal;
                        System.out.println("Cannot move this tile!");
                    }
                } else System.out.println("No tile found!");
            } catch (NumberFormatException e) {
                switch (input) {
                    case "help":
                    System.out.println("Welcome to the " + (DIM * DIM - 1) + " Puzzler! \n\n RULES: \n 1) Move the numbered tiles on the board \n\t until they are in order from 1-" + (DIM * DIM - 1) + ". \n\t It will look like this: ");
                    printBoard(key);
                    System.out.println(" 2) You can only move tiles into an \n\t empty space. You cannot move \n\t diagonally. \n\n 3) Input the number of the tile you \n\t wish to move and press Enter. \n");
                    System.out.println(" \n Other commands: \n\t help - show these rules again \n");
                    printBoard(board);
                        break;
                    default:
                        System.out.println("Invalid input!");
                }
            }
        } while (!solved(tiles, ansKey, tileMax));
        readIn.close();
    }

    /**
     * checks if one Tile ArrayList matches another; used to check current game board vs answer key
     * @param curr  the current ArrayList of Tile coodinates
     * @param ans   the ArrayList of solved Tile coordinates
     * @param max   int representing total number of Tile objects
     * @return  true if the coordinates stored in curr Tiles and ans Tiles are the same
     */
    private static boolean solved(ArrayList<puzzle.Tile> curr, ArrayList<puzzle.Tile> ans, int max) {
        int n = 0;
        for (int i = 0; i < max; i++) {
            int x = curr.get(i).x();
            int y = curr.get(i).y();
            int compX = ans.get(i).x();
            int compY = ans.get(i).y();
            if (!(x == compX && y == compY)) {
                n++;
            }
        }
        return n == 0;
    }

    /**
     * celebration lines to celebrate solving the puzzle
     * @return a random String from ArrayList of celebratory one-liners
     */
    private static ArrayList<String> celebrate() {
        ArrayList<String> winner = new ArrayList<>();
        winner.add("Winner, winner, chicken dinner! (Unless you're vegetarian, we have tofu options)");
        winner.add("Huzzah!");
        winner.add("Hurrah!");
        winner.add("Impressive!");
        winner.add("Felicitations.");
        winner.add("Noice!");
        Collections.shuffle(winner);
        return winner;
    }
}
