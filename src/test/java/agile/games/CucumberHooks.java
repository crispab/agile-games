package agile.games;

import io.cucumber.java.Before;

public class CucumberHooks {

    @Before
    public void beforeEaxhScenario() {
        GameStepUtilities.gameSession = null;
    }
}
