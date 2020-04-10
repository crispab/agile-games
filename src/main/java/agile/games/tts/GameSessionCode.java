package agile.games.tts;

import java.util.Objects;
import java.util.Random;

public class GameSessionCode {
    private static Random random = new Random();
    private final String id;

    public GameSessionCode() {
        int nextInt = random.nextInt(999999);
        String tmpStr = String.format("%06d", nextInt);
        id = tmpStr.substring(0,3) + " " + tmpStr.substring(3);
    }

    public GameSessionCode(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameSessionCode that = (GameSessionCode) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
