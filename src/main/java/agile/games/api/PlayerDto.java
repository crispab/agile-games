package agile.games.api;

public class PlayerDto {
    private String id;
    private String name;
    private String avatar;

    private PlayerDto() {
    }

    public static IName id(String id) {
        return new Builder(id);
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

    public interface IName {
        IAvatar name(String name);
    }

    public interface IAvatar {
        IBuild avatar(String avatar);
    }

    public interface IBuild {
        PlayerDto build();
    }

    public static class Builder implements IName, IAvatar, IBuild {
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
        public IBuild avatar(String avatar) {
            instance.avatar = avatar;
            return this;
        }

        @Override
        public PlayerDto build() {
            return instance;
        }
    }

}

