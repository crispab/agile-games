package agile.games;

import agile.games.tts.PlayerGoalState;
import agile.games.tts.PlayerId;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static agile.games.GameStepUtilities.getGameSession;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayersGoalState {
    private static final Logger LOG = LoggerFactory.getLogger(PlayersGoalState.class);

    @Then("the player named {string} first goal is in state {string}")
    public void thePlayerSFirstGoalIsInState(String name, String state) {
        checkState(name, state, 0);
    }

    @Then("the player named {string} second goal is in state {string}")
    public void thePlayerSSecondGoalIsInState(String name, String state) {
        checkState(name, state, 1);
    }

    @Then("the player named {string} end goal is in state {string}")
    public void thePlayerSEndGoalIsInState(String name, String state) {
        checkState(name, state, 2);
    }

    protected void checkState(String name, String state, int i) {
        PlayerId playerId = getGameSession().findPlayerByName(name);
        List<PlayerGoalState> states = getGameSession().getPlayerGoalStates(playerId);
        PlayerGoalState expected = PlayerGoalState.valueOf(state.toUpperCase().replace(" ", "_"));
        assertEquals(expected, states.get(i));
    }
}

