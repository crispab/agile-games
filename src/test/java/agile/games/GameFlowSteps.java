package agile.games;

import agile.games.tts.GamePhase;
import agile.games.tts.GameSession;
import agile.games.tts.PlayerId;
import agile.games.tts.PlayerState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameFlowSteps {

    private GameSession gameSession;

    @Then("the game is in phase {string}")
    public void theGameIsInPhase(String phase) {
        assertEquals(GamePhase.valueOf(phase.toUpperCase()), gameSession.getGamePhase());
    }

    @Given("a game is in phase {string}")
    public void aGameInPhase(String phase) {
        gameSession = new GameSession();
        gameSession.setGamePhase(GamePhase.valueOf(phase.toUpperCase()));
    }

    @When("the facilitator starts the assignments")
    public void theFacilitatorStartsTheAssignments() {
        gameSession.assignTargets();
    }

    @When("the facilitator starts the game")
    public void theFacilitatorStartsTheGame() {
        gameSession.start();
    }

    @When("all players of the game has reached their end goal")
    public void allPlayersOfTheGameHasReachedTheirEndGoal() {
        Set<PlayerId> players = gameSession.getPlayers();
        for (PlayerId player : players) {
            gameSession.setPlayerState(player, PlayerState.DONE);
        }
    }

    @And("there are {int} players in the game")
    public void thereArePlayersInTheGame(int noPlayers) {
        for (int n = 0; n < noPlayers; n++) {
            gameSession.addPlayer();
        }
    }
}
