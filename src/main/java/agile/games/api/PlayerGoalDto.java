package agile.games.api;

import agile.games.tts.PlayerGoalState;

public class PlayerGoalDto {
    private PlayerGoalState state;
    private Integer estimation;
    private int steps;

    public static IEstimation state(PlayerGoalState state) {
        return new Builder(state);
    }

    public PlayerGoalState getState() {
        return state;
    }

    public Integer getEstimation() {
        return estimation;
    }

    public int getSteps() {
        return steps;
    }

    public interface IEstimation {
        ISteps estimation(Integer estimation);
    }

    public interface ISteps {
        IBuild steps(int steps);
    }

    public interface IBuild {
        PlayerGoalDto build();
    }

    public static class Builder implements IEstimation, ISteps, IBuild {
        private final PlayerGoalDto instance;

        public Builder(PlayerGoalState state) {
            instance = new PlayerGoalDto();
            instance.state = state;
        }

        @Override
        public ISteps estimation(Integer estimation) {
            instance.estimation = estimation;
            return this;
        }

        @Override
        public IBuild steps(int steps) {
            instance.steps = steps;
            return this;
        }

        @Override
        public PlayerGoalDto build() {
            return instance;
        }
    }
}

