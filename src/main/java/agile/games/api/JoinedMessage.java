package agile.games.api;

import agile.games.tts.GameSessionCode;

public class JoinedMessage implements Message {

    private final JoinedInfo joined;

    public JoinedMessage() {
        joined = new JoinedInfo();
    }

    public static IPlayerName gameSessionCode(GameSessionCode gameSessionCode) {
        return new Builder(gameSessionCode);
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return joined.gameSessionCode;
    }

    public JoinedInfo getJoined() {
        return joined;
    }

    interface IPlayerName {
        IPlayerAvatar playerName(String playerName);
    }

    interface IPlayerAvatar {
        IBuild playerAvatar(String playerAvatar);
    }

    interface IBuild {
        JoinedMessage build();
    }

    public static class JoinedInfo {
        private GameSessionCode gameSessionCode;
        private String playerName;
        private String playerAvatar;

        public GameSessionCode getGameSessionCode() {
            return gameSessionCode;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getPlayerAvatar() {
            return playerAvatar;
        }
    }

    public static class Builder implements IPlayerName, IPlayerAvatar, IBuild {

        private JoinedMessage joinedMessage;

        public Builder(GameSessionCode gameSessionCode) {
            joinedMessage = new JoinedMessage();
            joinedMessage.joined.gameSessionCode = gameSessionCode;
        }

        @Override
        public IPlayerAvatar playerName(String playerName) {
            joinedMessage.joined.playerName = playerName;
            return this;
        }

        @Override
        public IBuild playerAvatar(String playerAvatar) {
            joinedMessage.joined.playerAvatar = playerAvatar;
            return this;
        }

        @Override
        public JoinedMessage build() {
            return joinedMessage;
        }
    }
}

