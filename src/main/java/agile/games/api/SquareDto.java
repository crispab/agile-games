package agile.games.api;

public class SquareDto {
    private final PlayerRefDto player;

    public SquareDto(PlayerRefDto playerRefDto) {
        this.player = playerRefDto;
    }

    public PlayerRefDto getPlayer() {
        return player;
    }

    public static class PlayerRefDto {
        private String id;
        private String name;
        private String avatar;

        public static IName id(String id) {
            return new Builder(id);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public interface IName {
            IAvatar name(String name);
        }

        public interface IAvatar {
            IBuild avatar(String avatar);
        }

        public interface IBuild {
            PlayerRefDto build();
        }

        public static class Builder implements IName, IAvatar, IBuild {

            private final PlayerRefDto instance;

            public Builder(String id) {
                instance = new PlayerRefDto();
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
            public PlayerRefDto build() {
                return instance;
            }
        }
    }

}

