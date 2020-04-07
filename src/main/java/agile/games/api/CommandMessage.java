package agile.games.api;

public class CommandMessage {
    private CommandType commandType;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public enum CommandType {
        FACILITATE,
        JOIN
    }
}
