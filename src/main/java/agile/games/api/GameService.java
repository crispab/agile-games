package agile.games.api;

import agile.games.tts.GameSession;
import agile.games.tts.GameSessionId;
import agile.games.tts.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class GameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private Map<String, UUID> userSessions = new HashMap<>();
    private Map<GameSessionId, GameSession> gameSessions = new HashMap<>();

    public String registerUser(String webSocketId) {
        LOG.info("Register user: {}", webSocketId);
        UUID uuid = UUID.randomUUID();
        userSessions.put(webSocketId, uuid);
        return uuid.toString();
    }

    public MessageResponse facilitate() {
        LOG.info("Facilitate");
        GameSession gameSession = new GameSession();
        UserId userId = gameSession.newUser();
        gameSession.setFacilitator(userId);
        GameSessionId gameSessionId = gameSession.getId();
        gameSessions.put(gameSessionId, gameSession);
        return MessageResponse.ok();
    }

    public MessageResponse join() {
        LOG.info("Join");
        return MessageResponse.failed("Can't find game");
    }
}
