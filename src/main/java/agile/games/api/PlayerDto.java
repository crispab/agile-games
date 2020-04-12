package agile.games.api;

public class PlayerDto {
    private final String name;
    private final String avatar;

    public PlayerDto(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}

