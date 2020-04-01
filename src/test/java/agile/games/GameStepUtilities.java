package agile.games;

import agile.games.tts.GameSession;
import agile.games.tts.PlayerId;

public class GameStepUtilities {
    protected static GameSession gameSession;
    protected static PlayerId playerId;

    private GameStepUtilities() {
        // Utility class
    }

    protected static GameSession getGameSession() {
        if (gameSession == null) {
            gameSession = new GameSession();
        }
        return gameSession;
    }

    protected static GameSession createGameSession(int width, int height) {
        gameSession = new GameSession(width, height);
        return gameSession;
    }
}
