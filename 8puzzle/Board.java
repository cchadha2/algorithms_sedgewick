import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    private final int n;
    private int[][] tileState;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles[0].length;
        tileState = new int[n][n];
        for (int row = 0; row < n; row++) {
            tileState[row] = Arrays.copyOf(tiles[row], tiles[row].length);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder boardState = new StringBuilder();

        boardState.append(n + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                boardState.append(" " + tileState[row][col] + " ");
                if (col == n - 1 && row != n - 1) boardState.append("\n");
            }
        }
        return boardState.toString();

    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int outOfPlace = 0;

        int goal = 1;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row == n - 1 && col == n - 1) break;
                if (!(tileState[row][col] == goal)) outOfPlace++;
                goal++;
            }
        }

        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int goal = 1;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = tileState[row][col];
                if (tile == 0) break;
                else if (!(tile == goal)) {
                    
                    distance += goal;
                }
                goal++;
            }
        }
        return distance;
    }


    // is this board the goal board?
    public boolean isGoal() {
        int[][] goalTiles = new int[n][n];
        goalTiles[n - 1][n - 1] = 0;

        int goal = 1;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row == n - 1 && col == n - 1) break;
                goalTiles[row][col] = goal;
                goal++;
            }
        }

        Board goalBoard = new Board(goalTiles);
        if (this.equals(goalBoard)) return true;
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        else if (this == y) return true;
        else if (!(this.getClass() == y.getClass())) return false;

        Board comparisonBoard = (Board) y;
        if (!(n == comparisonBoard.n)) return false;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (!(tileState[row][col] == comparisonBoard.tileState[row][col])) {
                    return false;
                }
            }
        }
        return true;

    }

    private boolean tileChecker(int row, int col) {
        return ((row >= 0 && row <= n - 1) && (col >= 0 && col <= n - 1));
    }

    private ArrayList<Board> neighbourChecker(int row, int col) {
        ArrayList<Board> neighbours = new ArrayList<Board>();

        if (tileChecker(row + 1, col)) {
            Board neighbour1 = new Board(tileState);
            int neighbourTile = neighbour1.tileState[row + 1][col];
            neighbour1.tileState[row][col] = neighbourTile;
            neighbour1.tileState[row + 1][col] = 0;
            neighbours.add(neighbour1);
        }

        if (tileChecker(row, col + 1)) {
            Board neighbour2 = new Board(tileState);
            int neighbourTile = neighbour2.tileState[row][col + 1];
            neighbour2.tileState[row][col] = neighbourTile;
            neighbour2.tileState[row][col + 1] = 0;
            neighbours.add(neighbour2);
        }

        if (tileChecker(row, col - 1)) {
            Board neighbour3 = new Board(tileState);
            int neighbourTile = neighbour3.tileState[row][col - 1];
            neighbour3.tileState[row][col] = neighbourTile;
            neighbour3.tileState[row][col - 1] = 0;
            neighbours.add(neighbour3);
        }

        if (tileChecker(row - 1, col)) {
            Board neighbour4 = new Board(tileState);
            int neighbourTile = neighbour4.tileState[row - 1][col];
            neighbour4.tileState[row][col] = neighbourTile;
            neighbour4.tileState[row - 1][col] = 0;
            neighbours.add(neighbour4);
        }

        return neighbours;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int zeroRow = 0;
        int zeroCol = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tileState[row][col] == 0) {
                    zeroRow = row;
                    zeroCol = col;
                }
            }
        }
        return neighbourChecker(zeroRow, zeroCol);
    }


    // a board that is obtained by exchanging any pair of tiles
    //public Board twin() {

    //}

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        tiles[0][0] = 1;
        tiles[0][1] = 0;
        tiles[0][2] = 2;
        tiles[1][0] = 3;
        tiles[1][1] = 4;
        tiles[1][2] = 5;
        tiles[2][0] = 6;
        tiles[2][1] = 7;
        tiles[2][2] = 8;
        Board testBoard = new Board(tiles);
        StdOut.println(testBoard);

        StdOut.println(testBoard.hamming());
        StdOut.println(testBoard.manhattan());
        StdOut.println(testBoard.equals(testBoard));

        int[][] comparisonTiles = new int[3][3];
        comparisonTiles[0][0] = 1;
        comparisonTiles[0][1] = 0;
        comparisonTiles[0][2] = 2;
        comparisonTiles[1][0] = 3;
        comparisonTiles[1][1] = 4;
        comparisonTiles[1][2] = 8;
        comparisonTiles[2][0] = 7;
        comparisonTiles[2][1] = 6;
        comparisonTiles[2][2] = 5;

        Board comparisonBoard = new Board(comparisonTiles);
        StdOut.println(comparisonBoard);
        StdOut.println(testBoard.equals(comparisonBoard));

        int[][] tilesNew = new int[3][3];
        tilesNew[0][0] = 1;
        tilesNew[0][1] = 2;
        tilesNew[0][2] = 3;
        tilesNew[1][0] = 4;
        tilesNew[1][1] = 5;
        tilesNew[1][2] = 6;
        tilesNew[2][0] = 7;
        tilesNew[2][1] = 8;
        tilesNew[2][2] = 0;
        Board testBoardTwo = new Board(tilesNew);
        StdOut.println(testBoardTwo);

        StdOut.println(testBoard.equals(testBoardTwo));

        StdOut.println(testBoard.isGoal());
        StdOut.println(testBoardTwo.isGoal());

        for (Board elem : testBoard.neighbors()) {
            StdOut.println(elem);
        }
        StdOut.println(testBoard);
    }
}
