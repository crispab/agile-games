package agile.games.api;

public class PlayerDto {
    private String id;
    private String name;
    private String avatar;
    private PlayerGoalsDto goals;

    private PlayerDto() {
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public static IName id(String id) {
        return new Builder(id);
    }

    public PlayerGoalsDto getGoals() {
        return goals;
    }

    public interface IName {
        IAvatar name(String name);
    }

    public interface IAvatar {
        IGoals avatar(String avatar);
    }

    public interface IGoals {
        IBuild goals(PlayerGoalsDto goalsDto);
    }

    public interface IBuild {
        PlayerDto build();
    }

    public static class Builder implements IName, IAvatar, IGoals, IBuild {
        private final PlayerDto instance;

        public Builder(String id) {
            this.instance = new PlayerDto();
            instance.id = id;
        }

        @Override
        public IAvatar name(String name) {
            instance.name = name;
            return this;
        }

        @Override
        public IGoals avatar(String avatar) {
            instance.avatar = avatar;
            return this;
        }

        @Override
        public IBuild goals(PlayerGoalsDto goalsDto) {
            instance.goals = goalsDto;
            return this;
        }

        @Override
        public PlayerDto build() {
            return instance;
        }
    }
}

