package agile.games.tts;

import java.util.Random;

public class GameSessionId  {
    private static Random random = new Random();
    private final String id;

    public GameSessionId() {
        int nextInt = random.nextInt(999999);
        String tmpStr = String.format("%06d", nextInt);
        id = tmpStr.substring(0,3) + " " + tmpStr.substring(3);
    }

    @Override
    public String toString() {
        return id;
    }
}
