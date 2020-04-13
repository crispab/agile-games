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

import static agile.games.api.MessageResponse.ParameterKey.DIRECTION;
import static agile.games.api.MessageResponse.ParameterKey.GAME_SESSION_CODE;
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
    public Publisher<MessageResponse> onOpen(WebSocketSession session) {
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
                return respondAndNewState(session, move(session, commandMessage));
            case PHASE_GATHERING:
                return respondAndNewState(session, gotoPhase(session, GATHERING));
            case PHASE_ASSIGNMENT:
                return respondAndNewState(session, gotoPhase(session, ASSIGNMENT));
            case PHASE_EXECUTING:
                return respondAndNewState(session, gotoPhase(session, EXECUTING));
            case PHASE_REPORTING:
                return respondAndNewState(session, gotoPhase(session, REPORTING));
            default:
                return session.send(MessageResponse.failed("Unknown command." + commandMessage.getCommandType()));
        }
    }

    @OnClose
    public Publisher<Message> onClose(WebSocketSession session) {
        LOG.info("Closed socket {}", session.getId());
        return broadcaster.broadcast(noMessage(), toNoOne());
    }

    private MessageResponse tryResume(WebSocketSession session, UserSessionId userSessionId) {
        return gameService.tryResume(session.getId(), userSessionId);
    }

    private MessageResponse move(WebSocketSession session, CommandMessage commandMessage) {
        return gameService.move(session.getId(), commandMessage.getParameters().get(DIRECTION.toString()));
    }

    private MessageResponse gotoPhase(WebSocketSession session, GamePhase gamePhase) {
        return gameService.gotoPhase(session.getId(), gamePhase);
    }

    private Publisher<Message> respondAndNewState(WebSocketSession session, MessageResponse messageResponse) {
        session.sendSync(messageResponse);
        GameSessionCode gameSessionCode = new GameSessionCode(messageResponse.getParameters().get(GAME_SESSION_CODE));
        return broadcastNewState(gameSessionCode);
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

    private Message noMessage() {
        return new MessageResponse();
    }

    private Predicate<WebSocketSession> toNoOne() {
        return (s -> false);
    }
}
