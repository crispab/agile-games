package agile.games.tts;

class Player {
    private final PlayerId id;
    private final Board board;
    private final String name;
    private PlayerState state;
    private PlayerPosition position;
    private PlayerPosition endGoal;
    private PlayerId goal1;
    private PlayerId goal2;

    public Player(String name, int x, int y, Board board) {
        this.id = new PlayerId();
        this.board = board;
        board.movePlayerTo(x, y, this);
        this.state = PlayerState.INTIAL;
        this.endGoal = position;
        this.name = name;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public PlayerState getState() {
        return state;
    }

    public PlayerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void move(Direction direction) {
        switch (direction) {
            case UP:
                board.movePlayerTo(this.getX(), this.getY() - 1, this);
                break;
            case DOWN:
                board.movePlayerTo(this.getX(), this.getY() + 1, this);
                break;
            case RIGHT:
                board.movePlayerTo((this.getX() + 1), this.getY(), this);
                break;
            case LEFT:
                board.movePlayerTo((this.getX() - 1), this.getY(), this);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }

    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public void moveTo(int x, int y) {
        board.movePlayerTo(x, y, this);
    }

    public PlayerPosition getEndGoal() {
        return endGoal;
    }

    public PlayerId getGoal1() {
        return goal1;
    }

    public void setGoal1(PlayerId goal1) {
        this.goal1 = goal1;
    }

    public PlayerId getGoal2() {
        return goal2;
    }

    public void setGoal2(PlayerId goal2) {
        this.goal2 = goal2;
    }
}
