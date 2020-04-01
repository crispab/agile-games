package agile.games.tts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameSession {
    private static final Logger LOG = LoggerFactory.getLogger(GameSession.class);

    private GamePhase gamePhase;
    private Map<PlayerId, Player> players = new HashMap<>();
    private Board board;

    public GameSession() {
        this(10, 10);
    }

    public GameSession(int x, int y) {
        this.gamePhase = GamePhase.GATHERING;
        this.board = new Board(x, y);
    }

    public PlayerId addPlayer() {
        Player player = new Player(0, 0, board);
        players.put(player.getId(), player);
        return player.getId();
    }

    public void start() {
        if (gamePhase == GamePhase.ASSIGNMENT) {
            gamePhase = GamePhase.EXECUTING;
        }
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void assignTargets() {
        this.setGamePhase(GamePhase.ASSIGNMENT);
    }

    public void placePlayerAt(PlayerId playerId, int x, int y) {
        players.get(playerId).setX(x).setY(y);
    }

    public void movePlayer(PlayerId playerId, Direction direction) {
        players.get(playerId).move(direction);
    }

    public PlayerPosition getPlayerPosition(PlayerId playerId) {
        return players.get(playerId).getPosition();
    }

    public Set<PlayerId> getPlayers() {
        return players.keySet();
    }

    public void setPlayerState(PlayerId playerId, PlayerState playerState) {
        players.get(playerId).setState(playerState);
        emit(GameEvent.NEW_PLAYER_STATE);
    }

    private void emit(GameEvent gameEvent) {
        LOG.info("Emitted event {}", gameEvent);
        switch (gameEvent) {
            case GAME_OVER:
                setGamePhase(GamePhase.REPORTING);
                break;
            case NEW_PLAYER_STATE:
                checkIfAllDone();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gameEvent);
        }
    }

    private void checkIfAllDone() {
        for (Player player : players.values()) {
            if (!player.getState().equals(PlayerState.DONE)) {
                return;
            }
        }
        emit(GameEvent.GAME_OVER);
    }
}
