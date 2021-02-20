import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private boolean solvable;
    private int moves;
    private Stack<Board> stack;

    private class Node implements Comparable<Node> {
        private Board board;
        private int numMoves;
        private Node prev;

        private int manhattanPriority() {
            return this.board.manhattan() + this.numMoves;
        }

        public int compareTo(Node that) {
            if (this.manhattanPriority() < that.manhattanPriority()) return -1;
            else if (this.manhattanPriority() > that.manhattanPriority()) return 1;
            return 0;
        }
    }

    private class StepSolver {
        private Node current;
        private MinPQ<Node> pq;
        private Stack<Board> stack;

        public StepSolver(Board initial) {
            pq = new MinPQ<Node>();
            stack = new Stack<Board>();

            current = new Node();
            current.board = initial;
            current.numMoves = 0;
            current.prev = new Node();

            pq.insert(current);
        }

        public boolean step() {
            current = pq.delMin();
            if (current.board.isGoal()) {
                moves = current.numMoves;
                while (current != null) {
                    stack.push(current.board);
                    current = current.prev;
                }
                return true;
            }
            else {
                for (Board neighbour : current.board.neighbors()) {
                    if (!neighbour.equals(current.prev.board)) {
                        Node next = new Node();
                        next.board = neighbour;
                        next.numMoves = current.numMoves + 1;
                        next.prev = current;
                        pq.insert(next);
                    }
                }
            }
            return false;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Initial board cannot be null");

        StepSolver board = new StepSolver(initial);
        StepSolver twin = new StepSolver(initial.twin());

        while (true) {
            boolean solved = board.step();
            if (solved) {
                stack = board.stack;
                solvable = true;
                break;
            }
            else if (twin.step()) {
                solvable = false;
                moves = -1;
                break;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) return stack;
        else return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
