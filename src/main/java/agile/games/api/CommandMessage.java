package agile.games.api;

public class CommandMessage {
    public enum CommandType {
        FACILITATE,
        JOIN
    }

    private CommandType commandType;
    private UserSessionId userSessionId;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public UserSessionId getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(UserSessionId userSessionId) {
        this.userSessionId = userSessionId;
    }
}
