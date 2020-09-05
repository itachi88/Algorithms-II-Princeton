/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 4 Sep 2020
 *  Description: Burrows Wheeler
 **************************************************************************** */


import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.HashMap;

public class BurrowsWheeler {


    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {

        String s = BinaryStdIn.readString();
        CircularSuffixArray cs = new CircularSuffixArray(s);
        for (int i = 0; i < cs.length(); i++) {
            if (cs.index(i) == 0) {
                BinaryStdOut.write(i); // original string index
                break;
            }
        }

        for (int i = 0; i < cs.length(); i++)
            BinaryStdOut.write(s.charAt((cs.index(i) + cs.length() - 1) % cs.length()));

        BinaryStdOut.flush();

    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // int first = BinaryStdIn.readInt();
        // String lc = BinaryStdIn.readString();

        int first = 3;
        String lc = "ARD!RCAAAABB";

        char[] fc = lc.toCharArray();
        int n = lc.length();
        Arrays.sort(fc);

        int[] next = new int[n];
        // System.out.println(Arrays.toString(fc));

        // boolean[] vis = new boolean[n];
        //
        // for (int i = 0; i < n; i++) {
        //     // linear search the character from fc in lc and should be non visited
        //     // order in fc is same as in lc
        //     for (int j = 0; j < n; j++) {
        //         if (fc[i] == lc.charAt(j) && !vis[j]) {
        //             next[i] = j;
        //             vis[j] = true;
        //             break;
        //         }
        //     }
        // }

        HashMap<Character, Queue<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (!map.containsKey(lc.charAt(i))) {
                Queue<Integer> q = new Queue<Integer>();
                q.enqueue(i);
                map.put(lc.charAt(i), q);
            }

            else
                map.get(lc.charAt(i)).enqueue(i);


        }

        // next array creation

        for (int i = 0; i < n; i++) {
            int id = map.get(fc[i]).dequeue();
            next[i] = id;
        }


        // System.out.println(map);

        // System.out.println(Arrays.toString(next));
        // System.out.println(Arrays.toString(vis));

        StringBuilder sb = new StringBuilder();
        int i = 0, j = first;
        while (i < n) {
            sb.append(lc.charAt(next[j]));
            j = next[j];
            i++;
        }

        // System.out.println(sb);
        BinaryStdOut.write(sb.toString());
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        // System.out.println("invert===>");
        // inverseTransform();
        if (args[0].equals("-")) BurrowsWheeler.transform();
        else BurrowsWheeler.inverseTransform();
    }

}

