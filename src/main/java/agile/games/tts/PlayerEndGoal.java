package agile.games.tts;

public class PlayerEndGoal extends PlayerGoal {
    private PlayerPosition goalPosition;

    public PlayerEndGoal(PlayerPosition goalPosition, PlayerGoal previous) {
        super(previous);
        this.goalPosition = goalPosition;
        assigned();
    }

    public PlayerPosition getGoalPosition() {
        return goalPosition;
    }


    public void check(PlayerPosition currentPosition) {
        if (currentPosition.equals(goalPosition)) {
            completed();
        }
    }

}

