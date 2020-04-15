package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class GameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private Map<UserSessionId, WebSocketId> userSessionsSockets = new HashMap<>();
    private Map<WebSocketId, GameSessionCode> socketSessions = new HashMap<>();
    private Map<WebSocketId, PlayerId> playerSessions = new HashMap<>();
    private Map<GameSessionCode, GameSession> gameSessions = new HashMap<>();

    public Message registerUser(String webSocketId) {
        UserSessionId userSessionId = new UserSessionId();
        LOG.info("Register user: {} {}", webSocketId, userSessionId);
        userSessionsSockets.put(userSessionId, new WebSocketId(webSocketId));
        return new SessionStartMessage(userSessionId);
    }

    public Message tryResume(String webSocketIdStr, UserSessionId sessionId) {
        WebSocketId oldWebSocketId = userSessionsSockets.get(sessionId);
        if (oldWebSocketId == null) {
            return registerUser(webSocketIdStr);
        } else {
            LOG.info("Resume user {} {}", webSocketIdStr, sessionId);
            WebSocketId newWebSocketId = new WebSocketId(webSocketIdStr);
            userSessionsSockets.put(sessionId, newWebSocketId);
            GameSessionCode gameSessionCode = socketSessions.get(oldWebSocketId);
            if (gameSessionCode == null) {
                return new OkMessage("Resumed session in lobby");
            }
            socketSessions.remove(oldWebSocketId);
            socketSessions.put(newWebSocketId, gameSessionCode);
            PlayerId playerId = playerSessions.get(oldWebSocketId);
            if (playerId == null) {
                LOG.info("Resume the facilitator.");
                return new FacilitateMessage(gameSessionCode);
            } else {
                GameSession gameSession = gameSessions.get(gameSessionCode);
                LOG.info("Resuming player {}", playerId);
                playerSessions.remove(oldWebSocketId);
                playerSessions.put(newWebSocketId, playerId);
                return JoinedMessage
                        .gameSessionCode(gameSessionCode)
                        .playerId(playerId.toString())
                        .playerName(gameSession.getPlayerName(playerId))
                        .playerAvatar(gameSession.getPlayerAvatar(playerId))
                        .build();
            }
        }
    }

    public GameSessionCode move(String webSocketIdStr, String direction) {
        WebSocketId webSocketId = new WebSocketId(webSocketIdStr);
        GameSessionCode gameSessionCode = socketSessions.get(webSocketId);
        PlayerId playerId = playerSessions.get(webSocketId);
        gameSessions.get(gameSessionCode).movePlayer(playerId, Direction.valueOf(direction));
        return gameSessionCode;
    }

    public GameSessionCode gotoPhase(String webSocketIdStr, GamePhase gamePhase) {
        GameSessionCode gameSessionCode = socketSessions.get(new WebSocketId(webSocketIdStr));
        gameSessions.get(gameSessionCode).setGamePhase(gamePhase);
        return gameSessionCode;
    }

    public Message facilitate(String webSocketIdStr) {
        LOG.info("Facilitate: {}", webSocketIdStr);
        GameSession gameSession = new GameSession(12, 12);
        UserId userId = gameSession.newUser();
        gameSession.setFacilitator(userId);
        GameSessionCode gameSessionCode = gameSession.getCode();
        gameSessions.put(gameSessionCode, gameSession);
        socketSessions.put(new WebSocketId(webSocketIdStr), gameSessionCode);
        return new FacilitateMessage(gameSessionCode);
    }

    public Message join(GameSessionCode gameSessionCode, PlayerName playerName, String webSocketId) {
        LOG.info("Join {} {}", gameSessionCode, playerName);
        if (playerName.getName().length() < 2) {
            return new FailMessage("Name must be at least 2 characters");
        }
        GameSession gameSession = gameSessions.get(gameSessionCode);
        if (gameSession == null) {
            return new FailMessage("Can't find game " + gameSessionCode);
        }

        UserId userId = gameSession.newUser();
        PlayerId playerId = gameSession.addPlayerNamed(playerName.getName(), userId);
        WebSocketId id = new WebSocketId(webSocketId);
        socketSessions.put(id, gameSessionCode);
        playerSessions.put(id, playerId);
        return JoinedMessage.gameSessionCode(gameSessionCode)
                .playerId(playerId.toString())
                .playerName(gameSession.getPlayerName(playerId))
                .playerAvatar(gameSession.getPlayerAvatar(playerId))
                .build();
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
            GameStateMessage.GameStateInfo gameStateInfo = new GameStateMessage.GameStateInfo();
            gameStateInfo.setGameSessionCode(gameSessionCode);
            gameStateInfo.setPhase(gameSession.getGamePhase());
            gameStateInfo.setPlayers(gameSession.getPlayerDtos());
            gameStateInfo.setBoard(gameSession.getBoard());
            gameStateMessage.setGameState(gameStateInfo);
            return gameStateMessage;
        }
        return new FailMessage("Invalid game session id " + gameSessionCode);
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
