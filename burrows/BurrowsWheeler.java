import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // Read input string for CircularSuffixArray.
        String s = BinaryStdIn.readString();
        BinaryStdIn.close();

        CircularSuffixArray suffixes = new CircularSuffixArray(s);

        // Build output string of final column in CircularSuffixArray.
        // StringBuilder output = new StringBuilder();
        int originalIdx = 0;
        for (int i = 0; i < s.length(); i++) {
            if (suffixes.index(i) == 0) {
                // If first index is 0, last index is s.length() - 1.
                // output.append(s.charAt(s.length() - 1));
                originalIdx = i;
                break;
            }
        }

        // Write index (32 bit signed integer) of original string in sorted CircularSuffixArray to StdOut as 4 bytes.
        BinaryStdOut.write(originalIdx);
        // Write output string to StdOut with each character represented as 1 byte.
        for (int i = 0; i < suffixes.length(); i++) {
            BinaryStdOut.write(s.charAt((suffixes.index(i) + s.length() - 1) % s.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // Read integer index of original string in sorted CircularSuffixArray.
        int first = BinaryStdIn.readInt();
        // Read output string from transform method.
        String s = BinaryStdIn.readString();
        BinaryStdIn.close();

        // Key-indexed counting
        int n = s.length();
        int R = 256;
        int[] count = new int[R + 1];
        int[] next = new int[n];

        // [!, A, B, C, D, R] -> [..., 0, 1, 5, 2, 1, 1, 0, ..., 2, 0, ...]
        for (int i = 0; i < n; i++) {
            count[s.charAt(i) + 1]++;
        }

        // [..., 0, 1, 5, 2, 1, 1, 0, ..., 2, 0, ...] -> [..., 0, 1, 6, 8, 9, 10, 10, ..., 12, 12, ...]
        // Finds row indices for suffixes in sorted order.
        for (int r = 0; r < R; r++) {
            count[r + 1] += count[r];
        }

        // For each tail value encountered in s, we have a suffix that has been reached
        // by shifting a row in the sorted suffix array to the left by 1.
        // The index of that shifted row is obtained from the count array.
        // For each character encountered in s, we find the row that it was
        // shifted from in the count array and set the next row for that row
        // to be the current row.
        // This gives us the original order if we
        // we were to start at next value of original suffix and follow subsequent next values.
        for (int i = 0; i < s.length(); i++) {
            next[count[s.charAt(i)]++] = i;
        }

        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt(next[first]));
            first = next[first];
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }

}
