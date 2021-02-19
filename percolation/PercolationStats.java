/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trials;
    private final double[] percolations;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be > 0");
        }
        this.trials = trials;

        percolations = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation obj = new Percolation(n);
            while (!obj.percolates()) {
                obj.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            double size = (n * n);
            double numOpen = obj.numberOfOpenSites();
            percolations[trial] = numOpen / size;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolations);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolations);
    }

    private double confidence() {
        return ((1.96 * Math.sqrt(stddev())) / Math.sqrt(trials));
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidence();
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidence();
    }

    // test client (see below)
    public static void main(String[] args) {
        int numGrids = Integer.parseInt(args[0]);
        int numTrials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(numGrids, numTrials);
        StdOut.printf("mean                     = %f%n", stats.mean());
        StdOut.printf("stddev                   = %f%n", stats.stddev());
        StdOut.printf("95%% confidence interval  = [%f,",
                      stats.confidenceLo());
        StdOut.printf(" %f]%n", stats.confidenceHi());
    }

}
