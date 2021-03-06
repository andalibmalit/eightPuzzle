package puzzle;

/**
 * This package is comprised of two classes.
 * NPuzzleV5 is the main class, and Tile is a helper class.
 *
 * @since 2.0
 */

/**
 * <p>
 * Tile objects are numbered starting at one and ending at dim*dim - 1.
 * They store important information such as coordinates and location,
 * and have methods to update said info after a Tile is moved.
 * </p>
 * @author A.M.S., 2020
 * @version 5.0
 */
public class Tile {
    private int number, dim, sqNum;
    private int[] coords = new int[2];
    private int[][] layout = null;

    /**
     * sole constructor for a Tile object
     * @param z the number assigned the Tile to be displayed on the puzzle board
     * @param n    the dimensions of the, n*n, are used to calculate sqNum
     * @param c pair of ints used to set initial coordinates of Tile
     * @see #calcSqNum()
     * @see #newCoords(int[])
     */
    Tile(int z, int n, int[] coords) {
        number = z;
        dim = n;
        newCoords(coords);
    }

    // Getters
    /**
     * retrieve x-coordinate
     * @return int, x-coordinate of Tile (which row of the 2D array it is in)
     */
    public int x() {
        return coords[0];
    }
    /**
     * retrieve y-coordinate
     * @return int, y-coordinate of Tile (which column of the 2D array it is in)
     */
    public int y() {
        return coords[1];
    }
    /**
     * retrieve number of Tile object
     * @return int, number of Tile object which is displayed on the screen
     */
    public int getNum() {
        return number;
    }

    /**
     * sqNum tracks which "square" (spot in the 2D array) the Tile is linked to. Squares are numbered from left to right and top to bottom.
     * @return int, which square (from 1 - n*n) the Tile is on
     */
    public int getSqNum() {
        return sqNum;
    }

    /**
     * retrieve both coordinates of Tile object
     * @return an int[] with the x-coordinate in coords[0] and the y-coordinate in coords[1]
     */
    public int[] getCoords() {
        return coords;
    }

    // Setters
    /**
     * Each Tile object stores a copy of the current game board state to determine if moving said Tile object is legal.
     * @param newLay    an int[][] representing the game board, 0 means that spot is empty
     */
    public void updateLay(int[][] newLay) {
        layout = newLay;
    }

    /**
     * moves the Tile object to the adjacent empty position, if any
     * @return true if the Tile was moved, false if all adjacent squares are occupied
     */
    public boolean move() {
        if (coords[0] - 1 > -1 && layout[coords[0] - 1][coords[1]] == 0) {
            int[] temp = {coords[0] - 1, coords[1]};
            newCoords(temp);
            return true;
        } else if (coords[0] + 1 < dim && layout[coords[0] + 1][coords[1]] == 0) {
            int[] temp = {coords[0] + 1, coords[1]};
            newCoords(temp);
            return true;
        } else if (coords[1] - 1 > -1 && layout[coords[0]][coords[1] - 1] == 0) {
            int[] temp = {coords[0], coords[1] - 1};
            newCoords(temp);
            return true;
        } else if (coords[1] + 1 < dim && layout[coords[0]][coords[1] + 1] == 0) {
            int[] temp = {coords[0], coords[1] + 1};
            newCoords(temp);
            return true;
        } else return false;
    }

    // Find the new sqNum whenever a Tile is moved
    private void newCoords(int[] newC) {
        coords = newC;
        calcSqNum();
    }

    private void calcSqNum() {
        if (coords[0] == 0)
            sqNum = coords[1] + 1;
        else if (coords[1] == 0)
            sqNum = coords[0] * dim + 1;
        else sqNum = coords[0] * dim + coords[1] + 1;
    }

    // Testing purposes
    public String toString() {
        String t = ("TILE " + number + ": ");
        String tNum = ("(" + coords[0] + ", " + coords[1] + ")");
        return t + tNum;
    }
}