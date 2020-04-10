package agile.games.api;

import java.util.Map;

public class CommandMessage {
    public enum CommandType {
        FACILITATE,
        JOIN,
        RESUME
    }

    private CommandType commandType;
    private UserSessionId userSessionId;
    private Map<String, String> parameters;

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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
