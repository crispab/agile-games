package agile.games;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"de.monochromata.cucumber.report.PrettyReports:build/pretty-cucumber"})
public class RunCucumberTest {
}