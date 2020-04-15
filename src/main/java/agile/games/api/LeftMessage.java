package agile.games.api;

import agile.games.tts.GameSessionCode;


public class LeftMessage implements Message {

    private final String left;

    private final GameSessionCode gameSessionCode;

    public LeftMessage(GameSessionCode gameSessionCode) {
        this.gameSessionCode = gameSessionCode;
        this.left = gameSessionCode.toString();
    }

    public LeftMessage() {
        this(new GameSessionCode(""));
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return gameSessionCode;
    }

    public String getLeft() {
        return left;
    }
}

