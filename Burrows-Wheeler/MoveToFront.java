/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 5 Sept 2020
 *  Description: Move to front encoding and decoding
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        char[] dict = init();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i;
            for (i = 0; i < 256; i++)
                if (dict[i] == c)
                    break;
            moveToFront(dict, i);
            BinaryStdOut.write(i, 8);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] dict = init();
        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readInt(8);
            BinaryStdOut.write(dict[idx], 8);
            moveToFront(dict, idx);
        }

        BinaryStdOut.flush();
    }

    private static char[] init() {
        char[] dict = new char[256];
        for (int i = 0; i < 256; i++)
            dict[i] = (char) i;
        return dict;
    }

    private static void moveToFront(char[] dict, int id) {
        char c = dict[id];
        // copy
        for (int i = id; i > 0; i--)
            dict[i] = dict[i - 1];
        dict[0] = c;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else decode();
    }
}
