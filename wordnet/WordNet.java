import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private ST<Integer, SET<String>> table;
    private ST<String, SET<Integer>> invertedTable;
    private Digraph graph;
    private SAP paths;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        argNull(synsets);
        argNull(hypernyms);

        table = new ST<Integer, SET<String>>();
        invertedTable = new ST<String, SET<Integer>>();
        In synsetsStream = new In(synsets);
        while (synsetsStream.hasNextLine()) {
            String[] line = synsetsStream.readLine().split(",");
            for (String word : line[1].split(" ")) {

                int synsetId = Integer.parseInt(line[0]);
                if (table.get(synsetId) == null) { table.put(synsetId, new SET<String>()); }
                if (invertedTable.get(word) == null) { invertedTable.put(word, new SET<Integer>()); }
                table.get(synsetId).add(word);
                invertedTable.get(word).add(synsetId);
            }
        }

        graph = new Digraph(table.size());
        In hypernymsStream = new In(hypernyms);
        while (hypernymsStream.hasNextLine()) {
            String[] line = hypernymsStream.readLine().split(",");

            int synsetId = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(synsetId, Integer.parseInt(line[i]));
            }
        }
        DirectedCycle checkCycle = new DirectedCycle(graph);
        if (checkCycle.hasCycle()) { throw new IllegalArgumentException("Graph is not a DAG."); }

        this.paths = new SAP(graph);
    }

    private void argNull(Object arg) {
        if (arg == null) { throw new IllegalArgumentException("Argument cannot be null."); }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return invertedTable.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        argNull(word);

        return invertedTable.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        argNull(nounA);
        argNull(nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("One of the input words is not in WordNet.");
        }

        return paths.length(invertedTable.get(nounA), invertedTable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        argNull(nounA);
        argNull(nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("One of the input words is not in WordNet.");
        }

        return String.join(" ",
                           table.get(paths.ancestor(invertedTable.get(nounA), invertedTable.get(nounB))));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");

        for (String word : net.nouns()) {
            StdOut.println(word);
        }

        StdOut.println(net.distance("AND_circuit", "gate"));

        StdOut.println(net.sap("AND_circuit", "logic_gate"));
    }
}