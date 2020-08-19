/* *****************************************************************************
 *  Name: Spandan Mishra
 *  Date: 19 Aug 2020
 *  Description: Baseball Elimination
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseballElimination {

    private int numberOfTeams;
    private String[] teams;
    private int[] win, loss, rem;
    private int[][] g;
    private Map<String, Integer> teamId;
    // private ArrayList<String>[] coe;
    private HashMap<Integer, Integer> revr;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeams = Integer.parseInt(in.readLine());

        /*
            4
                          w   l  r  gij
            Atlanta       83 71  8  0 1 6 1
            Philadelphia  80 79  3  1 0 0 2
            New_York      78 78  6  6 0 0 0
            Montreal      77 82  3  1 2 0 0
         */

        teams = new String[numberOfTeams];
        win = new int[numberOfTeams];
        loss = new int[numberOfTeams];
        rem = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];
        teamId = new HashMap<>();
        // coe = new ArrayList[numberOfTeams];
        //
        // for (int i = 0; i < coe.length; i++)
        //     coe[i] = new ArrayList<>();

        for (int i = 0; i < numberOfTeams; i++) {
            if (in.hasNextLine()) {
                String[] str = in.readLine().trim().split("\\s+");
                teams[i] = str[0];
                teamId.put(teams[i], i);
                win[i] = Integer.parseInt(str[1]);
                loss[i] = Integer.parseInt(str[2]);
                rem[i] = Integer.parseInt(str[3]);

                for (int j = 0; j < numberOfTeams; j++)
                    g[i][j] = Integer.parseInt(str[j + 4]);
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamId.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teamId.containsKey(team))
            throw new IllegalArgumentException("Invalid team name !");
        return win[teamId.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teamId.containsKey(team))
            throw new IllegalArgumentException("Invalid team name !");
        return loss[teamId.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamId.containsKey(team))
            throw new IllegalArgumentException("Invalid team name !");
        return rem[teamId.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamId.containsKey(team1) || !teamId.containsKey(team2))
            throw new IllegalArgumentException("One of the team names is invalid !");
        return g[teamId.get(team1)][teamId.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teamId.containsKey(team))
            throw new IllegalArgumentException("Invalid team name !");

        int id = teamId.get(team);

        /* trivial break i.e. winning remaining games still
         puts us behind any of the others  */

        for (int i = 0; i < numberOfTeams; i++)
            if (win[id] + rem[id] < win[i])
                return true;

        // non trivial - build flow network to decide

        FlowNetwork fn = buildFlowNetwork(team);
        // System.out.println(fn);
        int lt = ((numberOfTeams - 1) * (numberOfTeams - 2)) / 2;
        int r = numberOfTeams - 1;
        int v = lt + r + 2;
        FordFulkerson ff = new FordFulkerson(fn, 0, v - 1);

        // the r to t vertices which are in mincut are responsible for elimination

        for (int i = lt + 1; i < v - 1; i++) {
            if (ff.inCut(i))
                return true;
        }

        return false;
    }

    private FlowNetwork buildFlowNetwork(String team) {

        int lt = ((numberOfTeams - 1) * (numberOfTeams - 2)) / 2;
        int r = numberOfTeams - 1;
        int v = lt + r + 2;

        FlowNetwork fn = new FlowNetwork(v);
        int s = 0, t = v - 1;
        // HashMap<String, Integer> map = new HashMap<>();
        HashMap<Integer, String> left = new HashMap<>();
        HashMap<Integer, Integer> right = new HashMap<>();
        revr = new HashMap<>();
        int tid = teamId.get(team); // id of the team in question
        int id = 1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == tid)
                continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == tid)
                    continue;
                left.put(id++, i + " " + j);
            }
        }

        for (int i = 0; i < numberOfTeams; i++) {
            if (i == tid)
                continue;

            right.put(i, id);
            revr.put(id++, i);
        }

        // System.out.println(left + "\n" + right + "\n" + revr);
        // System.out.println(" l = " + lt + " r = " + r + " v = " + v);
        // System.out.println("t = " + t);


        // s to left
        for (int i = 1; i <= lt; i++) {
            String[] str = left.get(i).trim().split("\\s+");
            int from = Integer.parseInt(str[0]), to = Integer.parseInt(str[1]);
            double capacity = g[from][to];

            FlowEdge fl = new FlowEdge(s, i, capacity);
            fn.addEdge(fl);
        }

        // l to r

        for (int i = 1; i <= lt; i++) {
            String[] str = left.get(i).trim().split("\\s+");
            int from = right.get(Integer.parseInt(str[0])), to = right
                    .get(Integer.parseInt(str[1]));
            FlowEdge fl = new FlowEdge(i, from, Double.POSITIVE_INFINITY);
            fn.addEdge(fl);
            fl = new FlowEdge(i, to, Double.POSITIVE_INFINITY);
            fn.addEdge(fl);
        }

        // r to t


        for (int x : right.keySet()) {
            double capacity = win[teamId.get(team)] + rem[teamId.get(team)] -
                    win[x];
            // if (capacity < 0) {
            //     System.out.println(win[teamId.get(team)] + " " + rem[teamId.get(team)] + " " +
            //                                win[x]);
            //     System.out.println("neg edge = " + x + " val = " + right.get(x));
            // }
            FlowEdge fl = new FlowEdge(right.get(x), t, capacity);
            fn.addEdge(fl);
        }
        return fn;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        List<String> ls = new ArrayList<>();
        if (!isEliminated(team))
            return null;

        else {
            // trivial
            if (!teamId.containsKey(team))
                throw new IllegalArgumentException("Invalid team name !");

            int id = teamId.get(team);

        /* trivial break i.e. winning remaining games still
         puts us behind any of the others  */

            for (int i = 0; i < numberOfTeams; i++)
                if (win[id] + rem[id] < win[i]) {
                    ls.add(teams[i]);
                    return ls;
                }

            // non trivial case

            FlowNetwork fn = buildFlowNetwork(team);
            // System.out.println(fn);
            int lt = ((numberOfTeams - 1) * (numberOfTeams - 2)) / 2;
            int r = numberOfTeams - 1;
            int v = lt + r + 2;
            FordFulkerson ff = new FordFulkerson(fn, 0, v - 1);

            // the r to t vertices which are in mincut are responsible for elimination

            for (int i = lt + 1; i < v - 1; i++) {
                if (ff.inCut(i)) {
                    ls.add(teams[revr.get(i)]);
                }
            }
            return ls;
        }
    }

    // tester
    /* public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }*/
}
