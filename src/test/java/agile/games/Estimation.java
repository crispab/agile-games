package agile.games;

import agile.games.tts.PlayerId;
import agile.games.tts.PlayerState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;

import static agile.games.GameStepUtilities.getGameSession;

public class Estimation {

    private static final int ESTIMATION_1 = 1;
    private static final int ESTIMATION_2 = 2;
    private static final int ESTIMATION_3 = 3;

    @But("{string} has not estimated all their goals")
    public void hasNotEstimatedAllTheirGoals(String playerName) {
        PlayerId playerId = getGameSession().findPlayerByName(playerName);
        getGameSession().setPlayerState(playerId, PlayerState.INITIAL);
    }

    @And("{string} has done their estimation")
    public void hasDoneTheirEstimation(String playerName) {
        PlayerId playerId = getGameSession().findPlayerByName(playerName);
        getGameSession().setPlayerEstimation1(playerId, ESTIMATION_1);
        getGameSession().setPlayerEstimation2(playerId, ESTIMATION_2);
        getGameSession().setPlayerEstimation3(playerId, ESTIMATION_3);
    }
}
