package agile.games.tts;

public abstract class PlayerGoal {

    private PlayerGoalState state = PlayerGoalState.NO_GOAL_SET;
    private Integer estimation = null;
    private Integer steps = 0;
    private PlayerGoal previous;

    public PlayerGoal(PlayerGoal previous) {
        this.previous = previous;
    }

    public PlayerGoalState getState() {
        return state;
    }

    public void setState(PlayerGoalState state) {
        this.state = state;
    }

    public Integer getEstimation() {
        return estimation;
    }

    public void setEstimation(Integer estimation) {
        this.estimation = estimation;
        state = PlayerGoalState.ESTIMATED;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    protected void assigned() {
        if (getState() == PlayerGoalState.NO_GOAL_SET) {
            setState(PlayerGoalState.ASSIGNED);
        }
    }
    protected void completed() {
        if (previous == null || previous.getState() == PlayerGoalState.COMPLETED) {
            setState(PlayerGoalState.COMPLETED);
        }
    }
}

