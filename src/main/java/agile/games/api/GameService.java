package agile.games.api;

import agile.games.tts.GameSession;
import agile.games.tts.GameSessionId;
import agile.games.tts.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

import static agile.games.api.MessageResponse.MessageType.FACILITATE;
import static agile.games.api.MessageResponse.ParameterKey.GAME_SESSION_ID;

@Singleton
public class GameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private Map<String, UserSessionId> userSessions = new HashMap<>();
    private Map<GameSessionId, GameSession> gameSessions = new HashMap<>();

    public UserSessionId registerUser(String webSocketId) {
        UserSessionId userSessionId = new UserSessionId();
        userSessions.put(webSocketId, userSessionId);
        LOG.info("Register user: {}", userSessionId);
        return userSessionId;
    }

    public MessageResponse facilitate() {
        LOG.info("Facilitate");
        GameSession gameSession = new GameSession();
        UserId userId = gameSession.newUser();
        gameSession.setFacilitator(userId);
        GameSessionId gameSessionId = gameSession.getId();
        gameSessions.put(gameSessionId, gameSession);
        return MessageResponse.ok(FACILITATE).put(GAME_SESSION_ID, gameSessionId.toString());
    }

    public MessageResponse join() {
        LOG.info("Join");
        return MessageResponse.failed("Can't find game");
    }
}
