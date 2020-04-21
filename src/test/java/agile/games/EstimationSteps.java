package agile.games;

import agile.games.tts.PlayerId;
import agile.games.tts.PlayerState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;

import static agile.games.GameStepUtilities.getGameSession;
import static agile.games.GameStepUtilities.playerId;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EstimationSteps {

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
        getGameSession().setPlayerEstimationEnd(playerId, ESTIMATION_3);
    }

    @And("all players have done their estimations")
    public void allPlayersHasDoneTheirEstimations() {
        getGameSession().setRemainingEstimations();
    }

    @Then("the player's steps for the first goal are {int}")
    public void thePlayerForFirstGoalStepsIs(int steps) {
        assertEquals(steps, getGameSession().getPlayerSteps1(playerId));
    }

    @Then("the player's steps for the second goal are {int}")
    public void thePlayersForSecondGoalStepsIs(int steps) {
        assertEquals(steps, getGameSession().getPlayerSteps2(playerId));
    }

    @Then("the player's steps for the end goal are {int}")
    public void thePlayersForEndGoalStepsIs(int steps) {
        assertEquals(steps, getGameSession().getPlayerEndSteps(playerId));
    }
}
