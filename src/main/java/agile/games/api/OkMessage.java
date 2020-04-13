package agile.games.api;

import agile.games.tts.GameSessionCode;

public class OkMessage implements Message {

    private final OkInfo ok;

    public OkMessage(String okMessage) {
        ok = new OkInfo();
        ok.okMessage = okMessage;
    }

    public OkInfo getOk() {
        return ok;
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return null;
    }

    public static class OkInfo {
        private String okMessage;

        public String getOkMessage() {
            return okMessage;
        }
    }
}

