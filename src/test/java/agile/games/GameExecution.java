package agile.games;

import agile.games.tts.PlayerId;
import agile.games.tts.PlayerPosition;
import agile.games.tts.PlayerState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static agile.games.GameStepUtilities.getGameSession;
import static agile.games.GameStepUtilities.playerId;
import static agile.games.tts.PlayerState.DONE;
import static org.junit.jupiter.api.Assertions.*;

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

    @And("has as a first goal a player at position {int},{int}")
    public void hasAsAFirstGoalAPlayerAtPosition(int x, int y) {
        PlayerId targetId = getGameSession().addPlayerAt(getGameSession().newUser(), x, y);
        getGameSession().setPlayerGoal1(playerId, targetId);
    }

    @And("has as a second goal a player at position {int},{int}")
    public void hasAsASecondGoalAPlayerAtPosition(int x, int y) {
        PlayerId targetId = getGameSession().addPlayerAt(getGameSession().newUser(), x, y);
        getGameSession().setPlayerGoal2(playerId, targetId);
    }

    @Then("the player has reached the first goal")
    public void thePlayerHasReachedTheFirstGoal() {
        PlayerId playerGoal1 = getGameSession().getPlayerGoal1(playerId);
        assertNull(playerGoal1);
    }

    @And("the player has reached the second goal")
    public void thePlayerHasReachedTheSecondGoal() {
        PlayerId playerGoal2 = getGameSession().getPlayerGoal2(playerId);
        assertNull(playerGoal2);
    }

    @Then("the player is done")
    public void thePlayerIsDone() {
        PlayerState playerState = getGameSession().getPlayerState(playerId);
        assertEquals(DONE, playerState);
    }
}
