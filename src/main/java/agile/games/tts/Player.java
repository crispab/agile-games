package agile.games.tts;

import java.util.UUID;

class Player {
    private final PlayerId id;
    private final Board board;
    private PlayerState state;
    private PlayerPosition position;

    public Player(int x, int y, Board board) {
        this.id = new PlayerId(UUID.randomUUID().toString());
        this.position = new PlayerPosition(x, y);
        this.board = board;
        this.state = PlayerState.INTIAL;
    }

    public int getX() {
        return position.getX();
    }

    public Player setX(int x) {
        if(x > 0 && x < board.getX()) {
            this.position = new PlayerPosition(x, getY());
        }
        return this;
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        if(y > 0 && y < board.getY()) {
            this.position = new PlayerPosition(getX(), y);
        }
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
        switch (direction){
            case UP:
                setY(this.getY() - 1);
                break;
            case DOWN:
                setY(this.getY() + 1);
                break;
            case RIGHT:
                setX(this.getX() + 1);
                break;
            case LEFT:
                setX(this.getX() - 1);
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
}
