package agile.games.api;

import agile.games.tts.PlayerId;

public class PlayerTapGoalDto {
    private String targetPlayerId;
    private PlayerGoalDto goal;

    public static IBuild goal(PlayerGoalDto goalDto) {
        return new Builder(goalDto);
    }

    public String getTargetPlayerId() {
        return targetPlayerId;
    }

    public PlayerGoalDto getGoal() {
        return goal;
    }

    public interface IBuild {
        PlayerTapGoalDto build();

        IBuild targetPlayerId(PlayerId targetPlayerId);
    }

    public static class Builder implements IBuild {
        private final PlayerTapGoalDto instance;

        public Builder(PlayerGoalDto goalDto) {
            instance = new PlayerTapGoalDto();
            instance.goal = goalDto;
        }

        @Override
        public PlayerTapGoalDto build() {
            return instance;
        }

        @Override
        public IBuild targetPlayerId(PlayerId targetPlayerId) {
            instance.targetPlayerId = targetPlayerId == null ? null : targetPlayerId.toString();
            return this;
        }
    }

}

