package com.ta.api.framework.healenium;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        features = {"src/test/resources/features/healenium-run.feature"},
        glue = {"com.ta.api.framework.healenium.SelfHealSampleWebCommonActions"},
        monochrome = true,
        dryRun = true
)

public class SelfHealRunCucumberTest extends AbstractTestNGCucumberTests {

}
