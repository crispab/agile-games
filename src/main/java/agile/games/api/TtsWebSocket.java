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

import java.util.UUID;

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
    public Publisher<String> onOpen(WebSocketSession session) {
        LOG.info("Joiner {}", session.getId());

        String gameSession = UUID.randomUUID().toString();
        session.send(gameSession);
        return broadcaster.broadcast(gameSession);
    }

    @OnMessage
    public Publisher<MessageResponse> onMessage(CommandMessage message, WebSocketSession session) {
        switch (message.getCommandType()) {
            case FACILITATE:
                gameService.facilitate();
                return session.send(MessageResponse.ok());
            case JOIN:
                return session.send(MessageResponse.ok());
            default:
                return session.send(MessageResponse.failed());
        }
    }

    @OnClose
    public Publisher<String> onClose(WebSocketSession session) {

        return broadcaster.broadcast("Closed " + session.getId());
    }
}
