package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

import static agile.games.api.MessageResponse.MessageType.*;
import static agile.games.api.MessageResponse.ParameterKey.*;
import static agile.games.api.RoomType.Facilitator;
import static agile.games.api.RoomType.Lobby;
import static agile.games.api.Status.STATE;

@Singleton
public class GameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private Map<UserSessionId, WebSocketId> userSessionsSockets = new HashMap<>();
    private Map<WebSocketId, GameSessionCode> socketSessions = new HashMap<>();
    private Map<WebSocketId, PlayerId> playerSessions = new HashMap<>();
    private Map<GameSessionCode, GameSession> gameSessions = new HashMap<>();

    public MessageResponse registerUser(String webSocketId) {
        UserSessionId userSessionId = new UserSessionId();
        LOG.info("Register user: {} {}", webSocketId, userSessionId);
        userSessionsSockets.put(userSessionId, new WebSocketId(webSocketId));
        return MessageResponse.ok(SESSION_START).put(USER_SESSION_ID, userSessionId.toString());
    }

    public MessageResponse tryResume(String webSocketIdStr, UserSessionId sessionId) {
        WebSocketId oldWebSocketId = userSessionsSockets.get(sessionId);
        if (oldWebSocketId == null) {
            return registerUser(webSocketIdStr);
        } else {
            LOG.info("Resume user {} {}", webSocketIdStr, sessionId);
            WebSocketId newWebSocketId = new WebSocketId(webSocketIdStr);
            userSessionsSockets.put(sessionId, newWebSocketId);
            GameSessionCode gameSessionCode = socketSessions.get(oldWebSocketId);
            if (gameSessionCode == null) {
                return MessageResponse
                        .ok(RESUME)
                        .put(USER_SESSION_ID, sessionId.toString())
                        .put(ROOM, Lobby.toString());
            }
            socketSessions.remove(oldWebSocketId);
            socketSessions.put(newWebSocketId, gameSessionCode);
            PlayerId playerId = playerSessions.get(oldWebSocketId);
            if (playerId != null) {
                LOG.info("Resuming player {}", playerId);
                playerSessions.remove(oldWebSocketId);
                playerSessions.put(newWebSocketId, playerId);
            }
            GameSession gameSession = gameSessions.get(gameSessionCode);
            return MessageResponse
                    .ok(RESUME)
                    .put(USER_SESSION_ID, sessionId.toString())
                    .put(ROOM, playerId == null ? Facilitator.toString() : RoomType.Player.toString())
                    .put(GAME_SESSION_CODE, gameSession.getCode().toString());
        }
    }

    public MessageResponse facilitate(String webSocketIdStr) {
        LOG.info("Facilitate: {}", webSocketIdStr);
        GameSession gameSession = new GameSession();
        UserId userId = gameSession.newUser();
        gameSession.setFacilitator(userId);
        GameSessionCode gameSessionCode = gameSession.getCode();
        gameSessions.put(gameSessionCode, gameSession);
        socketSessions.put(new WebSocketId(webSocketIdStr), gameSessionCode);
        return MessageResponse.ok(FACILITATE).put(GAME_SESSION_CODE, gameSessionCode.toString());
    }

    public MessageResponse join(GameSessionCode gameSessionCode, PlayerName playerName, String webSocketId) {
        LOG.info("Join {} {}", gameSessionCode, playerName);
        if (playerName.getName().length() < 2) {
            return MessageResponse.failed("Name must be at least 2 characters");
        }
        GameSession gameSession = gameSessions.get(gameSessionCode);
        if (gameSession == null) {
            return MessageResponse.failed("Can't find game " + gameSessionCode);
        }
        UserId userId = gameSession.newUser();
        PlayerId playerId = gameSession.addPlayerNamed(playerName.getName(), userId);
        WebSocketId id = new WebSocketId(webSocketId);
        socketSessions.put(id, gameSessionCode);
        playerSessions.put(id, playerId);
        return MessageResponse.ok(JOINED).put(GAME_SESSION_CODE, gameSessionCode.toString());
    }

    public List<String> socketSessions(GameSessionCode gameSessionCode) {
        return socketSessions.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(gameSessionCode))
                .map(Map.Entry::getKey)
                .map(Objects::toString)
                .collect(Collectors.toList());
    }

    public Message gameState(GameSessionCode gameSessionCode) {
        GameSession gameSession = gameSessions.get(gameSessionCode);
        if (gameSession != null) {
            GameStateMessage gameStateMessage = new GameStateMessage();
            gameStateMessage.setStatus(STATE);
            GameStateMessage.InnerState innerState = new GameStateMessage.InnerState();
            innerState.setPhase(GamePhase.GATHERING);
            innerState.setPlayers(gameSession.getPlayerNames());
            gameStateMessage.setGameState(innerState);
            return gameStateMessage;
        }
        return MessageResponse.failed("Invalid game session id " + gameSessionCode);
    }

    public Optional<GameSessionCode> leave(String webSocketIdStr) {
        WebSocketId webSocketId = new WebSocketId(webSocketIdStr);
        LOG.info("Leave: {}", webSocketId);
        Optional<GameSessionCode> gameSessionId = findGameSessionId(webSocketId);
        if (gameSessionId.isPresent()) {
            Optional<GameSession> gameSession = findGameSession(gameSessionId.get());
            Optional<PlayerId> playerId = findPlayerId(webSocketId);
            playerId.ifPresent(id -> gameSession.map(g -> g.removePlayer(id)));
        }

        return gameSessionId;
    }

    private Optional<GameSessionCode> findGameSessionId(WebSocketId webSocketId) {
        return Optional.ofNullable(socketSessions.get(webSocketId));
    }

    private Optional<GameSession> findGameSession(GameSessionCode key) {
        return Optional.ofNullable(gameSessions.get(key));
    }

    private Optional<PlayerId> findPlayerId(WebSocketId webSocketId) {
        return Optional.ofNullable(playerSessions.get(webSocketId));
    }
}
