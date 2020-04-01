package agile.games;

import agile.games.tts.PlayerId;
import agile.games.tts.PlayerPosition;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static agile.games.GameStepUtilities.getGameSession;
import static agile.games.GameStepUtilities.playerId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameExecution {

    @Then("the player has end goal to reach {int},{int}")
    public void thePlayerHasEndGoalToReach(int x, int y) {
        PlayerPosition endPosition = getGameSession().getPlayerEndGoal(playerId);
        assertEquals(new PlayerPosition(x, y), endPosition);
    }

    @Given("a player at position {int},{int} named {string}")
    public void aPlayerAtPositionNamed(int x, int y, String name) {
        playerId = getGameSession().addPlayerAt(name, getGameSession().newUser(), x, y);
    }

    @And("another player at position {int},{int} named {string}")
    public void anotherPlayerAtPositionNamed(int x, int y, String name) {
        getGameSession().addPlayerAt(name, getGameSession().newUser(), x, y);
    }

    @Then("player named {string} has two goals {string} and {string}")
    public void playerNamedHasTwoGoalsAnd(String playerName, String targetName1, String targetName2) {
        PlayerId player = getGameSession().findPlayerByName(playerName);
        PlayerId target1 = getGameSession().findPlayerByName(targetName1);
        PlayerId target2 = getGameSession().findPlayerByName(targetName2);

        PlayerId goal1 = getGameSession().getPlayerGoal1(player);
        PlayerId goal2 = getGameSession().getPlayerGoal2(player);
        assertTrue(target1.equals(goal1) || target2.equals(goal1));
        assertTrue(target1.equals(goal2) || target2.equals(goal2));
    }
}
