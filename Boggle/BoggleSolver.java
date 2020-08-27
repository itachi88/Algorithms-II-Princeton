/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 26 Aug 2020
 *  Description: Boggle Solver datatype
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver {

    private TSET trie;
    private boolean[][] vis;
    private int[] dx = { -1, 0, 1 };
    private int[] dy = { -1, 0, 1 };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new TSET();
        for (String s : dictionary)
            trie.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        int r = board.rows(), c = board.cols();
        HashSet<String> all = new HashSet<>();
        vis = new boolean[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                dfs(i, j, "", all, board);
            }
        }

        return all;
    }

    private void dfs(int row, int col, String curr, HashSet<String> set, BoggleBoard board) {

        char ch = board.getLetter(row, col);
        String tmp;

        if (ch == 'Q')
            tmp = curr + "QU";
        else
            tmp = curr + ch;

        if (vis[row][col])
            return;

        if (trie.contains(tmp) && tmp.length() > 2)
            set.add(tmp);

        // valid prefix check

        if (!trie.isPrefix(tmp))
            return;

        vis[row][col] = true;

        // 8 dir dfs

        for (int x : dx) {
            for (int y : dy) {
                if (x == 0 && y == 0)
                    continue;

                if (row + x >= 0 && row + x < board.rows() && col + y >= 0 && col + y < board
                        .cols()) {
                    if (!vis[row + x][col + y]) {
                        dfs(row + x, col + y, tmp, set, board);
                    }
                }
            }
        }

        // since I want no past paths on fresh dfs
        vis[row][col] = false;

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.contains(word))
            return 0;
        int n = word.length();
        int ret;
        if (n <= 2)
            ret = 0;
        else if (n <= 4)
            ret = 1;

        else if (n == 5)
            ret = 2;

        else if (n == 6)
            ret = 3;

        else if (n == 7)
            ret = 5;
        else
            ret = 11;

        return ret;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
