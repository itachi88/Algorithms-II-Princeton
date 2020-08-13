/* *****************************************************************************
 *  Name:Spandan Mishra
 *  Date: 5 Aug'20
 *  Description: Shortest Ancestral Path datatype
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private static final int INF = Integer.MAX_VALUE;
    private final Digraph graph;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Empty Graph");

        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        if (isInvalid(v) || isInvalid(w))
            throw new IllegalArgumentException("Vertex out of range");

        int lca = ancestor(v, w);
        if (lca == -1)
            return -1;

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);

        return vPath.distTo(lca) + wPath.distTo(lca);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        /* we can find the distance of v and w to every vertex in the graph and
         * the min distance would be the LCA */

        if (isInvalid(v) || isInvalid(w))
            throw new IllegalArgumentException("Vertex out of range");

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);

        // Since every vertex is an ancestor to itself
        if (v == w)
            return v;

        int minPathDistance = INF;
        int lowestCommonAncestor = -1;

        for (int vertex = 0; vertex < graph.V(); vertex++) {
            /*
                check if path exists to this vertex from v as well as w
                if yes, then update ancestor based on path distance
             */

            if (vPath.hasPathTo(vertex) && wPath.hasPathTo(vertex)) {
                int vwPathDistance = vPath.distTo(vertex) + wPath.distTo(vertex);
                if (vwPathDistance < minPathDistance) {
                    minPathDistance = vwPathDistance;
                    lowestCommonAncestor = vertex;
                }
            }
        }

        return lowestCommonAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        int lca = ancestor(v, w);

        if (lca == -1)
            return -1;

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);

        return vPath.distTo(lca) + wPath.distTo(lca);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null Iterator");

        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(graph, w);
        int minPathDistance = INF;
        int lca = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                int vwPathDistance = vPath.distTo(i) + wPath.distTo(i);
                if (vwPathDistance < minPathDistance) {
                    minPathDistance = vwPathDistance;
                    lca = i;
                }
            }
        }

        return lca;
    }

    private boolean isInvalid(int v) {
        return v < 0 || v >= graph.V();
    }

    // do unit testing of this class
    // public static void main(String[] args) {
    // In in = new In(args[0]);
    // Digraph g = new Digraph(in);
    // StdOut.println("=====Digraph======");
    // SAP s = new SAP(g);
    // StdOut.println(s.graph);
    //
    // // LCA seems to be fine for dg2.txt (linear graph)
    // StdOut.println("====LCA=====");
    // StdOut.println(s.ancestor(1, 2));
    // StdOut.println(s.ancestor(2, 4));
    //
    //
    // // length seems to be fine for dg2.txt
    // StdOut.println("=====Length of LCA path between v and w====");
    // StdOut.println(s.length(1, 2));
    // StdOut.println(s.length(2, 4));

    // }


}
