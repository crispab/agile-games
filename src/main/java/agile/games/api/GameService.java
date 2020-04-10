package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static agile.games.api.MessageResponse.MessageType.FACILITATE;
import static agile.games.api.MessageResponse.MessageType.JOINED;
import static agile.games.api.MessageResponse.ParameterKey.GAME_SESSION_ID;
import static agile.games.api.Status.STATE;

@Singleton
public class GameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private Map<String, GameSessionId> socketSessions = new HashMap<>();
    private Map<String, PlayerId> playerSessions = new HashMap<>();
    private Map<GameSessionId, GameSession> gameSessions = new HashMap<>();

    public UserSessionId registerUser(String webSocketId) {
        UserSessionId userSessionId = new UserSessionId();
        LOG.info("Register user: {}", webSocketId);
        return userSessionId;
    }

    public MessageResponse facilitate(String webSocketId) {
        LOG.info("Facilitate: {}", webSocketId);
        GameSession gameSession = new GameSession();
        UserId userId = gameSession.newUser();
        gameSession.setFacilitator(userId);
        GameSessionId gameSessionId = gameSession.getId();
        gameSessions.put(gameSessionId, gameSession);
        socketSessions.put(webSocketId, gameSessionId);
        return MessageResponse.ok(FACILITATE).put(GAME_SESSION_ID, gameSessionId.toString());
    }

    public MessageResponse join(GameSessionId gameSessionId, PlayerName playerName, String webSocketId) {
        LOG.info("Join {} {}", gameSessionId, playerName);
        if (playerName.getName().length() < 2) {
            return MessageResponse.failed("Name must be at least 2 characters");
        }
        GameSession gameSession = gameSessions.get(gameSessionId);
        if (gameSession == null) {
            return MessageResponse.failed("Can't find game " + gameSessionId);
        }
        UserId userId = gameSession.newUser();
        PlayerId playerId = gameSession.addPlayerNamed(playerName.getName(), userId);
        socketSessions.put(webSocketId, gameSessionId);
        playerSessions.put(webSocketId, playerId);
        return MessageResponse.ok(JOINED).put(GAME_SESSION_ID, gameSessionId.toString());
    }

    public List<String> socketSessions(GameSessionId gameSessionId) {
        return socketSessions.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameSessionId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Message gameState(GameSessionId gameSessionId) {
        GameSession gameSession = gameSessions.get(gameSessionId);
        if (gameSession != null) {
            GameStateMessage gameStateMessage = new GameStateMessage();
            gameStateMessage.setStatus(STATE);
            GameStateMessage.InnerState innerState = new GameStateMessage.InnerState();
            innerState.setPhase(GamePhase.GATHERING);
            innerState.setPlayers(gameSession.getPlayerNames());
            gameStateMessage.setGameState(innerState);
            return gameStateMessage;
        }
        return MessageResponse.failed("Invalid game session id " + gameSessionId);
    }

    public GameSessionId leave(String webSocketId) {
        LOG.info("Leave: {}", webSocketId);
        GameSessionId gameSessionId = socketSessions.get(webSocketId);
        PlayerId playerId = playerSessions.get(webSocketId);
        GameSession gameSession = gameSessions.get(gameSessionId);
        gameSession.removePlayer(playerId);
        return gameSessionId;
    }
}
