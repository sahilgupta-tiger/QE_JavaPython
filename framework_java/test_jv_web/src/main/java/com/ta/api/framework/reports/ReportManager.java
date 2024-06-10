package com.ta.api.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.properties.ConfigReader;
import com.ta.api.framework.web.wrappers.GenericWrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.asserts.SoftAssert;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



public class ReportManager {

    protected static final AtomicInteger id = new AtomicInteger(0);
    protected static final Logger logger = Logger.getLogger(ReportManager.class);
    protected static final SoftAssert sAssert = new SoftAssert();
    public static File resultDirectory;
    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected String badge1 = "<b class=\"badge badge-primary\">";
    protected String badge2 = "<font color=\"red\">";
    protected String badge3 = "<b>";
    protected String badge4 = "</b>";
    protected String badge5 = "</font>";
    protected String badge6 = "<font color=\"Tomato\">";
    protected String badge7 = "<font color=\"green\">";
    protected CommonPojo commonPojo;

    AtomicInteger sno = new AtomicInteger(1);

    public ReportManager(CommonPojo commonPojo) {
        this.commonPojo = commonPojo;
    }

    public ReportManager() {
    }

    /**
     * This method is to Create the Folder for the Test Results
     * If the Folder is already exists, it will use the same
     * It will Initialize the Extent Reports
     *
     * @param context The current test context
     */
    protected synchronized void initiateBaseSuiteReport(final ITestContext context) {
        logger.info("Initializing Base Suite Report");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm");
        String currentDateTime = LocalDateTime.now().format(dtf);

        resultDirectory = new File(System.getProperty("user.dir") + "/testresults/" + currentDateTime);

        if (!resultDirectory.exists()) {
            if (resultDirectory.mkdirs()) {
                logger.info("Directory is created for Test Results");
            } else {
                logger.info("Failed to Create Test Results Directory");
            }
        } else {
            logger.info("Test Results Directory is already exists");
        }
        try {
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(new File(resultDirectory + File.separator + "index.html"));

            htmlReporter.config().setReportName("<font size=\"3\">" + context.getCurrentXmlTest().getParameter("TestGroups") + "Test Report" + "</font>");
            htmlReporter.config().isTimelineEnabled();

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        logger.info("Extent Report directory: " + resultDirectory);
    }

    public static void logScreenshot(String description, String screenshotFilePath) {
        try {
            if (test != null) {
                // Load the screenshot file as byte array
                File screenshotFile = new File(screenshotFilePath);
                FileInputStream fis = new FileInputStream(screenshotFile);
                byte[] screenshotBytes = new byte[(int) screenshotFile.length()];
                fis.read(screenshotBytes);
                fis.close();
                // Embed the screenshot in the report
                test.addScreenCaptureFromPath(screenshotFilePath, description);
            }
        } catch (IOException e) {
            logger.error("Failed to log screenshot in report: " + e.getMessage());
        }
    }

   /* public String testCaseMethodName ;
    public String testMethodName;

    public String testCaseMethodName(){
        if (testCaseMethodName.equals(method.getName())) {
            testCaseMethodName = method.getName();
        }
        return testCaseMethodName;
    }*/
    /**
     * This method will logs the Respective Status and Message to the Extent Reports
     *
     * @param status  The status of the test step to log in reports
     * @param message The message of the test step to log in reports
     */

    public synchronized void logReport(final String status, final String message) {
        if (ReportManager.test != null) {
            if (status.equalsIgnoreCase("PASS")) {
                ReportManager.test.pass(message);
            } else if (status.equalsIgnoreCase("FAIL")) {
                ReportManager.test.fail(this.badge2 + message + this.badge5);
                ReportManager.logger.info((Object) ("****FAILED MESSAGE******" + message));
                GenericWrapper genericWrapper1 = new GenericWrapper();
//                genericWrapper1.takeSnapshot();
                String screenshotFilePath = genericWrapper1.takeSnapshot();
                ReportManager.logScreenshot("Failed step screenshot", screenshotFilePath);

            } else if (status.equalsIgnoreCase("INFO")) {
                ReportManager.test.info(message);
            } else if (status.equalsIgnoreCase("SKIP")) {
                ReportManager.test.skip(message);
            } else {
                ReportManager.logger.error((Object) "Report Initialization is Failed");
            }
        } else {
            Assert.fail("Error!! Report Initialization failed!!");
        }
    }

    /**
     * This method helps us to return property from the Properties file.
     *
     * @param key Pass the key for which the value should return from properties file
     * @return returns the value for the user specified key
     */
    public String getProperty(final String key) {
        String returnKey = "";
        final String propKey = ConfigReader.getProperty(key);
        if (propKey == null || propKey.isEmpty()) {
            ReportManager.logger.error((Object) ("The Property Key " + key + " is not present in ExecutionConfig.properties file"));
        } else {
            returnKey = propKey;
        }
        return returnKey.trim();
    }

    /**
     * This method will align the XML in the Pretty Print Format
     * Returns the formatted XML in a String
     *
     * @param xmlData The XML string
     * @param indent  The indent value
     * @return The XML string in pretty print format
     */
    public String prettyXMLformat(String xmlData, int indent) {

        try {
            Source xmlInput = new StreamSource(new StringReader(xmlData));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return "<textarea rows=\"20\" cols=\"40\" style=\"border:none;\">" + xmlOutput.getWriter().toString() + "</textarea>";
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * This method will align the JSON in the Pretty Print Format
     * Returns the formatted String, Input should be JSON String
     *
     * @param input The JSON String
     * @return The JSON String in pretty print format
     */
    public String prettyJsonFormat(Object input) {

        ObjectMapper objectMapper = new ObjectMapper();
        String formattedString = "";
        try {
            JsonNode tree = objectMapper.readTree(input.toString());
            formattedString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
        } catch (JsonProcessingException e) {
            try {
                formattedString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return formattedString;
    }

    /**
     * This method will compare the Expected and Actual Values for the Custom Reports
     * Will Update the Test Status to Pass/ Fail accordingly
     * It will update the Comments for the Failed Ones in the Custom Reports
     *
     * @param commonPojo the beans class
     * @return returns the final Status of the Test Case
     */
    public List<String> finalStatustoReports(CommonPojo commonPojo) {

        List<String> finalStatus = new ArrayList<>();
        if (commonPojo.getTestStatus().contains("FAIL")) {
            finalStatus.add(this.badge2 + "FAIL" + this.badge5);
            finalStatus.add(commonPojo.getComments().toString());
        } else {
            finalStatus.add(this.badge7 + "PASS" + this.badge5);
            finalStatus.add(null);
        }
        return finalStatus;
    }
}
