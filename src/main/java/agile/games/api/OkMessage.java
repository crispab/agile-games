package agile.games.api;

import agile.games.tts.GameSessionCode;

public class OkMessage implements Message {

    private final OkInfo ok;
    private GameSessionCode gameSessionCode;

    public OkMessage(String okMessage) {
        ok = new OkInfo();
        ok.okMessage = okMessage;
    }

    public OkMessage gameSessionCode(GameSessionCode gameSessionCode) {
        this.gameSessionCode = gameSessionCode;
        return this;
    }

    public OkInfo getOk() {
        return ok;
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return gameSessionCode;
    }

    public static class OkInfo {
        private String okMessage;

        public String getOkMessage() {
            return okMessage;
        }
    }
}

