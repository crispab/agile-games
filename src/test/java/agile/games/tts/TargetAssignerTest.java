package agile.games.tts;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TargetAssignerTest {

    @Test
    public void given_a_list_of_three_players_they_are_given_each_others_as_targets() {
        Board board = new Board(12, 12);
        List<Player> players = Arrays.asList(
                new Player("player 1", 0, 0, board, "avatar1.png"),
                new Player("player 2", 0, 1, board, "avatar2.png"),
                new Player("player 3", 0, 2, board, "avatar3.png")
        );

        TargetAssigner.assignTargets(players);

        checkTargets(players, 0, 1, 2);
        checkTargets(players, 1, 0, 2);
        checkTargets(players, 2, 0, 1);
    }

    protected void checkTargets(List<Player> players, int player, int target1, int target2) {
        boolean goal1isp1 = players.get(player).getGoal1().getTapGoal().equals(players.get(target1).getId());
        boolean goal1isp2 = players.get(player).getGoal1().getTapGoal().equals(players.get(target2).getId());
        boolean goal2isp1 = players.get(player).getGoal2().getTapGoal().equals(players.get(target1).getId());
        boolean goal2isp2 = players.get(player).getGoal2().getTapGoal().equals(players.get(target2).getId());
        assertTrue((goal1isp1 && goal2isp2) || (goal1isp2 && goal2isp1), "Failed for " + player);
    }

    @Test
    void random_distribution_should_be_fine() {
        Board board = new Board(15, 15);
        List<Player> players = generatePlayers(10, board);

        HashMap<PlayerId, Integer> distributionMap = new HashMap<>();
        int samples = 1000;
        for (int n = 0; n < samples; n++) {
            pickRandom(players, distributionMap);
        }
        long lower = Math.round(samples / 10.0 * 0.3);
        long upper = Math.round(samples / 10.0 * 1.3);
        List<Map.Entry<PlayerId, Integer>> collect = distributionMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() < lower || entry.getValue() > upper)
                .collect(Collectors.toList());

        assertEquals(0, collect.size(), "The distribution was not good.\n" + showDistribution(distributionMap));
    }

    @Test
    void distribution_of_random_players_is_fine() {
        Board board = new Board(15, 15);
        List<Player> players = generatePlayers(10, board);

        TargetAssigner.assignTargets(players);

        HashMap<PlayerId, Integer> distributionMap = new HashMap<>();
        players.forEach( p -> {
            count(p.getGoal1(), distributionMap);
            count(p.getGoal2(), distributionMap);
        });

        List<Map.Entry<PlayerId, Integer>> collect = distributionMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() < 1 || entry.getValue() > 5)
                .collect(Collectors.toList());

        assertEquals(0, collect.size(), "The distribution was not good.\n" + showDistribution(distributionMap));

    }

    private void count(PlayerTapGoal playerTapGoal, HashMap<PlayerId, Integer> distributionMap) {
        distributionMap.putIfAbsent(playerTapGoal.getTapGoal(), 0);
        distributionMap.put(playerTapGoal.getTapGoal(), distributionMap.get(playerTapGoal.getTapGoal()) + 1);
    }

    private String showDistribution(HashMap<PlayerId, Integer> distributionMap) {
        return distributionMap.entrySet().stream()
                .map(entry -> "" + entry.getKey() + ":\t " + entry.getValue() + "\n")
                .collect(Collectors.joining());
    }

    private void pickRandom(List<Player> players, HashMap<PlayerId, Integer> distributionMap) {
        PlayerId id = TargetAssigner.pickRandomlyFromList(players).getId();
        distributionMap.putIfAbsent(id, 0);
        distributionMap.put(id, distributionMap.get(id) + 1);
    }

    private List<Player> generatePlayers(int noPlayers, Board board) {
        List<Player> result = new ArrayList<>();
        for (int n = 0; n < noPlayers; n++) {
            result.add(new Player("player" + n, 0, n, board, "avatar" + n));
        }
        return result;
    }
}