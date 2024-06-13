package TestRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = "Feature/SauceDemo.feature",
		glue = {"stepDefinitions"},
		plugin = {
				"pretty",
				"html:target/Reports/report.html",
				"json:target/Reports/report.json",
				"junit:target/Reports/report.xml",}
		)

public class TestRun extends AbstractTestNGCucumberTests{

}
