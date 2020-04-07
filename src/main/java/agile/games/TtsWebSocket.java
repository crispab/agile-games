package agile.games;

import agile.games.api.GameService;
import agile.games.api.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

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
    public Publisher<MessageResponse> onMessage(String message, WebSocketSession session) {
        try {
            Map map = new ObjectMapper().readValue(message, Map.class);
            if (map.get("commandType").equals("FACILITATE")) {
                gameService.facilitate();
            }
        } catch (JsonProcessingException e) {
            session.send(MessageResponse.failed());
        }
        return session.send(MessageResponse.ok());
    }

    @OnClose
    public Publisher<String> onClose(WebSocketSession session) {

        return broadcaster.broadcast("Closed " + session.getId());
    }

    private Predicate<WebSocketSession> isValid(String topic) {
        return s -> topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }
}
