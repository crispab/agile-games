package agile.games.tts;

import java.util.Objects;
import java.util.UUID;

public class AbstractId {
    private final String id;

    public AbstractId() {
        this.id = UUID.randomUUID().toString();
    }

    protected AbstractId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractId abstractId = (AbstractId) o;
        return id.equals(abstractId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
