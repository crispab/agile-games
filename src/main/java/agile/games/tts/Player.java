package agile.games.tts;

class Player {
    private final PlayerId id;
    private final Board board;
    private PlayerState state;
    private PlayerPosition position;

    public Player(int x, int y, Board board) {
        this.id = new PlayerId();
        this.board = board;
        board.movePlayerTo(x, y, this);
        this.state = PlayerState.INTIAL;
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
}
