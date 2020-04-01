package agile.games;

import agile.games.tts.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Set;

import static agile.games.GameStepUtilities.getGameSession;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameFlowSteps  {

    private UserId userId;

    @Given("a game is in phase {string}")
    public void aGameInPhase(String phase) {
        getGameSession().setGamePhase(GamePhase.valueOf(phase.toUpperCase()));
    }

    @When("the facilitator starts the assignments")
    public void theFacilitatorStartsTheAssignments() {
        getGameSession().assignTargets();
    }

    @Then("the game is in phase {string}")
    public void theGameIsInPhase(String phase) {
        assertEquals(GamePhase.valueOf(phase.toUpperCase()), getGameSession().getGamePhase());
    }

    @When("the facilitator starts the game")
    public void theFacilitatorStartsTheGame() {
        getGameSession().start();
    }

    @When("all players of the game has reached their end goal")
    public void allPlayersOfTheGameHasReachedTheirEndGoal() {
        Set<PlayerId> players = getGameSession().getPlayers();
        for (PlayerId player : players) {
            getGameSession().setPlayerState(player, PlayerState.DONE);
        }
    }

    @And("there are {int} players in the game")
    public void thereArePlayersInTheGame(int noPlayers) {
        for (int n = 0; n < noPlayers; n++) {
            getGameSession().addPlayer(getGameSession().newUser());
        }
    }

    @Given("a user")
    public void aUser() {
        userId = getGameSession().newUser();
    }

    @When("the user chose to facilitate a game")
    public void theUserChoseToFacilitateAGame() {
        getGameSession().setFacilitator(userId);
    }

    @And("the user gets the role {string}")
    public void theUserGetsTheRole(String roleName) {
        UserRole expectedRole = UserRole.valueOf(roleName.toUpperCase());
        UserRole hasRole = getGameSession().getUserRole(userId);
        assertEquals(expectedRole, hasRole);
    }

    @When("the user chose to join the game")
    public void theUserChoseToJoinTheGame() {
        getGameSession().addPlayer(userId);
    }

}
