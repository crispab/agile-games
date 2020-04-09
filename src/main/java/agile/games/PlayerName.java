package agile.games;

public class PlayerName {
    private final String name;

    public PlayerName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
