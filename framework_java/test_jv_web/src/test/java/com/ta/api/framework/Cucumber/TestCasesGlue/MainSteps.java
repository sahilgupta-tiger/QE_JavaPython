package com.ta.api.framework.Cucumber.TestCasesGlue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MainSteps {
    @Given("I can identify three UI fields on the website")
    public void i_can_identify_ui_fields_on_the_website() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("I can identify {int} UI fields on the website");
//        commonMethods.HealeniumApplicationchecksOldLocator(map);
        //throw new io.cucumber.java.PendingException();
    }
    @When("I click the Change Locators button")
    public void i_click_the_button() {
        System.out.println("I click the {string} button");
        // Write code here that turns the phrase above into concrete actions
        //throw new io.cucumber.java.PendingException();
    }
    @Then("I should be able to identify the same three UI fields on the website")
    public void i_should_be_able_to_identify_the_same_ui_fields_on_the_website() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("I click the {string} button");
        //throw new io.cucumber.java.PendingException();
    }


}
