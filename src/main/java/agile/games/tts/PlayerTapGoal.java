package agile.games.tts;

public class PlayerTapGoal extends PlayerGoal {

    private PlayerId tapGoal;

    public PlayerTapGoal() {
        super(null);
    }

    public PlayerTapGoal(PlayerTapGoal previous) {
        super(previous);
    }

    public PlayerId getTapGoal() {
        return tapGoal;
    }

    public void setTapGoal(PlayerId tapGoal) {
        this.tapGoal = tapGoal;
        assigned();
    }

    public void check(PlayerPosition position, Player goal1Player) {
        if (goal1Player == null || position.distance(goal1Player.getPosition()) <= 1) {
            completed();
        }
    }
}

