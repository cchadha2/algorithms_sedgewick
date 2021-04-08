import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class MoveToFront {

    private static int R = 256;
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // Maintain a dictionary to look up characters by current index and a
        // reverse index to find current indices by given character.
        SeparateChainingHashST<Character, Integer> revIdx
                = new SeparateChainingHashST<Character, Integer>(R);

        char[] dict = new char[R];
        for (int i = 0; i < R; i++) {
            revIdx.put(Alphabet.EXTENDED_ASCII.toChar(i), i);
            dict[i] = Alphabet.EXTENDED_ASCII.toChar(i);
        }


        while (!BinaryStdIn.isEmpty()) {
            char current = BinaryStdIn.readChar();
            // Lookup current index of character in reverse index.
            int currentIdx = revIdx.get(current);

            // Write current index to standard output.
            BinaryStdOut.write(currentIdx, 8);

            // Move current character to front.
            for (int i = currentIdx; i > 0; i--) {
                dict[i] = dict[i - 1];
            }
            dict[0] = current;

            // Update dictionary of character to index values.
            for (int i = 0; i < R; i++) {
                revIdx.put(dict[i], i);
            }

        }

        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        // Set a static radix value.

        // Maintain a dictionary to look up characters by current index and a
        // reverse index to find current indices by given character.
        SeparateChainingHashST<Character, Integer> revIdx
                = new SeparateChainingHashST<Character, Integer>(R);

        char[] dict = new char[R];
        for (int i = 0; i < R; i++) {
            revIdx.put(Alphabet.EXTENDED_ASCII.toChar(i), i);
            dict[i] = Alphabet.EXTENDED_ASCII.toChar(i);
        }


        while (!BinaryStdIn.isEmpty()) {
            int currentIdx = BinaryStdIn.readInt(8);
            // Lookup current character by index in dict.
            char current = dict[currentIdx];

            // Write current character to standard output.
            BinaryStdOut.write(current);

            // Move current character to front.
            for (int i = currentIdx; i > 0; i--) {
                dict[i] = dict[i - 1];
            }
            dict[0] = current;

            // Update dictionary of character to index values.
            for (int i = 0; i < R; i++) {
                revIdx.put(dict[i], i);
            }

        }

        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }

}
