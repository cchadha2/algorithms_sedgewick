import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }      // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        String outcast = nouns[0];
        int maxDistance = Integer.MIN_VALUE;

        for (String candidate : nouns) {
            int distance = 0;
            for (String word2 : nouns) {
                distance += wordnet.distance(candidate, word2);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
                outcast = candidate;
            }
        }
        return outcast;
    } // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    } // see test client below
}