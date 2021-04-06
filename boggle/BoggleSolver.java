import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver
{
    private TrieSETNew dict;
    private BoggleBoard currentBoard;
    private SET<String> validWords;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new TrieSETNew();

        for (String word : dictionary) {
            dict.add(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        currentBoard = board;
        validWords = new SET<String>();

        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                boolean[] marked = new boolean[currentBoard.rows() * currentBoard.cols()];
                String word = String.valueOf(currentBoard.getLetter(row, col));
                if (word.equals("Q")) { word += "U"; }
                possibleWords(marked, row, col, word);
            }
        }
        return validWords;
    }

    private boolean adjacentDiceChecker(int row, int col) {
        if (row < 0 || row >= currentBoard.rows() || col < 0 || col >= currentBoard.cols()) { return false; }
        return true;
    }

    private int TwoDtoOneD (int row, int col) {
        return row*currentBoard.rows() + col;
    }

    private void possibleWords (boolean[] marked, int row, int col, String word) {
        marked[TwoDtoOneD(row, col)] = true;

        if (!dict.containsPrefix(word)) return;
        if (dict.contains(word) && word.length() >= 3) validWords.add(word);

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (adjacentDiceChecker(i, j) && !marked[TwoDtoOneD(i, j)]) {
                    if (currentBoard.getLetter(i, j) == 'Q') {
                        possibleWords(marked.clone(), i, j, word + currentBoard.getLetter(i, j) + 'U');
                    }
                    else {
                        possibleWords(marked.clone(), i, j, word + currentBoard.getLetter(i, j));
                    }
                }
            }
        }

        return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dict.contains(word)) return 0;

        int len = word.length();
        if (len < 3) return 0;
        else if (len <= 4) return 1;
        else if (len <= 6) return len - 3;
        else if (len == 7) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int len = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            len += 1;
        }
        StdOut.println("Number of valid words = " + len);
        StdOut.println("Score = " + score);
    }
}