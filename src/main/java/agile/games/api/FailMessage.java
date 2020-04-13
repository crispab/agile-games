package agile.games.api;

import agile.games.tts.GameSessionCode;

public class FailMessage implements Message {

    private final FailInfo fail;

    public FailMessage(String failMessage) {
        fail = new FailInfo();
        fail.failMessage = failMessage;
    }

    public FailInfo getFail() {
        return fail;
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return null;
    }

    public static class FailInfo {
        private String failMessage;

        public String getFailMessage() {
            return failMessage;
        }
    }
}

