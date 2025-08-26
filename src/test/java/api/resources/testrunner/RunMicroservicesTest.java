package api.resources.testrunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import static allurereportGeneration.allureReportGeneration.*;

@CucumberOptions(
        features = "src/test/java/api/resources/features",
        glue = {"api/stepdefinitions"},
        plugin = {
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "pretty",
                "html:target/Reports/report_api.html",
                "json:target/Reports/report_api.json",
                "junit:target/Reports/report_api.xml"
        }
)

public class RunMicroservicesTest extends AbstractTestNGCucumberTests {

        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }

        @BeforeClass
        public void Notestartexecutiontime(){
                saveExecutionStartTime();
        }

        @AfterClass
        public void Generatereport() throws Exception {
                loadProperties();
                reportGeneration();
        }

}
