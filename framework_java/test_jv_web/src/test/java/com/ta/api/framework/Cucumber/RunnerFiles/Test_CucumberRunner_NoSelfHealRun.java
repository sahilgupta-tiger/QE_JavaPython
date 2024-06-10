package com.ta.api.framework.Cucumber.RunnerFiles;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features ="FeatureFiles",
        glue ="TestCasesGlue", monochrome = true)
public class Test_CucumberRunner_NoSelfHealRun extends AbstractTestNGCucumberTests {

}

//CucumberStepDefinition