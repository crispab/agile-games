package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.GameSessionCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServiceTest {

    private static final String SOME_SESSION = "some session";
    private static final String SOME_WEB_SOCKET_ID = "some web socket id";
    private static final String GOOD_NAME = "xyz";
    private static final String BAD_NAME = "x";

    @Test
    void given_player_name_that_is_too_short_when_join_then_fail() {
        GameService gameService = new GameService();
        GameSessionCode gameSessionCode = createGame(gameService);

        Message response = gameService.join(
                gameSessionCode,
                new PlayerName(BAD_NAME),
                SOME_WEB_SOCKET_ID);

        assertEquals("Name must be at least 2 characters", ((FailMessage) response).getFail().getFailMessage());
    }

    @Test
    void given_incorrect_code_when_join_then_fail() {
        GameService gameService = new GameService();

        Message response = doJoin(gameService, new GameSessionCode(SOME_SESSION));

        assertTrue(response instanceof FailMessage);
    }

    @Test
    void given_correct_name_and_id_when_join_then_ok() {
        GameService gameService = new GameService();
        GameSessionCode gameSessionCode = createGame(gameService);
        doJoin(gameService, gameSessionCode);

        GameStateMessage message = (GameStateMessage) gameService.gameState(gameSessionCode);

        List<PlayerDto> players = message.getGameState().getPlayers();
        assertEquals(1, players.size());
        assertEquals(GOOD_NAME, players.get(0).getName());
    }

    private Message doJoin(GameService gameService, GameSessionCode gameSessionCode) {
        return gameService.join(
                gameSessionCode,
                new PlayerName(GOOD_NAME),
                SOME_WEB_SOCKET_ID);
    }

    private GameSessionCode createGame(GameService gameService) {
        Message facilitate = gameService.facilitate(SOME_WEB_SOCKET_ID);
        return facilitate.gameSessionCode();
    }
}