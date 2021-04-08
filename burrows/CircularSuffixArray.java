import java.util.Arrays;

public class CircularSuffixArray {

    private String s;
    private CircularSuffix[] suffixes;

    private class CircularSuffix implements Comparable<CircularSuffix> {

        private int firstIdx;

        public CircularSuffix(int firstIdx) {
            this.firstIdx = firstIdx;
        }

        public int compareTo(CircularSuffix that) {
            int comparison = compareChars(s.charAt(firstIdx), s.charAt(that.firstIdx));

            int thisIdx = firstIdx;
            int thatIdx = that.firstIdx;


            for (int i = 0; i < s.length(); i++) {
                if (comparison == 0) {
                    thisIdx = nextIdx(thisIdx);
                    thatIdx = nextIdx(thatIdx);
                    comparison = compareChars(s.charAt(thisIdx), s.charAt(thatIdx));
                }
                else {
                    break;
                }
            }
            
            return comparison;
        }

        private int nextIdx(int idx) {
            if (!(idx == s.length() - 1)) return idx + 1;
            return 0;
        }

        private int compareChars(char thisChar, char thatChar) {
            if (thisChar < thatChar) return -1;
            else if (thisChar > thatChar) return 1;
            return 0;
        }


    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        this.s = s;

        // Create array of CircularSuffix objects.
        suffixes = new CircularSuffix[s.length()];
        for (int idx = 0; idx < s.length(); idx++) {
            suffixes[idx] = new CircularSuffix(idx);
        }

        // Sort CircularSuffix objects in place.
        Arrays.sort(suffixes);
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= s.length()) {
            throw new IllegalArgumentException("Index outside of range");
        }

        return suffixes[i].firstIdx;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String test = "ABRACADABRA!";

        CircularSuffixArray testSuffixes = new CircularSuffixArray(test);

        System.out.println(testSuffixes.length());

        for (int idx = 0; idx < testSuffixes.length(); idx++) {
            System.out.println(testSuffixes.index(idx));
        }

        System.out.println();

        CircularSuffixArray newTest = new CircularSuffixArray("BANANA");

        for (int idx = 0; idx < newTest.length(); idx++) {
            System.out.println(newTest.index(idx));
        }
        System.out.println();

        CircularSuffixArray newTest2 = new CircularSuffixArray("*************");

        for (int idx = 0; idx < newTest2.length(); idx++) {
            System.out.println(newTest2.index(idx));
        }

    }

}
