package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.GameSessionId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static agile.games.api.MessageResponse.ParameterKey.GAME_SESSION_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameServiceTest {

    private static final String SOME_SESSION = "some session";
    private static final String SOME_WEB_SOCKET_ID = "some web socket id";
    private static final String GOOD_NAME = "xyz";
    private static final String BAD_NAME = "x";

    @Test
    void given_player_name_that_is_too_short_when_join_then_fail() {
        GameService gameService = new GameService();
        GameSessionId gameSessionId = createGame(gameService);

        MessageResponse response = gameService.join(
                gameSessionId,
                new PlayerName(BAD_NAME),
                SOME_WEB_SOCKET_ID);

        assertEquals(Status.FAIL, response.getStatus());
        assertEquals("Name must be at least 2 characters", response.getMessage());
    }

    @Test
    void given_wrong_id_when_join_then_fail() {
        GameService gameService = new GameService();

        MessageResponse response = goodJoin(gameService, new GameSessionId(SOME_SESSION));

        assertEquals(Status.FAIL, response.getStatus());
    }

    @Test
    void given_correct_name_and_id_when_join_then_ok() {
        GameService gameService = new GameService();
        GameSessionId gameSessionId = createGame(gameService);
        goodJoin(gameService, gameSessionId);

        GameStateMessage message = (GameStateMessage) gameService.gameState(gameSessionId);

        assertEquals(Status.STATE, message.getStatus());
        List<String> players = message.getGameState().getPlayers();
        assertEquals(1, players.size());
        assertEquals(GOOD_NAME, players.get(0));
    }

    private MessageResponse goodJoin(GameService gameService, GameSessionId gameSessionId) {
        return gameService.join(
                    gameSessionId,
                    new PlayerName(GOOD_NAME),
                    SOME_WEB_SOCKET_ID);
    }

    private GameSessionId createGame(GameService gameService) {
        MessageResponse facilitate = gameService.facilitate(SOME_WEB_SOCKET_ID);
        return new GameSessionId(facilitate.getParameters().get(GAME_SESSION_ID));
    }
}