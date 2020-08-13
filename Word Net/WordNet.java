/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 6th August 2020
 *  Description: WordNet Datatype
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final Map<Integer, String> idToNoun;
    private final Map<String, List<Integer>> nounToId;
    private final Digraph graph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null || synsets.isEmpty()
                || hypernyms.isEmpty())
            throw new IllegalArgumentException("Invalid file(s)");

        In syns = new In(synsets);
        In hyps = new In(hypernyms);

        idToNoun = new HashMap<>();
        nounToId = new HashMap<>();

        while (!syns.isEmpty()) {

            String[] strs = syns.readLine().split(",");
            int id = Integer.parseInt(strs[0]);
            String synset = strs[1];
            String[] nouns = synset.split("\\s+");
            idToNoun.put(id, synset);

            for (String noun : nouns) {
                if (nounToId.containsKey(noun)) {
                    nounToId.get(noun).add(id);
                }
                else {
                    List<Integer> ls = new ArrayList<>();
                    ls.add(id);
                    nounToId.put(noun, ls);
                }
            }

        }
        // digraph has as many nodes as synset ids
        graph = new Digraph(idToNoun.size());

        while (!hyps.isEmpty()) {
            String[] strs = hyps.readLine().split(",");
            int id = Integer.parseInt(strs[0]);
            // List<Integer> ls = new ArrayList<>();

            for (int i = 1; i < strs.length; i++) {
                int hid = Integer.parseInt(strs[i]);
                // ls.add(hid);
                graph.addEdge(id, hid);
            }
        }

        DirectedCycle cyc = new DirectedCycle(graph);
        if (cyc.hasCycle())
            throw new IllegalArgumentException("Cycle detected");

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null || word.isEmpty())
            throw new IllegalArgumentException("Invalid word");
        return nounToId.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not present in word net");

        List<Integer> aIter = nounToId.get(nounA);
        List<Integer> bIter = nounToId.get(nounB);

        SAP sap = new SAP(graph);

        return sap.length(aIter, bIter);

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not present in word net");

        List<Integer> aIter = nounToId.get(nounA);
        List<Integer> bIter = nounToId.get(nounB);

        SAP sap = new SAP(graph);
        int lca = sap.ancestor(aIter, bIter);
        return idToNoun.get(lca);
    }

    // do unit testing of this class
    // public static void main(String[] args) {
    //
    //     // WordNet wordNet = new WordNet("/synsets.txt", "/hypernyms.txt");
    //     // System.out.println("========WordNet=====");
    //     //
    //     // // StdOut.println(wordNet.graph);
    //     // StdOut.println(wordNet.idToNoun);
    //     // StdOut.println(wordNet.nounToId);
    //
    // }

}
