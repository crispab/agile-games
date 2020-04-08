package agile.games.api;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static agile.games.api.MessageResponse.MessageType.SESSION_START;
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
        LOG.info("Joiner {}", session.getId());
        String userSessionId = gameService.registerUser(session.getId());
        return session.send(MessageResponse.ok(SESSION_START).put(USER_SESSION_ID, userSessionId));
    }

    @OnMessage
    public Publisher<MessageResponse> onMessage(CommandMessage message, WebSocketSession session) {
        switch (message.getCommandType()) {
            case FACILITATE:
                return session.send(gameService.facilitate());
            case JOIN:
                return session.send(gameService.join());
            default:
                return session.send(MessageResponse.failed("Unknown command." + message.getCommandType()));
        }
    }

    @OnClose
    public Publisher<String> onClose(WebSocketSession session) {
        return broadcaster.broadcast("Closed " + session.getId());
    }
}
