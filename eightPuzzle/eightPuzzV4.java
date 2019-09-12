/**
 * This package is comprised of two classes.
 * eightPuzzV4 is the main class, and Tile is a helper class.
 *
 * @since 2.0
 */

package eightPuzzle;

import java.util.*;
import static java.lang.Math.sqrt;
import static java.lang.Integer.valueOf;

/*
NEEDS FIXING:
chkInversions for even DIM
printBoard() (probably needs separate class with printf() formatting)
 */

/**
 * This is a simple n-puzzle game, just compile and run. Instructions print on screen.
 * Private methods of eightPuzzV4 class are documented for personal reference.
 * @author Andalib Malit Samandari, 2018
 * @version 4.2
 */
public class eightPuzzV4 {
    /**
     * main method, doesn't accept any parameters
     * @param foo   N/A
     * @see #puzzle(int)
     */
    public static void main(String[] foo) {
        int size = 0;
        Scanner scan;
        System.out.println("What size puzzle (n * n) would you like to solve?");

        boolean puzzleStart = false;

        do {
          scan = new Scanner(System.in);
          System.out.print("Enter an integer 3 >= n >= 4: ");
          if (scan.hasNextInt()){
            size = scan.nextInt();
            puzzleStart = true;
            if (size<3 || size>4) puzzleStart = false;
        }
        } while(!puzzleStart);

        puzzle(size);

        scan.close();
    }

    /**
     * method that generates an n*n puzzle and answer key, then starts the game
     * @param num   the int that controls the generated square puzzle's dimensions
     * @see #runGame(ArrayList, ArrayList, int[][], int[][], int)
     */
    private static void puzzle(int num) {

        final int DIM = valueOf(num);

        //Generates a puzzle and a solved version to check it against.
        ArrayList<Tile> tiles = generate(DIM);
        ArrayList<Tile> ansKey = getKey(DIM);

        int[][] board = new int[DIM][DIM];
        int[][] key = new int[DIM][DIM];

        update(board, tiles);
        update(key, ansKey);

        // Instructions
        System.out.println("Welcome to the " + DIM + " Puzzler! \n\n RULES: \n 1) Move the numbered tiles on the board \n\t until they are in order from 1-" + (DIM * DIM - 1) + ". \n\t It will look like this: ");
        printBoard(key);
        System.out.println(" 2) You can only move tiles into an \n\t empty space. You cannot move \n\t diagonally. \n\n 3) Input the number of the tile you \n\t wish to move and press Enter. \n");
        System.out.println(" \n Other commands: \n\t help - show these rules again \n");
        printBoard(board);

        runGame(tiles, ansKey, board, key, DIM * DIM - 1);

        //Display random witty comment after game is won
        System.out.println(wit().get(0));

    }

    /**
     * Creates n*n - 1 Tile objects for the puzzle (see Tile class). If the given combination of coordinates makes an unsolvable puzzle, a new set is generated.
     * @param n the int which represents the dimensions of the puzzle generated, and controls the iterations of Tile creation; passed down from puzzle()
     * @return  an ArrayList of Tiles which will store the data for each Tile object in the puzzle
     * @see #chkInversions(ArrayList, int)
     */
    private static ArrayList<Tile> generate(int n) {
        ArrayList<int[]> poss = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[] coord = new int[2];
                coord[0] = i;
                coord[1] = j;
                poss.add(coord);
            }
        }
        ArrayList<Tile> tiles;
        do {
            Collections.shuffle(poss);
            tiles = new ArrayList<>();
            for (int z = 0; z < n * n - 1; z++) {
                tiles.add(new Tile(z + 1, n, (poss.get(z))));
            }
        } while (!chkInversions(tiles, n));
        return tiles;
    }

    /**
     * method that determines if the generated puzzle is solvable by calculating inversions
     * @see <a href="https://www.geeksforgeeks.org/check-instance-15-puzzle-solvable"></a>
     * @param tiles the ArrayList of Tile objects being checked for solvability
     * @param n int used to determine if number of rows is even or odd
     * @return true if number of inversions in puzzle makes it solvable
     */
    private static boolean chkInversions(ArrayList<Tile> tiles, int n) {
        if (n % 2 != 0) {
            int q = 0;
            for (int i = 0; i < tiles.size() - 1; i++)
                for (int j = i + 1; j < tiles.size(); j++) {
                    int t = (tiles.get(i)).getSqValue();
                    int c = (tiles.get(j)).getSqValue();
                    if (t > c)
                        q++;
                }
            return q % 2 == 0;
        } else {
            ArrayList<int[]> tempCoords = new ArrayList<>();
            for (Tile t : tiles) {
                tempCoords.add(t.getCoords());
            }
            ArrayList<int[]> tempCompare = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    int[] i = {j, k};
                    tempCompare.add(i);
                }
            }
            int emptyX = 0;
            for (int[] arr : tempCompare) {
                if (!tempCoords.contains(arr)) {
                    emptyX = arr[0];
                }
            }
            //if empty spot is an even distance from bottom
            if ((n - emptyX + 1) % 2 == 0) {
                int q = 0;
                for (int i = 0; i < tiles.size() - 1; i++)
                    for (int j = i + 1; j < tiles.size(); j++) {
                        int t = (tiles.get(i)).getSqValue();
                        int c = (tiles.get(j)).getSqValue();
                        if (t > c)
                            q++;
                    }
                return q % 2 != 0;
            } else {
                int q = 0;
                for (int i = 0; i < tiles.size() - 1; i++)
                    for (int j = i + 1; j < tiles.size(); j++) {
                        int t = (tiles.get(i)).getSqValue();
                        int c = (tiles.get(j)).getSqValue();
                        if (t > c)
                            q++;
                    }
                return q % 2 == 0;
            }
        }
    }

    /**
     * generates a solved puzzle of size n*n to use as an answer key
     * @param n int dimensions of answer key puzzle
     * @return  an ArrayList of Tile objects containing the coordinates they will each have when puzzle is solved
     * @see #solved(ArrayList, ArrayList, int)
     */
    private static ArrayList<Tile> getKey(int n) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int k = 1; k <= n * n - 1; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int[] c = {i, j};
                    tiles.add(new Tile(k, n, c));
                    k++;
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
    private static void update(int[][] board, ArrayList<Tile> tiles) {
        for (Tile t : tiles) {
            board[t.x()][t.y()] = t.getNum();
            t.updateLay(board);
        }
    }

    //FIX: tweak for display of board with n>3.

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
    private static void runGame(ArrayList<Tile> tiles, ArrayList<Tile> ansKey, int[][] board, int[][] key, int tileMax) {
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
                    System.out.println("Welcome to the " + DIM + " Puzzler! \n\n RULES: \n 1) Move the numbered tiles on the board \n\t until they are in order from 1-" + (DIM * DIM - 1) + ". \n\t It will look like this: ");
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
    //Why is i < max and not <= max??

    /**
     * checks if one Tile ArrayList matches another; used to check current game board vs answer key
     * @param curr  the current ArrayList of Tile coodinates
     * @param ans   the ArrayList of solved Tile coordinates
     * @param max   int representing total number of Tile objects
     * @return  true if the coordinates stored in curr Tiles and ans Tiles are the same
     */
    private static boolean solved(ArrayList<Tile> curr, ArrayList<Tile> ans, int max) {
        int n = 0;
        for (int i = 0; i < max; i++) {
            int x = curr.get(i).x();
            int y = curr.get(i).y();
            int compX = ans.get(i).x();
            int compY = ans.get(i).y();
            if (!(x == compX && y == compY))
                n++;
        }
        return n == 0;
    }

    /**
     * witty one-liners to celebrate solving the puzzle
     * @return a random String from ArrayList of witty one-liners
     */
    private static ArrayList<String> wit() {
        ArrayList<String> winner = new ArrayList<>();
        winner.add("Winner, winner, chicken dinner! (Unless you're vegetarian, we have tofu options)");
        winner.add("Huzzah!");
        winner.add("Hurrah!");
        winner.add("Impressive! You're a regular Einstein/Curie/Carver/Andalib!");
        winner.add("Felicitations.");
        winner.add("Noice! (Bonus points if you know which K&P skit this is from)");
        Collections.shuffle(winner);
        return winner;
    }
}
