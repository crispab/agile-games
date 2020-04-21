package agile.games;

import agile.games.tts.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static agile.games.GameStepUtilities.getGameSession;
import static agile.games.GameStepUtilities.playerId;
import static agile.games.tts.PlayerGoalState.COMPLETED;
import static agile.games.tts.PlayerState.DONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameExecution {

    private boolean inGivenMode;

    @Then("the player has end goal to reach {int},{int}")
    public void thePlayerHasEndGoalToReach(int x, int y) {
        PlayerPosition endPosition = getGameSession().getPlayerEndGoalPosition(playerId);
        assertEquals(new PlayerPosition(x, y), endPosition);
    }

    @Given("the player is at position {int},{int} named {string}")
    public void aPlayerAtPositionNamed(int x, int y, String name) {
        playerId = getGameSession().addPlayerAt(name, getGameSession().newUser(), x, y);
    }

    @And("a player named {string}")
    public void aPlayerAtPositionNamed(String name) {
        getGameSession().addPlayerNamed(name, getGameSession().newUser());
    }

    @Then("player named {string} has two goals {string} and {string}")
    public void playerNamedHasTwoGoalsAnd(String playerName, String targetName1, String targetName2) {
        PlayerId player = getGameSession().findPlayerByName(playerName);
        PlayerId target1 = getGameSession().findPlayerByName(targetName1);
        PlayerId target2 = getGameSession().findPlayerByName(targetName2);

        PlayerTapGoal goal1 = getGameSession().getPlayerGoal1(player);
        PlayerTapGoal goal2 = getGameSession().getPlayerGoal2(player);
        assertTrue(target1.equals(goal1.getTapGoal()) || target2.equals(goal1.getTapGoal()));
        assertTrue(target1.equals(goal2.getTapGoal()) || target2.equals(goal2.getTapGoal()));
    }

    @And("player named {string} is assigned the two goals {string} and {string}")
    public void playerNamedIsAssignedTwoGoalsAnd(String playerName, String targetName1, String targetName2) {
        PlayerId playerId = getGameSession().findPlayerByName(playerName);
        PlayerId target1 = getGameSession().findPlayerByName(targetName1);
        PlayerId target2 = getGameSession().findPlayerByName(targetName2);
        getGameSession().setPlayerGoals(playerId, target1, target2);
    }

    @Then("the player has reached the first goal")
    public void thePlayerHasReachedTheFirstGoal() {
        PlayerTapGoal playerGoal1 = getGameSession().getPlayerGoal1(playerId);
        assertEquals(COMPLETED, playerGoal1.getState(), "Player has not reached goal 1.");
    }

    @And("the player has reached the second goal")
    public void thePlayerHasReachedTheSecondGoal() {
        PlayerTapGoal playerGoal2 = getGameSession().getPlayerGoal2(playerId);
        assertEquals(COMPLETED, playerGoal2.getState(), "Player has not reached goal 2.");
    }

    @And("the player has reached the end goal")
    public void thePlayerHasReachedTheEndGoal() {
        PlayerEndGoal playerEndGoal = getGameSession().getPlayerEndGoal(playerId);
        assertEquals(COMPLETED, playerEndGoal.getState(), "Player has not reached end goal.");
    }

    @Then("the player is done")
    public void thePlayerIsDone() {
        PlayerState playerState = getGameSession().getPlayerState(playerId);
        assertEquals(DONE, playerState);
    }

    @And("the board looks as:")
    public void theBoardIsAs() {
        inGivenMode = true;
    }

    @Then("the board should look like:")
    public void theGridShouldLookLike() {
        inGivenMode = false;
    }


    @And("{int} {string}")
    public void boardState(int row, String rowContent) {
        if (inGivenMode) {
            createRowOnBoard(row, rowContent);
        } else {
            assertRowOfBoard(row, rowContent);
        }
    }

    private void createRowOnBoard(int row, String desiredContent) {
        int xPos = 0;
        for (int n = 0; n < desiredContent.length(); n++) {
            char current = desiredContent.charAt(n);
            switch (current) {
                case ' ':
                    xPos++;
                    continue;
                case '|':
                    continue;
                default:
                    PlayerId player = getGameSession().findPlayerByName("" + current);
                    getGameSession().placePlayerAt(player, xPos, row);
                    xPos++;
            }
        }
    }

    private void assertRowOfBoard(int row, String expectedContent) {
        String actual = getGameSession().getRow(row);
        assertEquals(expectedContent, actual, "On row " + row);
    }

}
