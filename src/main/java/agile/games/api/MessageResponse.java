package agile.games.api;

public class MessageResponse {

    private String message;

    private Status status;

    public static MessageResponse failed(String message) {
        MessageResponse result = new MessageResponse();
        result.status = Status.FAIL;
        result.message = message;
        return result;
    }

    public static MessageResponse ok() {
        MessageResponse result = new MessageResponse();
        result.status = Status.OK;
        return result;
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

    public void setMessage(String message) {
        this.message = message;
    }

    public enum Status {
        OK, FAIL
    }
}
