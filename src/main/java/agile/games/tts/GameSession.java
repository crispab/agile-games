package agile.games.tts;

import agile.games.api.PlayerDto;
import agile.games.api.SquareDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class GameSession {
    private static final Logger LOG = LoggerFactory.getLogger(GameSession.class);

    private GamePhase gamePhase;
    private Map<UserId, User> users = new HashMap<>();
    private Map<PlayerId, Player> players = new HashMap<>();
    private Board board;
    private final GameSessionCode code;
    private final AvatarAssigner avatarAssigner;

    public GameSession() {
        this(5, 5);
    }

    public GameSession(int x, int y) {
        gamePhase = GamePhase.GATHERING;
        board = new Board(x, y);
        code = new GameSessionCode();
        avatarAssigner = new AvatarAssigner();
    }

    public void addPlayer(UserId userId) {
        addPlayerNamed("test", userId);
    }

    public PlayerId addPlayerNamed(String name, UserId userId) {
        PlayerPosition p = board.getRandomFreeSquare();
        return addPlayerAt(name, userId, p.getX(), p.getY());
    }

    public PlayerId addPlayerAt(UserId userId, int x, int y) {
        return addPlayerAt("test", userId, x, y);
    }

    public PlayerId addPlayerAt(String name, UserId userId, int x, int y) {
        User user = findUserById(userId);
        user.setAsPlayer();
        Player player = new Player(name, x, y, board, avatarAssigner.nextAvatar());
        players.put(player.getId(), player);
        return player.getId();
    }

    public void placePlayerAt(PlayerId playerId, int x, int y) {
        Player player = findPlayerById(playerId);
        Player otherPlayer = board.getPlayerAt(x, y);
        if (otherPlayer == null) {
            board.movePlayerTo(x, y, player);
        } else {
            board.swapPlayerPosition(player, otherPlayer);
        }
    }

    public String getRow(int row) {
        return board.getRowAsString(row);
    }

    public void start() {
        if (gamePhase == GamePhase.ASSIGNMENT && allPlayersDoneEstimating()) {
            gamePhase = GamePhase.EXECUTING;
        }
    }

    private boolean allPlayersDoneEstimating() {
        return players.values().stream().noneMatch(p -> p.getState() == PlayerState.INITIAL);
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void assignTargets() {
        this.setGamePhase(GamePhase.ASSIGNMENT);
        TargetAssigner.assignTargets(new ArrayList<>(this.players.values()));
    }

    private Player findPlayerById(PlayerId playerId) {
        return players.get(playerId);
    }

    public void movePlayer(PlayerId playerId, Direction direction) {
        if (gamePhase == GamePhase.EXECUTING) {
            findPlayerById(playerId).move(direction);
            emit(GameEvent.PLAYER_MAY_HAVE_MOVED);
        }
    }

    public PlayerPosition getPlayerPosition(PlayerId playerId) {
        return findPlayerById(playerId).getPosition();
    }

    public Set<PlayerId> getPlayers() {
        return players.keySet();
    }

    public void setPlayerState(PlayerId playerId, PlayerState playerState) {
        findPlayerById(playerId).setState(playerState);
        emit(GameEvent.NEW_PLAYER_STATE);
    }

    public void setPlayerGoals(PlayerId playerId, PlayerId goal1, PlayerId goal2) {
        TargetAssigner.assignGoal1(findPlayerById(playerId), goal1);
        TargetAssigner.assignGoal2(findPlayerById(playerId), goal2);
    }

    public UserId newUser() {
        User user = new User();
        users.put(user.getUserId(), user);
        return user.getUserId();
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
            case PLAYER_MAY_HAVE_MOVED:
                checkTargets();
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

    private void checkTargets() {
        for (Player player : players.values()) {
            if (player.isTargetingGoal1()) {
                checkGoal1(player);
            }
            if (player.isTargetingGoal2()) {
                checkGoal2(player);
            }
            if (player.isTargetingEndGoal()) {
                checkEndGoal(player);
            }
        }
    }

    private void checkGoal1(Player player) {
        Player goal1Player = findPlayerById(player.getGoal1());
        if (player.getPosition().distance(goal1Player.getPosition()) <= 1) {
            player.setGoal1(null);
        }
    }

    private void checkGoal2(Player player) {
        Player goal2Player = findPlayerById(player.getGoal2());
        if (player.getPosition().distance(goal2Player.getPosition()) <= 1) {
            player.setGoal2(null);
        }
    }

    private void checkEndGoal(Player player) {
        if (player.getEndGoal().equals(player.getPosition())) {
            setPlayerState(player.getId(), PlayerState.DONE);
        }
    }

    public void setFacilitator(UserId userId) {
        findUserById(userId).setAsFacilitator();
    }

    public UserRole getUserRole(UserId userId) {
        return findUserById(userId).getUserRole();
    }

    private User findUserById(UserId userId) {
        return users.get(userId);
    }

    public PlayerPosition getPlayerEndGoal(PlayerId playerId) {
        Player playerById = findPlayerById(playerId);
        return playerById.getEndGoal();
    }

    public PlayerId findPlayerByName(String playerName) {
        Optional<Player> player = players.values()
                .stream()
                .filter(p -> p.getName().equals(playerName))
                .findFirst();
        if (player.isPresent()) {
            return player.get().getId();
        }
        throw new IllegalArgumentException("No player named: '" + playerName + "'.");
    }

    public PlayerId getPlayerGoal1(PlayerId playerId) {
        Player player = findPlayerById(playerId);
        return player.getGoal1();
    }

    public PlayerId getPlayerGoal2(PlayerId playerId) {
        Player player = findPlayerById(playerId);
        return player.getGoal2();
    }

    public PlayerState getPlayerState(PlayerId playerId) {
        return findPlayerById(playerId).getState();
    }

    public void setPlayerEstimation1(PlayerId playerId, int estimation1) {
        Player player = findPlayerById(playerId);
        player.setEstimation1(estimation1);
    }

    public void setPlayerEstimation2(PlayerId playerId, int estimation2) {
        Player player = findPlayerById(playerId);
        player.setEstimation2(estimation2);
    }

    public void setPlayerEstimation3(PlayerId playerId, int estimation3) {
        Player player = findPlayerById(playerId);
        player.setEstimation3(estimation3);
    }

    public void setRemainingEstimations() {
        players.values().forEach(Player::setRemainingEstimations);
    }

    public int getPlayerSteps1(PlayerId playerId) {
        Player player = findPlayerById(playerId);
        return player.getSteps1();
    }

    public GameSessionCode getCode() {
        return code;
    }

    public String getPlayerName(PlayerId playerId) {
        Player player = findPlayerById(playerId);
        return player.getName();
    }

    public String getPlayerAvatar(PlayerId playerId) {
        Player player = findPlayerById(playerId);
        return player.getAvatar();
    }

    public List<String> getPlayerNames() {
        return players.values().stream().map(Player::getName).collect(Collectors.toList());
    }

    public PlayerId removePlayer(PlayerId playerId) {
        return players.remove(playerId).getId();
    }

    public List<List<SquareDto>> getBoard() {
        return board.getSquaresAsListList().stream().map(this::toDtos).collect(Collectors.toList());
    }

    private List<SquareDto> toDtos(List<Square> squares) {
        return squares.stream().map(this::toDto).collect(Collectors.toList());
    }

    private SquareDto toDto(Square square) {
        return new SquareDto(toPlayerDto(square.getPlayer()));
    }

    private PlayerDto toPlayerDto(Player player) {
        return player == null ? null : new PlayerDto(player.getName(), player.getAvatar());
    }
}
