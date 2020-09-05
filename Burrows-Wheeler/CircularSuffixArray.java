/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 4 Sept 2020
 *  Description: Circular Suffix Array DS
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private final String s;
    private Integer[] idx;


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new NullPointerException("Null String passed!");
        this.s = s;
        idx = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++)
            idx[i] = i;

        Arrays.sort(idx, new CSAComp());
    }


    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length())
            throw new IllegalArgumentException("Out of range!");
        return idx[i];
    }

    // unit testing (required)
    public static void main(String[] args) {

        String test = "ABRACADABRA!";
        CircularSuffixArray csu = new CircularSuffixArray(test);
        for (int i = 0; i < csu.length(); i++) {
            System.out.print(csu.index(i) + "\t");
        }
        System.out.println();
        for (int i = 0; i < csu.length(); i++) {
            System.out.print(test.charAt(csu.index(i)));
        }
    }

    private class CSAComp implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            int first = o1, sec = o2;
            for (int i = 0; i < length(); i++) {
                if (first > length() - 1)
                    first = 0; // wrap around

                if (sec > length() - 1)
                    sec = 0;

                if (s.charAt(first) < s.charAt(sec))
                    return -1;

                if (s.charAt(first) > s.charAt(sec))
                    return 1;

                first++;
                sec++;
            }

            // equal
            return 0;
        }
    }
}
