package agile.games.api;

public class SquareDto {
    private final PlayerDto player;

    public SquareDto(PlayerDto player) {
        this.player = player;
    }

    public PlayerDto getPlayer() {
        return player;
    }
}

