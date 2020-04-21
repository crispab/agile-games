package agile.games.tts;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
}