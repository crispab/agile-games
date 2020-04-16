package agile.games.tts;

import agile.games.api.PlayerDto;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    private static final String NAME = "test";

    @Test
    void given_board_with_players_when_removing_player_then_remove_from_board() {
        GameSession gameSession = new GameSession();
        PlayerId playerId = gameSession.addPlayerNamed(NAME, gameSession.newUser());

        PlayerPosition playerPosition = gameSession.getPlayerPosition(playerId);
        Optional<PlayerDto> playerAt = gameSession.getPlayerAt(playerPosition);
        if (playerAt.isPresent()) {
            assertEquals("test", playerAt.get().getName());
        } else {
            fail();
        }

        gameSession.removePlayer(playerId);

        assertFalse(gameSession.getPlayerAt(playerPosition).isPresent());
    }
}