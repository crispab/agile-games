package agile.games.tts;

import java.util.Objects;
import java.util.UUID;

public class AbstractId {
    public final String id;

    public AbstractId() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerId playerId = (PlayerId) o;
        return id.equals(playerId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}