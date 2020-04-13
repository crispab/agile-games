package agile.games.tts;

class Player {
    private final PlayerId id;
    private final Board board;
    private final String name;
    private final String avatar;
    private PlayerState state;
    private PlayerPosition position;
    private PlayerPosition endGoal;
    private PlayerId goal1;
    private PlayerId goal2;
    private Integer estimation1;
    private Integer estimation2;
    private Integer estimation3;
    private int steps1;

    public Player(String name, int x, int y, Board board, String avatar) {
        this.id = new PlayerId();
        this.board = board;
        board.movePlayerTo(x, y, this);
        this.state = PlayerState.INITIAL;
        this.endGoal = position;
        this.name = name;
        this.avatar = avatar;
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

    public String getAvatar() {
        return avatar;
    }

    public int move(Direction direction) {
        int steps;
        switch (direction) {
            case UP:
                steps = board.movePlayerTo(this.getX(), this.getY() - 1, this);
                break;
            case DOWN:
                steps = board.movePlayerTo(this.getX(), this.getY() + 1, this);
                break;
            case RIGHT:
                steps = board.movePlayerTo((this.getX() + 1), this.getY(), this);
                break;
            case LEFT:
                steps = board.movePlayerTo((this.getX() - 1), this.getY(), this);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        addSteps(steps);
        return steps;
    }

    private void addSteps(int steps) {
        if (steps == 0) {
            return;
        }
        if (isTargetingGoal1()) {
            this.setSteps1(this.getSteps1()  + steps);
        }
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
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

    public boolean isTargetingGoal1() {
        return this.goal1 != null;
    }

    public boolean isTargetingGoal2() {
        return !isTargetingGoal1() && this.getGoal2() != null;
    }

    public boolean isTargetingEndGoal() {
        return !isTargetingGoal1() && !isTargetingGoal2();
    }

    public int getEstimation1() {
        return estimation1;
    }

    public void setEstimation1(int estimation1) {
        this.estimation1 = estimation1;
        checkIfAllEstimationsDone();
    }

    public int getEstimation2() {
        return estimation2;
    }

    public void setEstimation2(int estimation2) {
        this.estimation2 = estimation2;
        checkIfAllEstimationsDone();
    }

    public int getEstimation3() {
        return estimation3;
    }

    public void setEstimation3(int estimation3) {
        this.estimation3 = estimation3;
        checkIfAllEstimationsDone();
    }

    private void checkIfAllEstimationsDone() {
        if (getState() == PlayerState.INITIAL &&
                estimation1 != null &&
                estimation2 != null &&
                estimation3 != null) {
            setState(PlayerState.ESTIMATION_COMPLETED);
        }
    }

    public void setRemainingEstimations() {
        if (estimation1 == null) {
            estimation1 = 0;
        }
        if (estimation2 == null) {
            estimation2 = 0;
        }
        if (estimation3 == null) {
            estimation3 = 0;
        }
        checkIfAllEstimationsDone();
    }

    public int getSteps1() {
        return steps1;
    }

    public void setSteps1(int steps1) {
        this.steps1 = steps1;
    }
}
