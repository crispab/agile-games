package agile.games.api;

public class PlayerGoalsDto {
    private PlayerTapGoalDto goal1;
    private PlayerTapGoalDto goal2;
    private PlayerEndGoalDto endGoal;

    public static IGoal2 goal1(PlayerTapGoalDto goal1) {
        return new Builder(goal1);
    }

    public PlayerTapGoalDto getGoal1() {
        return goal1;
    }

    public PlayerTapGoalDto getGoal2() {
        return goal2;
    }

    public PlayerEndGoalDto getEndGoal() {
        return endGoal;
    }

    public interface IGoal2 {
        IEndGoal goal2(PlayerTapGoalDto goalDto);
    }

    public interface IEndGoal {
        IBuild endGoal(PlayerEndGoalDto endGoalDto);
    }

    public interface IBuild {
        PlayerGoalsDto build();
    }

    public static class Builder implements IGoal2, IEndGoal, IBuild {

        private final PlayerGoalsDto instance;

        public Builder(PlayerTapGoalDto goal1) {
            instance = new PlayerGoalsDto();
            instance.goal1 = goal1;
        }

        @Override
        public IEndGoal goal2(PlayerTapGoalDto goalDto) {
            instance.goal2 = goalDto;
            return this;
        }

        @Override
        public IBuild endGoal(PlayerEndGoalDto endGoalDto) {
            instance.endGoal = endGoalDto;
            return this;
        }

        @Override
        public PlayerGoalsDto build() {
            return instance;
        }
    }
}

