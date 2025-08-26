package reports;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CustomHtmlReport extends ReportManager {

    private static final String CUSTOM_REPORT_END = "    </tr>\n  </tbody>\n</table></body>";
    private static final String CUSTOM_COLUMN_START = "        <td>  ";
    private static final String CUSTOM_COLUMN_END = "  </td>\n";
    private static final List<String> customHtmlData = new ArrayList<>();
    private static FileWriter customHTMLWriter;
    private static BufferedWriter customHTMLBufferWriter;
    protected List<String> customHeader = new ArrayList<>();

    /**
     * This method is to end the Custom HTML reports
     *
     * @return ends the custom HTML report
     */
    public static String customHtmlReportEnd() {
        return CUSTOM_REPORT_END;
    }

    /**
     * This Method will Flush the Data to the Custom Reports
     *
     * @param data Data to add to the custom reports
     */
    public void addToCustomReport(final String data) {
        try {
            CustomHtmlReport.customHTMLBufferWriter.write(data);
            CustomHtmlReport.customHTMLBufferWriter.flush();
            CustomHtmlReport.customHTMLWriter.flush();
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * This Method will add the Headers to the Custom Reports
     * Need to send the Header Values at the Test Case Level
     *
     * @param values the headers to log in custom reports
     */
    private void addHeaderToCustomReport(final String... values) {

        List<String> headerValues = new ArrayList<>();
        Collections.addAll(headerValues, values);

        if (!headerValues.equals(customHeader)) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("<head>\n<style>.button {\n         background-color: #4CAF50; /* Green */\n         border: none;\n         color: white;\n         padding: 12px;\n         text-align: center;\n         text-decoration: none;\n         display: inline-block;\n         font-size: 14px;\n         margin: 10px 25px;\n         transition-duration: 0.4s;\n         cursor: pointer;\n         width: 90%;\n         }\n         .button:hover {\n         background-color: blue; /* Green */\n         color: white;\n         }\n         #summary {\nfont-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;         border-collapse: collapse;\n         width: 90%;\n         margin: 10px 25px;\n         }\n         #summary td, #summary th {\n         padding: 10px;\n         text-align: center;\n         border: 2px solid #e9eaec;\n         }\n         #summary th {\n         padding-top: 8px;\n         padding-bottom: 8px;\n         text-align: center;\n         background-color: #c3ccce80;\n         color: #585858;\n         border: 2px solid #e9eaec;\n         }\n         .button1 {border-radius: 12px;}\n         .button2 {border-radius: 12px;}</style>\n</head>\n<body>\n<table id=\"summary\">\n  <tbody>\n    <tr>\n\n");
            buffer.append("      <th >S.No</th>\n");
            buffer.append("      <th >Test Name</th>\n");
            for (final String value : values) {

                buffer.append("      <th >" + value + "</th>\n");
            }
            buffer.append("      <th > Test Status</th>\n");
            buffer.append("      <th > Comments</th>\n");
            buffer.append("    </tr>\n    <tr>\n");
            this.addToCustomReport(buffer.toString());
            try {
                CustomHtmlReport.customHTMLWriter.flush();
            } catch (IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        }

        customHeader.clear();
        customHeader.addAll(headerValues);

    }

    /**
     * This Method will add the Data under the Headers to the Custom Reports
     * Need to send the Data at the Test Case Level in the Finally Block
     *
     * @param values the data to log in custom reports
     */
    public void addRowToCustomReport(final String... values) {

        CustomHtmlReport.customHtmlData.add("\n");
        this.addToCustomReport("<tr>");
        this.addToCustomReport(CUSTOM_COLUMN_START + sno.getAndIncrement() + CUSTOM_COLUMN_END);
        this.addToCustomReport(CUSTOM_COLUMN_START + this.commonPojo.getTestMethodName() + CUSTOM_COLUMN_END);
        for (final String value : values) {
            CustomHtmlReport.customHtmlData.add(value + ",");
            this.addToCustomReport(CUSTOM_COLUMN_START + value + CUSTOM_COLUMN_END);
        }

        List<String> finalstatus = finalStatustoReports(this.commonPojo);

        this.addToCustomReport(CUSTOM_COLUMN_START + finalstatus.get(0) + CUSTOM_COLUMN_END);
        this.addToCustomReport(CUSTOM_COLUMN_START + finalstatus.get(1) + CUSTOM_COLUMN_END);

        this.addToCustomReport("</tr>");

    }

    /**
     * This method is to Start the Custom HTML Reports
     *
     * @param file the custom HTML report file
     */
    public synchronized void customHtmlReportStart(File file) {

        try {
            CustomHtmlReport.customHTMLWriter = new FileWriter(file);
            CustomHtmlReport.customHTMLWriter.flush();
            CustomHtmlReport.customHTMLBufferWriter = new BufferedWriter(CustomHtmlReport.customHTMLWriter);
            CustomHtmlReport.customHTMLWriter.flush();
            CustomHtmlReport.customHTMLBufferWriter.flush();
        } catch (IOException e) {

            logger.error(ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * This Method will Copy all the Test Data Headers to Custom Reports except "TestCase" and "ExecuteTest" columns
     *
     * @param map the test data in the form of map
     * @return returns the data to log into the custom reports
     */
    private List<String> customRptHeadersfromTestData(Map<String, Object> map) {

        List<String> headers = new ArrayList<>();
        List<String> data = new ArrayList<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase("TestCase") && !entry.getKey().equalsIgnoreCase("ExecuteTest")) {
                headers.add(entry.getKey());
                data.add(entry.getValue().toString());
            }
        }
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).equals("TestScenario")) {

                headers.add(0, headers.get(i));
                headers.remove(i + 1);
                data.add(0, data.get(i));
                data.remove(i + 1);
            }
        }
        Object[] objArr = headers.toArray();
        String[] customHeaders = Arrays.copyOf(objArr, objArr.length, String[].class);

        addHeaderToCustomReport(customHeaders);
        return data;
    }

    /**
     * This Method copy all the Headers and Data to the Custom Reports
     * except the "TestCase" and "ExecuteTest" columns
     * Need to call this method in the FINALLY block in @Test Method
     *
     * @param map the test data
     */
    public void customReportfromTestData(Map<String, Object> map) {

        List<String> data = customRptHeadersfromTestData(map);

        Object[] objArr = data.toArray();
        String[] customData = Arrays.copyOf(objArr, objArr.length, String[].class);

        addRowToCustomReport(customData);
    }
}