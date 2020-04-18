package agile.games.api;

public class PlayerEndGoalDto {
    private int targetX;
    private int targetY;
    private PlayerGoalDto goal;

    public static ITargetX goal(PlayerGoalDto goal) {
        return new Builder(goal);
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public PlayerGoalDto getGoal() {
        return goal;
    }

    public interface ITargetX {
        ITargetY targetX(int x);
    }

    public interface ITargetY {
        IBuild targetY(int y);
    }

    public interface IBuild{
        PlayerEndGoalDto build();
    }

    public static class Builder implements ITargetX, ITargetY, IBuild {
        private final PlayerEndGoalDto instance;

        public Builder(PlayerGoalDto goal) {
            instance = new PlayerEndGoalDto();
            instance.goal = goal;
        }

        @Override
        public ITargetY targetX(int x) {
            instance.targetX = x;
            return this;
        }

        @Override
        public IBuild targetY(int y) {
            instance.targetY = y;
            return this;
        }

        @Override
        public PlayerEndGoalDto build() {
            return instance;
        }
    }
}

