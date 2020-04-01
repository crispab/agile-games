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
                    player.setGoal1(target1.getId());
                    break;
                }
            }
            for (int attempts = 0; attempts <= MAX_ATTEMPTS; attempts++) {
                Player target2 = pickRandomlyFromList(random, playerList);
                if (!target2.getId().equals(player.getId()) && !target2.getId().equals(player.getGoal1())) {
                    player.setGoal2(target2.getId());
                    break;
                }
            }
        }
    }

    private static Player pickRandomlyFromList(Random random, List<Player> playerList) {
        return playerList.get(random.nextInt(playerList.size()));

    }
}
