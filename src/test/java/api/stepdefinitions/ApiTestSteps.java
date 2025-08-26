package api.stepdefinitions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Attachment;
import io.restassured.response.Response;


public class ApiTestSteps extends TestBaseApi {

    @Given("Assigning Payload source {string} and {string}")
    public void loadpayloadpath(String workbook, String worksheet) {
        Workbook = workbook;
        Worksheet = worksheet;
    }


    @Given("Load Data into Methods {string}")
    public void loadDataIntoMethods(String dataref) {
        datavalues = excelDatafetcher.getExcelData(dataref, Workbook, Worksheet);
        alluresetTestcase.setAllureTestcaseName(datavalues.get("Test_Case_Name"));
    }


    @When("Make API call to run")
    public void makeApiCallToRun() throws Exception {
        try {
            responseJson=apiCoreModel.makeApiCall(datavalues);
        } catch (Exception e) {
            throw new RuntimeException("No more data sets available to run." + e);
        }
    }
    @Then("Validate the json response with the expected output")
    public void validateResponse() {
        try{
            apiCoreModel.responseValidation(datavalues,responseJson);
        } catch (Exception e){
            throw new RuntimeException("No more data sets available to run."+e);
        }
    }


    @Attachment(value = "API Response", type = "text/plain")
    public String attachResponseToAllure(Response response) {
        return response.asString();
    }


}