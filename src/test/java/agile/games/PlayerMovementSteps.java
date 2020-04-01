package agile.games;

import agile.games.tts.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerMovementSteps {
    private GameSession gameSession;
    private PlayerId playerId;


    @Given("a board with dimensions {int},{int}")
    public void aBoardWithDimensions(int x, int y) {
        gameSession = new GameSession(x, y);
    }

    @And("a player on position {int},{int}")
    public void aPlayerOnPosition(int x, int y) {
        playerId = gameSession.addPlayer(gameSession.newUser());
        gameSession.placePlayerAt(playerId, x, y);
    }

    @When("the player moves in direction {string}")
    public void thePlayerMovesInDirectionUp(String direction) {
        gameSession.movePlayer(playerId, Direction.valueOf(direction.toUpperCase()));
    }

    @Then("the player is on position {int},{int}")
    public void thePlayerIsOnPosition(int x, int y) {
        PlayerPosition playerPosition = gameSession.getPlayerPosition(playerId);
        assertEquals(x, playerPosition.getX());
        assertEquals(y, playerPosition.getY());
    }

    public GameSession getGameSession() {
        return gameSession;
    }
}
