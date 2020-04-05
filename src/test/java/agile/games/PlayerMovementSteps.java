package agile.games;

import agile.games.tts.Direction;
import agile.games.tts.GamePhase;
import agile.games.tts.PlayerPosition;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static agile.games.GameStepUtilities.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerMovementSteps  {

    @Given("a board with dimensions {int},{int}")
    public void aBoardWithDimensions(int width, int height) {
        createGameSession(width, height);
        getGameSession().setGamePhase(GamePhase.EXECUTING);
    }

    @And("a player at position {int},{int}")
    public void aPlayerOnPosition(int x, int y) {
        playerId = getGameSession().addPlayerAt(getGameSession().newUser(), x, y);
    }

    @And("another player at position {int},{int}")
    public void anotherPlayerOnPosition(int x, int y) {
       getGameSession().addPlayerAt(getGameSession().newUser(), x, y);
    }

    @When("the player moves in direction {string}")
    public void thePlayerMovesInDirection(String direction) {
        getGameSession().movePlayer(playerId, Direction.valueOf(direction.toUpperCase()));
    }

    @Then("the player is on position {int},{int}")
    public void thePlayerIsOnPosition(int x, int y) {
        PlayerPosition playerPosition = getGameSession().getPlayerPosition(playerId);
        assertEquals(x, playerPosition.getX());
        assertEquals(y, playerPosition.getY());
    }
}
