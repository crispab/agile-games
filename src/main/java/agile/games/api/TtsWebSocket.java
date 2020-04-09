package agile.games.api;

import agile.games.tts.GameSessionId;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

import static agile.games.api.MessageResponse.MessageType.SESSION_START;
import static agile.games.api.MessageResponse.ParameterKey.GAME_SESSION_ID;
import static agile.games.api.MessageResponse.ParameterKey.USER_SESSION_ID;

@ServerWebSocket("/ws/tts")
public class TtsWebSocket {
    private static final Logger LOG = LoggerFactory.getLogger(TtsWebSocket.class);

    private WebSocketBroadcaster broadcaster;
    private GameService gameService;

    public TtsWebSocket(WebSocketBroadcaster broadcaster, GameService gameService) {
        this.broadcaster = broadcaster;
        this.gameService = gameService;
    }

    @OnOpen
    public Publisher<MessageResponse> onOpen(WebSocketSession session) {
        LOG.info("New session: {}", session.getId());
        UserSessionId userSessionId = gameService.registerUser(session.getId());
        return session.send(MessageResponse.ok(SESSION_START).put(USER_SESSION_ID, userSessionId.toString()));
    }

    @OnMessage
    public Publisher<Message> onMessage(CommandMessage commandMessage, WebSocketSession session) {
        switch (commandMessage.getCommandType()) {
            case FACILITATE:
                return respondAndNewState(session, gameService.facilitate(session.getId()));
            case JOIN:
                GameSessionId gameSessionId = new GameSessionId(commandMessage.getParameters().get("gameSessionId"));
                return respondAndNewState(session, gameService.join(gameSessionId, session.getId()));
            default:
                return session.send(MessageResponse.failed("Unknown command." + commandMessage.getCommandType()));
        }
    }

    @OnClose
    public Publisher<String> onClose(WebSocketSession session) {
        return broadcaster.broadcast("Closed " + session.getId());
    }

    private Publisher<Message> respondAndNewState(WebSocketSession session, MessageResponse messageResponse) {
        session.sendSync(messageResponse);
        GameSessionId gameSessionId = new GameSessionId(messageResponse.getParameters().get(GAME_SESSION_ID));
        List<String> socketSessions = gameService.socketSessions(gameSessionId);
        return broadcaster.broadcast(gameService.gameState(gameSessionId), isInGame(socketSessions));
    }

    private Predicate<WebSocketSession> isInGame(List<String> sessions) {
        return s -> sessions.contains(s.getId());
    }
}
