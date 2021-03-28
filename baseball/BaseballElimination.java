/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public final class BaseballElimination {

    private final int n;
    private final int size;
    private final HashMap<String, int[]> teams;
    private final String[] teamKeys;
    private SET<String> eliminatedBy;

    public BaseballElimination(String filename) {
        In file = new In(filename);
        this.n = Integer.parseInt(file.readLine());

        teams = new HashMap<String, int[]>();
        teamKeys = new String[n];

        // Create integer array for wins, losses, remaining games, and team index.
        this.size = 4 + n;
        int teamIdx = 0;
        int teamName;
        while (file.hasNextLine()) {
            String[] row = file.readLine().split("\\s+");

            // Check for whitespace in front of team name.
            if (row[0].isEmpty()) {
                teamName = 1;
            }
            else { teamName = 0; }

            // Create arrays for each team.
            teams.put(row[teamName], new int[size]);

            // Fill the arrays with stats.
            for (int idx = 0; idx < size - 1; idx++) {
                teams.get(row[teamName])[idx] = Integer.parseInt(row[teamName + idx + 1]);
            }
            // Add name of team to team keys array.
            teamKeys[teamIdx] = row[teamName];

            // Create a team index within the team array for reverse index lookup.
            teams.get(row[teamName])[size - 1] = teamIdx++;
        }
    }

    private void teamChecker(String team) {
        if (!teams.containsKey(team)) { throw new IllegalArgumentException("Given team does not exist."); }
    }

    public int numberOfTeams() { return n; }

    public Iterable<String> teams() {
        return new HashSet<String>(teams.keySet());
    }

    public int wins(String team) {
        teamChecker(team);
        return teams.get(team)[0];
    }

    public int losses(String team) {
        teamChecker(team);
        return teams.get(team)[1];
    }

    public int remaining(String team) {
        teamChecker(team);
        return teams.get(team)[2];
    }

    public int against(String team1, String team2) {
        teamChecker(team1);
        teamChecker(team2);

        // Add 3 to bypass wins, losses, and remaining games indices.
        return teams.get(team1)[3 + teams.get(team2)[size - 1]];
    }

    private void trivialElimination(String team) {
        for (String otherTeam : teams()) {
            if (otherTeam.equals(team)) { continue; }
            // if team's wins and remaining games are less than any other team's wins,
            // then team is eliminated.
            if (teams.get(team)[0] + teams.get(team)[2] < teams.get(otherTeam)[0]) {
                eliminatedBy.add(otherTeam);
            }
        }
    }

    private FlowNetwork buildGraph(int numRemainingTeams, int numGames, String team) {
        // Create flow network with source and sink.
        int numVertices = 2 + numGames + numRemainingTeams;
        int source = numVertices - 2;
        int sink = numVertices - 1;
        FlowNetwork graph = new FlowNetwork(numVertices);

        // Use separate integers to track the current game vertex and the team vertices to connect.
        int gameTracker = 0;
        int teamTracker = 0;
        int otherTeamTracker = teamTracker + 1;

        // Add game edges from source and edges from games to teams.
        outer: for (int i = 0; i < n - 1; i++) {
            if (i == teams.get(team)[size - 1]) { continue; }
            for (int j = i + 1; j < n; j++) {
                if (j == teams.get(team)[size - 1]) { continue; }

                graph.addEdge(new FlowEdge(source, gameTracker, against(teamKeys[i], teamKeys[j])));
                graph.addEdge(new FlowEdge(gameTracker, teamTracker + numGames, Double.POSITIVE_INFINITY));
                graph.addEdge(new FlowEdge(gameTracker, otherTeamTracker + numGames, Double.POSITIVE_INFINITY));

                // Increment the current game vertex and the other team vertex but not the current team vertex
                // as i has not yet changed.
                gameTracker++;
                otherTeamTracker++;
            }
            // Increment the current team vertex and reset the other team vertex to match.
            teamTracker++;
            otherTeamTracker = teamTracker + 1;
        }

        // Separately track the team vertex as it appears in the team keys reverse index.
        // This needs to be done as there are 1 fewer team vertices in graph than number of teams.
        int teamKey = 0;
        // Add edges from team vertices to the sink.
        for (int teamVertex = numGames; teamVertex < source; teamVertex++) {
            if (teamKey == teams.get(team)[size - 1]) { teamKey++; }
            graph.addEdge(
                    new FlowEdge(teamVertex,
                                 sink,
                                 (
                                  // Current team's (that we're checking for elimination)
                                  // wins and remaining games minus other team's wins.
                                  teams.get(team)[0] + teams.get(team)[2]
                                  - teams.get(teamKeys[teamKey++])[0]
                                 )
                    )
            );
        }

        return graph;
    }

    public boolean isEliminated(String team) {
        teamChecker(team);
        // Create a set to store subset R teams.
        eliminatedBy = new SET<String>();

        // Carry out trivial elimination.
        trivialElimination(team);
        if (!eliminatedBy.isEmpty()) {
            return true;
        }

        // Triangular sum of games without team being checked for elimination..
        int numRemainingTeams = n - 1;
        int numGames = ((numRemainingTeams) * (numRemainingTeams - 1) / 2);
        FlowNetwork graph = buildGraph(numRemainingTeams, numGames, team);

        // Find maxflow/mincut of graph.
        FordFulkerson maxFlow = new FordFulkerson(graph, graph.V() - 2, graph.V() - 1);

        // Keep track of the team key in reverse index due to differences
        // in number of vertices in graph vs. number of teams in total.
        int teamKey = 0;
        // Find subset of teams that caused elimination.
        for (int teamVertex = numGames; teamVertex < graph.V() - 2; teamVertex++) {
            if (teamKey == teams.get(team)[size - 1]) { teamKey++; }
            if (maxFlow.inCut(teamVertex)) {
                eliminatedBy.add(teamKeys[teamKey]);
            }
            teamKey++;
        }

        return (!eliminatedBy.isEmpty());
    }

    public Iterable<String> certificateOfElimination(String team) {
        teamChecker(team);
        if (!isEliminated(team)) { return null; }

        return new SET<String>(eliminatedBy);
    }


    public static void main(String[] args) {
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

    }
}
