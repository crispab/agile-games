package agile.games.api;

import agile.games.PlayerName;
import agile.games.tts.GamePhase;
import agile.games.tts.GameSessionCode;
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

import static agile.games.api.CommandMessage.ParameterKey.DIRECTION;
import static agile.games.tts.GamePhase.*;

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
    public Publisher<Message> onOpen(WebSocketSession session) {
        LOG.info("New session: {}", session.getId());
        return session.send(gameService.registerUser(session.getId()));
    }

    @OnMessage
    public Publisher<Message> onMessage(CommandMessage commandMessage, WebSocketSession session) {
        switch (commandMessage.getCommandType()) {
            case FACILITATE:
                return respondAndNewState(session, gameService.facilitate(session.getId()));
            case JOIN:
                GameSessionCode gameSessionCode = new GameSessionCode(commandMessage.getParameters().get("gameSessionId"));
                PlayerName playerName = new PlayerName(commandMessage.getParameters().get("playerName"));
                return respondAndNewState(session, gameService.join(gameSessionCode, playerName, session.getId()));
            case RESUME:
                return respondAndNewState(session, tryResume(session, commandMessage.getUserSessionId()));
            case MOVE:
                return broadcastNewState(move(session, commandMessage));
            case LEAVE:
                return respondAndNewState(session, gameService.leave(session.getId()));
            case PHASE_GATHERING:
                return broadcastNewState(gotoPhase(session, GATHERING));
            case PHASE_ESTIMATION:
                return broadcastNewState(gotoPhase(session, ESTIMATION));
            case PHASE_EXECUTING:
                return broadcastNewState(gotoPhase(session, EXECUTING));
            case PHASE_REPORTING:
                return broadcastNewState(gotoPhase(session, REPORTING));
            case ESTIMATE:
                return respondAndNewState(session, estimate(session, commandMessage));
            default:
                return session.send(new FailMessage("Unknown command. " + commandMessage.getCommandType()));
        }

    }

    @OnClose
    public Publisher<Message> onClose(WebSocketSession session) {
        LOG.info("Closed socket {}", session.getId());
        return broadcaster.broadcast(new OkMessage("<to none>"), toNoOne());
    }

    private Message tryResume(WebSocketSession session, UserSessionId userSessionId) {
        return gameService.tryResume(session.getId(), userSessionId);
    }

    private Message estimate(WebSocketSession session, CommandMessage commandMessage) {
        try {
            int e1 = Integer.parseInt(commandMessage.getParameters().get("estimation1"));
            int e2 = Integer.parseInt(commandMessage.getParameters().get("estimation2"));
            int ee = Integer.parseInt(commandMessage.getParameters().get("estimationEnd"));
            return gameService.estimate(session.getId(), e1, e2, ee);
        } catch (NumberFormatException ne) {
            return new FailMessage("Wrong format, should be integer: " + ne.getMessage());
        }
    }

    private GameSessionCode move(WebSocketSession session, CommandMessage commandMessage) {
        return gameService.move(session.getId(), commandMessage.getParameters().get(DIRECTION.toString()));
    }

    private GameSessionCode gotoPhase(WebSocketSession session, GamePhase gamePhase) {
        return gameService.gotoPhase(session.getId(), gamePhase);
    }

    private Publisher<Message> respondAndNewState(WebSocketSession session, Message message) {
        session.sendSync(message);
        return broadcastNewState(message.gameSessionCode());
    }

    private Publisher<Message> broadcastNewState(GameSessionCode gameSessionCode) {
        return broadcaster.broadcast(
                gameService.gameState(gameSessionCode),
                isInGame(gameService.socketSessions(gameSessionCode))
        );
    }

    private Predicate<WebSocketSession> isInGame(List<String> sessions) {
        return s -> sessions.contains(s.getId());
    }

    private Predicate<WebSocketSession> toNoOne() {
        return (s -> false);
    }
}
