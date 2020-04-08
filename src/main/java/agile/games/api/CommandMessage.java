package agile.games.api;

public class CommandMessage {
    public enum CommandType {
        FACILITATE,
        JOIN
    }

    private CommandType commandType;

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}
