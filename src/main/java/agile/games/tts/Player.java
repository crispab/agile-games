package agile.games.tts;

class Player {
    private final PlayerId id;
    private final Board board;
    private final String name;
    private final String avatar;
    private PlayerState state;
    private PlayerPosition position;
    private PlayerEndGoal endGoal;
    private PlayerTapGoal goal1;
    private PlayerTapGoal goal2;

    public Player(String name, int x, int y, Board board, String avatar) {
        this.id = new PlayerId();
        this.board = board;
        board.movePlayerTo(x, y, this);
        this.state = PlayerState.INITIAL;
        this.name = name;
        this.avatar = avatar;
        this.goal1 = new PlayerTapGoal();
        this.goal2 = new PlayerTapGoal(goal1);
        this.endGoal = new PlayerEndGoal(position, goal2);
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
            this.setSteps1(this.getSteps1() + steps);
        }
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public PlayerEndGoal getEndGoal() {
        return endGoal;
    }

    public PlayerTapGoal getGoal1() {
        return goal1;
    }

    public void setGoal1(PlayerId goal1) {
        getGoal1().setTapGoal(goal1);
    }

    public PlayerTapGoal getGoal2() {
        return goal2;
    }

    public void setGoal2(PlayerId goal2) {
        getGoal2().setTapGoal(goal2);
    }

    public boolean isTargetingGoal1() {
        return getGoal1().getState() != PlayerGoalState.COMPLETED;
    }

    public boolean isTargetingGoal2() {
        return !isTargetingGoal1() && getGoal2().getState() != PlayerGoalState.COMPLETED;
    }

    public boolean isTargetingEndGoal() {
        return !isTargetingGoal1() && !isTargetingGoal2() && getEndGoal().getState() != PlayerGoalState.COMPLETED;
    }

    public void setEstimation1(int estimation1) {
        this.goal1.setEstimation(estimation1);
        checkIfAllEstimationsDone();
    }

    public void setEstimation2(int estimation2) {
        this.goal2.setEstimation(estimation2);
        checkIfAllEstimationsDone();
    }

    public void setEstimation3(int estimation3) {
        this.endGoal.setEstimation(estimation3);
        checkIfAllEstimationsDone();
    }

    private void checkIfAllEstimationsDone() {
        if (getState() == PlayerState.INITIAL &&
                getGoal1().getState() == PlayerGoalState.ESTIMATED &&
                getGoal2().getState() == PlayerGoalState.ESTIMATED &&
                getEndGoal().getState() == PlayerGoalState.ESTIMATED)
            setState(PlayerState.ESTIMATION_COMPLETED);
    }

    public void setRemainingEstimations() {
        if (getGoal1().getState() != PlayerGoalState.ESTIMATED) {
            getGoal1().setEstimation(0);
        }
        if (getGoal2().getState() != PlayerGoalState.ESTIMATED) {
            getGoal2().setEstimation(0);
        }
        if (getEndGoal().getState() != PlayerGoalState.ESTIMATED) {
            getEndGoal().setEstimation(0);
        }
        checkIfAllEstimationsDone();
    }

    public int getSteps1() {
        return getGoal1().getSteps();
    }

    public void setSteps1(int steps1) {
        getGoal1().setSteps(steps1);
    }

    public void checkGoal1(Player goal1Player) {
        getGoal1().check(this.position, goal1Player);
    }

    public void checkGoal2(Player goal1Player) {
        getGoal2().check(this.position, goal1Player);
    }

    public void checkEndGoal() {
        getEndGoal().check(this.position);
        if (getEndGoal().getState() == PlayerGoalState.COMPLETED) {
            this.state = PlayerState.DONE;
        }
    }
}
