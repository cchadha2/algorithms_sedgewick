import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        argNull(G);

        this.G = new Digraph(G);
    }

    private void argNull(Object arg) {
        if (arg == null) { throw new IllegalArgumentException("Argument cannot be null."); }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        argNull(v);
        argNull(w);

        int parent = ancestor(v, w);
        if (parent == -1) {
            return -1;
        }
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G.reverse(), parent);
        return bfs.distTo(v) + bfs.distTo(w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        argNull(v);
        argNull(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return ancestorFinder(bfsV, bfsW);
    }

    // // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        argNull(v);
        argNull(w);

        int parent = ancestor(v, w);
        if (parent == -1) {
            return -1;
        }
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        return bfsV.distTo(parent) + bfsW.distTo(parent);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        argNull(v);
        argNull(w);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);

        return ancestorFinder(bfsV, bfsW);
    }

    private int ancestorFinder(BreadthFirstDirectedPaths a, BreadthFirstDirectedPaths b) {
        int distance = Integer.MAX_VALUE;
        int common = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (a.hasPathTo(vertex) && b.hasPathTo(vertex)) {
                int newDistance = a.distTo(vertex) + b.distTo(vertex);
                if (newDistance < distance) {
                    distance = newDistance;
                    common = vertex;
                }
            }
        }
        return common;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph graph = new Digraph(new In(args[0]));
        StdOut.println(graph);
        SAP sap = new SAP(graph);
        StdOut.println(sap.length(3, 7));
        StdOut.println(sap.length(3, 9));

        StdOut.println(sap.length(5, 7));
        StdOut.println(sap.length(5, 9));

        ArrayList<Integer> A = new ArrayList<Integer>();
        ArrayList<Integer> B = new ArrayList<Integer>();
        A.add(3);
        A.add(5);
        B.add(7);
        B.add(9);

        StdOut.println(sap.length(A, B));
    }
}
