/* *****************************************************************************
 *  Name:Spandan Mishra
 *  Date: 7 Aug 2020
 *  Description: Outcast datatype
 **************************************************************************** */

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int max = Integer.MIN_VALUE;
        String outcast = "";
        for (String from : nouns) {
            int dist = 0;
            for (String to : nouns) {
                dist += wordnet.distance(from, to);
            }

            if (dist > max) {
                max = dist;
                outcast = from;
            }
        }

        return outcast;
    }

    // // see test client below
    // public static void main(String[] args) {
    //
    // }
}
