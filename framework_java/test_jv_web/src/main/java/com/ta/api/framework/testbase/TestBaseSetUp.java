package com.ta.api.framework.testbase;

import com.ta.api.framework.constants.CommonConstants;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.properties.ConfigReader;
import com.ta.api.framework.reports.APIHtmlReport;
import com.ta.api.framework.reports.CustomHtmlReport;
import com.ta.api.framework.reports.ReportManager;
import com.ta.api.framework.web.wrappers.GenericWrapper;
import com.ta.api.framework.mobile.wrappers.MobileGenericWrapper;
import com.ta.api.framework.web.wrappers.SelfHealGenericWrapper;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TestBaseSetUp extends CustomHtmlReport {

    public static final APIHtmlReport apiHtmlReport = new APIHtmlReport();
    static final Logger log = Logger.getLogger(TestBaseSetUp.class);
    /**
     * This tdMap is to initialize the TestData to reflect to the Custom Report
     **/
    protected HashMap<String, Object> tdMap;
    public String testCaseMethodName="";
    private AtomicInteger id = new AtomicInteger(1);

    public TestBaseSetUp() {
        this.commonPojo = new CommonPojo();
        tdMap = new HashMap<>();
    }

    /**
     * This method is return the Suite Parameters testType, BuildNumber, TestGroups and apiURI
     * from the Executionconfig.properties
     *
     * @return returns some parameters from the suite XML
     */
    public Map<String, String> suiteParameters() {

        String[] apiBaseURI = getProperty("apiBaseURI").split(",");
        String[] port = getProperty("port").split(",");
        String[] version = getProperty("version").split(",");

        StringBuilder apiUriBuilder = new StringBuilder();
        if (apiBaseURI.length == port.length && port.length == version.length) {
            for (int i = 0; i < apiBaseURI.length; i++) {
                String completeBaseUri = apiBaseURI[i] + ":" + port[i] + "/" + version[i] + ",";
                completeBaseUri = completeBaseUri.replace(":null", "");
                completeBaseUri = completeBaseUri.replace("/null", "");
                completeBaseUri = completeBaseUri.replace(",", "/,");
                apiUriBuilder.append(completeBaseUri);
            }
        } else {
            log.error("Please configure the apiBaseURI, port and version in the Execution.properties file properly");
            System.exit(0);
        }
        HashMap<String, String> suiteParameters = new HashMap<>();
        suiteParameters.put("testType", "api");
        suiteParameters.put(CommonConstants.BUILD_NUMBER, getProperty(CommonConstants.BUILD_NUMBER));
        suiteParameters.put("TestGroups", getProperty("TestGroups"));
        suiteParameters.put("apiURI", apiUriBuilder.toString());
        return suiteParameters;
    }

    /**
     * This method is to Initiate the Extent and Custom Reports
     *
     * @param context The test context which contains all the information for a given test run
     */
    @BeforeSuite(alwaysRun = true)
    public void startReport(final ITestContext context) {

        context.getCurrentXmlTest().setParameters(suiteParameters());
        initiateBaseSuiteReport(context);

        final File apiHtmlFile = CommonConstants.API_HTML_REPORT;
        apiHtmlReport.apiHtmlReportStart(apiHtmlFile);
        apiHtmlReport.apiCustomReportHeader(8, context.getCurrentXmlTest().getName(),
                context.getCurrentXmlTest().getParameter("apiURI"),
                context.getSuite().getName(),
                context.getCurrentXmlTest().getParameter(CommonConstants.BUILD_NUMBER));

        final File customHtmlFile = CommonConstants.CUSTOM_HTML_REPORT;
        customHtmlReportStart(customHtmlFile);
    }

    /**
     * This method is to call the suite Parameters and pass them accordingly
     *
     * @param testType the test type from the suite XML
     * @param apiURI   the API URI from the suite XML
     */
    @BeforeTest(alwaysRun = true)
    @Parameters({"testType", "apiURI"})
    public void setSuiteParameters(String testType, String apiURI) {

        this.commonPojo.setSuiteTestType(testType);
        if (apiURI != null) {
            this.commonPojo.setSuiteApiURI(Arrays.asList(apiURI.split(",")));
        }

    }

    /**
     * This method is to add the Row Header to the API HTML Custom Report
     */


    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext context)
    {
        apiHtmlReport.addApiRowHeader(this.getClass().getSimpleName());
    }

    @BeforeGroups(alwaysRun = true, groups = {"web"})
    public synchronized void startWebTest() {
        System.out.println("web test in group");
        GenericWrapper genericWrapper = new GenericWrapper();
        genericWrapper.invokeApp("chromde", "https://www.facebook.com/");
    }

    @BeforeGroups(alwaysRun = true, groups = {"api"})
    public synchronized void startApiTest() {
        System.out.println("web test in api");
    }

    /**
     * This method is to create the Test to the Extent Reports
     * Also, to set the Test Scenario and Test Data from the Data provider
     *
     * @param method the java reflection method
     * @param params the test data object from data provider
     */
    @SuppressWarnings("unchecked")
    @BeforeMethod
    public synchronized void startTest(Method method, Object[] params,ITestContext context) {


        if (!testCaseMethodName.equals(method.getName())) {
            testCaseMethodName = method.getName();
            id = new AtomicInteger(1);
        }

        String index = Integer.toString(id.getAndIncrement());
        this.commonPojo.setTestMethodName(method.getName());

        HashMap<String, Object> map = (HashMap<String, Object>) Arrays.asList(params).get(0);
        String testScenario = String.valueOf(map.get("TestScenario"));

        /*test = extent.createTest(method.getName() + " - " + index + "Test Scenario is: " + testScenario ,
                this.badge6 + "Test Scenario is: " + testScenario + this.badge5);*/
        /*test = extent.createTest(method.getName() + " - " + testScenario ,
                this.badge6 + "Test Scenario is: " + testScenario + this.badge5);*/
        test = extent.createTest(method.getName() + " - " + index + " - " + testScenario ,
                "");
        this.commonPojo.setTestScenario(testScenario);
        tdMap.putAll(map);
        String path = context.getCurrentXmlTest().getParameter("jsonDataPath");
        if (testCaseMethodName.toLowerCase().contains("web")) {
            if(context.getCurrentXmlTest().getParameter("SelfHeal").equalsIgnoreCase("yes")){
                SelfHealGenericWrapper selfhealgenericWrapper = new SelfHealGenericWrapper();
                selfhealgenericWrapper.invokeselfhealApp(ConfigReader.getProperty("browser"), ConfigReader.getProperty("selfheal.web.url"));
            }
            else if(context.getCurrentXmlTest().getParameter("SelfHeal").equalsIgnoreCase("no")){
                GenericWrapper genericWrapper = new GenericWrapper();
                genericWrapper.invokeApp(ConfigReader.getProperty("browser"), ConfigReader.getProperty("selfheal.web.url"));
            }
            else{
                GenericWrapper genericWrapper = new GenericWrapper();
                genericWrapper.invokeApp(ConfigReader.getProperty("browser"), ConfigReader.getProperty("web.url"));
            }
            }
        else if (testCaseMethodName.toLowerCase().contains("api")) {
//            if (GetTokenApi.token.equals("")) {
//                GetTokenApi getTokenApi = new GetTokenApi(commonPojo);
//                getTokenApi.getToken(path);
//            }
        }
        else if(testCaseMethodName.toLowerCase().contains("mobile")) {
            MobileGenericWrapper mobileGenericWrapper = new MobileGenericWrapper();
            System.out.println(context.getCurrentXmlTest().getParameter("testtype"));
            if(context.getCurrentXmlTest().getParameter("testtype").equalsIgnoreCase("native")){

            }
            else {
                mobileGenericWrapper.invokemobile(
                        ConfigReader.getProperty("appiumOs"),
                        ConfigReader.getProperty("appiummobilebrowser"),
                        ConfigReader.getProperty("appiumipAddress"),
                        ConfigReader.getProperty("appiumport"),
                        ConfigReader.getProperty("mobile.url"),
                        ConfigReader.getProperty("AndroidDeviceName")
                );
            }
        }

    }
   /* @BeforeMethod (groups={"web"})
    public synchronized void startUITest(Method method, Object[] params) {

        if (!testCaseMethodName.equals(method.getName())) {
            testCaseMethodName = method.getName();
            id = new AtomicInteger(1);
        }
        String index = Integer.toString(id.getAndIncrement());
        this.commonPojo.setTestMethodName(method.getName());

        HashMap<String, Object> map = (HashMap<String, Object>) Arrays.asList(params).get(0);
        String testScenario = String.valueOf(map.get("TestScenario"));

        test = extent.createTest(method.getName() + " - " + index,
                this.badge6 + "Description is: " + testScenario + this.badge5);

        this.commonPojo.setTestScenario(testScenario);
        tdMap.putAll(map);
    }*/

    /**
     * This method is to Flushing the Data to the TestData Custom Report after execution
     * Also to add the Rows to the API HTML Custom Report
     * Also, to clear the POJO values which was set earlier
     */
    @AfterMethod(alwaysRun = true)
    public void flushTestResult() {

        customReportfromTestData(tdMap);
        tdMap.clear();

        if (testCaseMethodName.toLowerCase().contains("api")) {
            apiHtmlReport.addApiRowValues(this.commonPojo);

            this.commonPojo.rptHttpMethod.clear();
            this.commonPojo.rptEndpoint.clear();
            this.commonPojo.rptExpStatusCode.clear();
            this.commonPojo.rptActStatusCode.clear();
            this.commonPojo.rptResponseTime.clear();
            this.commonPojo.rptTestStatus.clear();


            this.commonPojo.rptComments.clear();
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        // To Handle things in afterClass
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        // To Handle things in afterTest
    }

    /**
     * To End and Flush the Extent and Custom Reports
     */
    @AfterSuite(alwaysRun = true)
    public void endReport() {

        apiHtmlReport.apiHtmlReportBuild();
        ReportManager.extent.flush();
        addToCustomReport(customHtmlReportEnd());
    }



}
