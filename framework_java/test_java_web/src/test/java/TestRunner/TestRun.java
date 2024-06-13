package TestRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
		features = "src/test/resources/features/SauceDemo.feature",
		glue = {"stepDefinitions"},
		plugin = {
				"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
				"pretty",
				"html:target/Reports/report.html",
				"json:target/Reports/report.json",
				"junit:target/Reports/report.xml"
				}
		)

public class TestRun extends AbstractTestNGCucumberTests{

}
