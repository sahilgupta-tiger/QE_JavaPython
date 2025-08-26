package apicoreutils;//package com.ta.api.framework.api.stepdefinitions;
//
//import com.ta.api.framework.commonmethods.GetTokenApi;
//import com.ta.api.framework.constants.CommonConstants;
//import com.ta.api.framework.pojo.CommonPojo;
//import com.ta.api.framework.properties.ConfigReader;
//import com.ta.api.framework.reports.APIHtmlReport;
//import com.ta.api.framework.reports.CustomHtmlReport;
//import com.ta.api.framework.reports.ReportManager;
//import com.ta.api.framework.web.wrappers.GenericWrapper;
//import io.cucumber.java.Before;
//import io.cucumber.java.After;
//import io.cucumber.java.Scenario;
//import io.cucumber.java.en.Given;
//import io.qameta.allure.Allure;
//import io.qameta.allure.AllureLifecycle;
//import io.qameta.allure.model.TestResult;
//import org.apache.log4j.Logger;
//import org.testng.ITestContext;
//import org.testng.annotations.Parameters;
//
//import java.io.File;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class HookDefinitions extends CustomHtmlReport {
//
//    public static final APIHtmlReport apiHtmlReport = new APIHtmlReport();
//    static final Logger log = Logger.getLogger(HookDefinitions.class);
//    protected HashMap<String, Object> tdMap;
//    public String testCaseMethodName = "";
//    private AtomicInteger id = new AtomicInteger(1);
//    private CommonPojo commonPojo;
//    private AllureLifecycle allureLifecycle;
//    private String currentTestUuid;
//
//    public HookDefinitions() {
//        this.commonPojo = new CommonPojo();
//        tdMap = new HashMap<>();
//        this.allureLifecycle = Allure.getLifecycle();
//    }
//
//    public Map<String, String> suiteParameters() {
//        String[] apiBaseURI = getProperty("apiBaseURI").split(",");
//        String[] port = getProperty("port").split(",");
//        String[] version = getProperty("version").split(",");
//
//        StringBuilder apiUriBuilder = new StringBuilder();
//        if (apiBaseURI.length == port.length && port.length == version.length) {
//            for (int i = 0; i < apiBaseURI.length; i++) {
//                String completeBaseUri = apiBaseURI[i] + ":" + port[i] + "/" + version[i] + ",";
//                completeBaseUri = completeBaseUri.replace(":null", "");
//                completeBaseUri = completeBaseUri.replace("/null", "");
//                completeBaseUri = completeBaseUri.replace(",", "/,");
//                apiUriBuilder.append(completeBaseUri);
//            }
//        } else {
//            log.error("Please configure the apiBaseURI, port and version in the Execution.properties file properly");
//            System.exit(0);
//        }
//        HashMap<String, String> suiteParameters = new HashMap<>();
//        suiteParameters.put("testType", "api");
//        suiteParameters.put(CommonConstants.BUILD_NUMBER, getProperty(CommonConstants.BUILD_NUMBER));
//        suiteParameters.put("TestGroups", getProperty("TestGroups"));
//        suiteParameters.put("apiURI", apiUriBuilder.toString());
//        return suiteParameters;
//    }
//
//    @Before(order = 0)
//    public void startReport(final ITestContext context) {
//        context.getCurrentXmlTest().setParameters(suiteParameters());
//        initiateBaseSuiteReport(context);
//
//        final File apiHtmlFile = CommonConstants.API_HTML_REPORT;
//        apiHtmlReport.apiHtmlReportStart(apiHtmlFile);
//        apiHtmlReport.apiCustomReportHeader(8, context.getCurrentXmlTest().getName(),
//                context.getCurrentXmlTest().getParameter("apiURI"),
//                context.getSuite().getName(),
//                context.getCurrentXmlTest().getParameter(CommonConstants.BUILD_NUMBER));
//
//        final File customHtmlFile = CommonConstants.CUSTOM_HTML_REPORT;
//        customHtmlReportStart(customHtmlFile);
//    }
//
////    @Before(order = 1)
////    @Parameters({"testType", "apiURI"})
////    public void setSuiteParameters(String testType, String apiURI) {
////        this.commonPojo.setSuiteTestType(testType);
////        if (apiURI != null) {
////            this.commonPojo.setSuiteApiURI(Arrays.asList(apiURI.split(",")));
////        }
////    }
//
//    @Before(order = 1)
//    @Parameters({"testType", "apiURI"})
//    public void setSuiteParameters(String testType, String apiURI) {
//        this.commonPojo.setSuiteTestType(testType);
//        if (apiURI != null) {
//            this.commonPojo.setSuiteApiURI(Arrays.asList(apiURI.split(",")));
//        }
//    }
//
//    @Before(order = 2)
//    public void beforeClass(ITestContext context) {
//        apiHtmlReport.addApiRowHeader(this.getClass().getSimpleName());
//    }
//
//    @Before(order = 3)
//    public synchronized void startWebTest() {
//        System.out.println("web test in group");
//        GenericWrapper genericWrapper = new GenericWrapper();
//        genericWrapper.invokeApp("chrome", "https://www.facebook.com/");
//    }
//
//    @Before(order = 3)
//    public synchronized void startApiTest() {
//        System.out.println("web test in api");
//    }
//
//    @Before(order = 4)
//    public synchronized void startTest(Method method, Object[] params, ITestContext context) {
//        if (!testCaseMethodName.equals(method.getName())) {
//            testCaseMethodName = method.getName();
//            id = new AtomicInteger(1);
//        }
//
//        String index = Integer.toString(id.getAndIncrement());
//        this.commonPojo.setTestMethodName(method.getName());
//
//        HashMap<String, Object> map = (HashMap<String, Object>) Arrays.asList(params).get(0);
//        String testScenario = String.valueOf(map.get("TestScenario"));
//
//        test = extent.createTest(method.getName() + " - " + index + " - " + testScenario, "");
//        this.commonPojo.setTestScenario(testScenario);
//        tdMap.putAll(map);
//        String path = context.getCurrentXmlTest().getParameter("jsonDataPath");
//        if (testCaseMethodName.toLowerCase().contains("web")) {
//            GenericWrapper genericWrapper = new GenericWrapper();
//            genericWrapper.invokeApp("chrome", ConfigReader.getProperty("web.url"));
//        } else if (testCaseMethodName.toLowerCase().contains("api")) {
//            if (GetTokenApi.token.equals("")) {
//                // GetTokenApi getTokenApi = new GetTokenApi(commonPojo);
//                // getTokenApi.getToken(path);
//            }
//        }
//
//        // Start Allure Test Case with UUID
//        currentTestUuid = UUID.randomUUID().toString();
//        TestResult testResult = new TestResult()
//                .setUuid(currentTestUuid)
//                .setName(method.getName() + " - " + index + " - " + testScenario);
//        allureLifecycle.scheduleTestCase(testResult);
//        allureLifecycle.startTestCase(currentTestUuid);
//    }
//    private Scenario scenario;
//
//    @Before(order = 5) // Adjust the order as needed
//    public void beforeScenario(Scenario scenario) {
//        this.scenario = scenario;
//    }
//
//    @Given("I perform some action")
//    public void iPerformSomeAction() {
//        // Access scenario name
//        String scenarioName = scenario.getName();
//        System.out.println("Executing scenario: " + scenarioName);
//    }
//    @After(order = 0)
//    public void flushTestResult() {
//        customReportfromTestData(tdMap);
//        tdMap.clear();
//
//        if (testCaseMethodName.toLowerCase().contains("api")) {
//            apiHtmlReport.addApiRowValues(this.commonPojo);
//            this.commonPojo.rptHttpMethod.clear();
//            this.commonPojo.rptEndpoint.clear();
//            this.commonPojo.rptExpStatusCode.clear();
//            this.commonPojo.rptActStatusCode.clear();
//            this.commonPojo.rptResponseTime.clear();
//            this.commonPojo.rptTestStatus.clear();
//            this.commonPojo.rptComments.clear();
//        }
//
//        // Stop Allure Test Case
//        if (currentTestUuid != null) {
//            allureLifecycle.stopTestCase(currentTestUuid);
//            allureLifecycle.writeTestCase(currentTestUuid);
//            currentTestUuid = null;
//        }
//    }
//
//    @After(order = 1)
//    public void afterClass() {
//    }
//
//    @After(order = 2)
//    public void afterTest() {
//    }
//
//    @After(order = 3)
//    public void endReport() {
//        apiHtmlReport.apiHtmlReportBuild();
//        ReportManager.extent.flush();
//        addToCustomReport(customHtmlReportEnd());
//    }
//}
