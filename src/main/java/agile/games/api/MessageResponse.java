package agile.games.api;

public class MessageResponse {

    private Status status;

    public static MessageResponse ok() {
        MessageResponse result = new MessageResponse();
        result.status = Status.OK;
        return result;
    }

    public static MessageResponse failed() {
        MessageResponse result = new MessageResponse();
        result.status = Status.FAIL;
        return result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public enum Status {
        OK, FAIL
    }
}
