package com.ta.api.framework.RunnerCucmber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features ="src/main/java/com/ta/api/framework/features",
        glue ="CucumberStepDefinition", monochrome = true)
public class CucumberRunner_NoSelfHealRun extends AbstractTestNGCucumberTests {

}

//