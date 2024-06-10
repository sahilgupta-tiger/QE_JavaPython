package com.ta.api.framework.healenium;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features ="src/java/com/ta/api/framework/features",
        glue ="com.ta.api.framework.healenium.NoSelfHealWebSampleTC", monochrome = true)
public class NoSelfHealRunCucumberTest extends AbstractTestNGCucumberTests {

}
