package agile.games.api;

import agile.games.tts.GameSessionCode;

public class FacilitateMessage implements Message {

    private final FacilitateInfo facilitate;

    public FacilitateMessage(GameSessionCode gameSessionCode) {
        facilitate = new FacilitateInfo();
        facilitate.gameSessionCode = gameSessionCode;
    }

    public FacilitateInfo getFacilitate() {
        return facilitate;
    }

    @Override
    public GameSessionCode gameSessionCode() {
        return facilitate.gameSessionCode;
    }

    public static class FacilitateInfo {
        private GameSessionCode gameSessionCode;

        public String getGameSessionCode() {
            return gameSessionCode.toString();
        }
    }
}

