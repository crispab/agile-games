package agile.games.api;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings({"unused"})
public class MessageResponse implements Message {

    private MessageType messageType;
    private String message;
    private Map<ParameterKey, String> parameters = new EnumMap<>(ParameterKey.class);

    private Status status;

    public static MessageResponse ok(MessageType messageType) {
        MessageResponse result = new MessageResponse();
        result.status = Status.OK;
        result.messageType = messageType;
        return result;
    }

    public static MessageResponse failed(String message) {
        MessageResponse result = new MessageResponse();
        result.status = Status.FAIL;
        result.message = message;
        return result;
    }

    public MessageResponse put(ParameterKey key, String value) {
        parameters.put(key, value);
        return this;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Map<ParameterKey, String> getParameters() {
        return parameters;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setParameters(Map<ParameterKey, String> parameters) {
        this.parameters = parameters;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public enum MessageType {
        JOINED,
        FACILITATE,
        SESSION_START,
        RESUME
    }

    public enum ParameterKey {
        USER_SESSION_ID,
        GAME_SESSION_CODE,
        ROOM
    }
}
