package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.GameSessionCode;
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
        GameSessionCode gameSessionCode = createGame(gameService);

        MessageResponse response = gameService.join(
                gameSessionCode,
                new PlayerName(BAD_NAME),
                SOME_WEB_SOCKET_ID);

        assertEquals(Status.FAIL, response.getStatus());
        assertEquals("Name must be at least 2 characters", response.getMessage());
    }

    @Test
    void given_wrong_id_when_join_then_fail() {
        GameService gameService = new GameService();

        MessageResponse response = goodJoin(gameService, new GameSessionCode(SOME_SESSION));

        assertEquals(Status.FAIL, response.getStatus());
    }

    @Test
    void given_correct_name_and_id_when_join_then_ok() {
        GameService gameService = new GameService();
        GameSessionCode gameSessionCode = createGame(gameService);
        goodJoin(gameService, gameSessionCode);

        GameStateMessage message = (GameStateMessage) gameService.gameState(gameSessionCode);

        assertEquals(Status.STATE, message.getStatus());
        List<String> players = message.getGameState().getPlayers();
        assertEquals(1, players.size());
        assertEquals(GOOD_NAME, players.get(0));
    }

    private MessageResponse goodJoin(GameService gameService, GameSessionCode gameSessionCode) {
        return gameService.join(
                gameSessionCode,
                    new PlayerName(GOOD_NAME),
                    SOME_WEB_SOCKET_ID);
    }

    private GameSessionCode createGame(GameService gameService) {
        MessageResponse facilitate = gameService.facilitate(SOME_WEB_SOCKET_ID);
        return new GameSessionCode(facilitate.getParameters().get(GAME_SESSION_ID));
    }
}