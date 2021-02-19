/* *****************************************************************************
 *  Name:              Chirag Chadha
 *  Coursera User ID:  123456
 *  Last modified:     19/02/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int gridSize;
    private boolean[][] grid;
    private int numOpen = 0;
    private final int n;
    private final WeightedQuickUnionUF sets;
    private final WeightedQuickUnionUF fullUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;

        grid = new boolean[n + 1][n + 1];
        gridSize = (n * n);

        // Add 2 indices for virtual sites.
        sets = new WeightedQuickUnionUF(2 + gridSize);
        fullUF = new WeightedQuickUnionUF(1 + gridSize);
    }

    private void indexChecker(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    private int twoDToOneD(int row, int col) {
        indexChecker(row, col);
        return n * (row - 1) + col;
    }

    private void neighbourUnion(int currentIndex, int row, int col) {
        try {
            indexChecker(row, col);
            if (isOpen(row, col)) {
                sets.union(currentIndex, twoDToOneD(row, col));
                fullUF.union(currentIndex, twoDToOneD(row, col));
            }
        }
        catch (IllegalArgumentException error) {
            // do nothing
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        indexChecker(row, col);
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            numOpen++;
            if (row == 1) {
                sets.union(0, twoDToOneD(row, col));
                fullUF.union(0, twoDToOneD(row, col));
            }
            if (row == n) {
                sets.union(gridSize + 1, twoDToOneD(row, col));
            }
            neighbourUnion(twoDToOneD(row, col), row - 1, col);
            neighbourUnion(twoDToOneD(row, col), row + 1, col);
            neighbourUnion(twoDToOneD(row, col), row, col - 1);
            neighbourUnion(twoDToOneD(row, col), row, col + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        indexChecker(row, col);
        return (grid[row][col]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        indexChecker(row, col);
        if (isOpen(row, col)) {
            return (fullUF.find(0) == fullUF.find(twoDToOneD(row, col)));
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return (sets.find(gridSize + 1) == sets.find(0));
    }

    // test client (optional)
    // public static void main(String[] args) {
    //     Percolation obj = new Percolation(5);
    //     System.out.println(obj.numberOfOpenSites());
    //     System.out.println(obj.isOpen(1, 1));
    //     System.out.println(obj.isOpen(2, 1));
    //     obj.open(2, 1);
    //     System.out.println(obj.isOpen(2, 1));
    //     System.out.println(obj.numberOfOpenSites());
    //     System.out.println(obj.sets);
    //     System.out.println(obj.twoDToOneD(4, 5));
    //     obj.open(1, 2);
    //     System.out.println(obj.isFull(1, 2));
    //     obj.open(2, 2);
    //     System.out.println(obj.isFull(2, 2));
    //     obj.open(3, 2);
    //     System.out.println(obj.isFull(3, 2));
    //     obj.open(4, 2);
    //     System.out.println(obj.isFull(4, 2));
    //     System.out.println(obj.percolates());
    //     obj.open(5, 2);
    //     System.out.println(obj.isFull(5, 2));
    //     System.out.println(obj.numberOfOpenSites());
    //     System.out.println(obj.percolates());
    // }
}
