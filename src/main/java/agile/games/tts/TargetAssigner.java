package agile.games.tts;

import java.util.List;
import java.util.Random;

public class TargetAssigner {

    private static final int MAX_ATTEMPTS = 10000;
    private static Random random = new Random();

    private TargetAssigner() {
        // Utility class
    }

    public static void assignTargets(List<Player> playerList) {
        for (Player player : playerList) {
            for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
                Player target1 = pickRandomlyFromList(random, playerList);
                if (!target1.getId().equals(player.getId())) {
                    assignGoal1(player, target1.getId());
                    break;
                }
            }
            for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
                Player target2 = pickRandomlyFromList(random, playerList);
                if (!target2.getId().equals(player.getId()) && !target2.getId().equals(player.getGoal1())) {
                    assignGoal2(player, target2.getId());
                    break;
                }
            }
        }
    }

    public static void assignGoal1(Player player, PlayerId goal) {
        player.setGoal1(goal);
    }
    public static void assignGoal2(Player player, PlayerId goal) {
        player.setGoal2(goal);
    }

    private static Player pickRandomlyFromList(Random random, List<Player> playerList) {
        return playerList.get(random.nextInt(playerList.size()));
    }
}
