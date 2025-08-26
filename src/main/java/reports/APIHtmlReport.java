package reports;

import constants.CommonConstants;
import pojo.CommonPojo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class APIHtmlReport extends ReportManager {

    public static final String HTML_HEAD_WITH_STYLE = "<head> <meta charset=\"UTF-8\"><title>API Emailable Report</title>    <style>         table {             border-collapse: collapse;             border: 1px solid #e9eaec;            width: 100%;            background-color:#E3E3E3;font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }          td, th {            padding: 9px;             border: 2px solid #e9eaec; font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }          td {            font-size: 12px;            text-align: center;            background-color:white;        }        th {            text-align: center;            font-size: 12px;            font-weight: normal;         }        .labels{             font-weight: bold; font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }         .testlabel{             font-weight: bold; font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;            background-color:#e7dcf2;        }         .headlabels{             font-weight: bold;             background-color:#d3dee3cc;            text-align: center;            font-size: 12px;font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }          .label tr td label {             display: block;         }              </style> </head> ";
    private static final Logger log = Logger.getLogger(APIHtmlReport.class);
    private static final String HTML_START = "<html>";
    private static final String HTML_END = "</html>";
    private static final String TABLE_START = "<table>";
    private static final String TABLE_END = "</table>";
    private static final String HEADER_END = "</th>";
    private static final String ROW_START = "<tr>";
    private static final String ROW_END = "</tr>";
    private static final String COLUMN_START = "<td>";
    private static final String COLUMN_END = "</td>";
    private static final String COLUMN_START1 = "<td rowspan= ";
    private static StringBuilder table;
    private static FileWriter apiHTMLWriter;
    private static BufferedWriter apiHTMLBufferWriter;
    private int columns;

    /**
     * This method is to append the Font to the Api HTML report
     *
     * @param value the required data
     * @param color the required color
     * @return returns the string that appends value with required color
     */
    public static String appendFont(final String value, final HtmlColor color) {
        log.info("Appending Font");
        final StringBuilder sb = new StringBuilder();
        sb.append("<font color=");
        switch (color) {
            case GREEN:
                sb.append("'green'");
                break;
            case BLUE:
                sb.append("blue");
                break;
            case RED:
                sb.append("red");
                break;
            case BROWN:
                sb.append("brown");
                break;
            case BLACK:
                sb.append("black");
                break;
            default:
                sb.append("black");
                break;
        }
        sb.append(" size = '" + value + "'");
        sb.append(">");
        sb.append("<b>");
        sb.append("</b>");
        sb.append("</font>");
        log.info("Font Appended");
        return sb.toString();
    }

    /**
     * This method is to start the API HTML report
     *
     * @param file the API HTML report file
     */
    public synchronized void apiHtmlReportStart(File file) {

        try {
            (apiHTMLWriter = new FileWriter(file)).flush();
            apiHTMLBufferWriter = new BufferedWriter(apiHTMLWriter);
            apiHTMLWriter.flush();
            apiHTMLBufferWriter.flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * This method is to add the Custom Title and Labels to the API HTML Report
     *
     * @param columns    the number of columns
     * @param testName   the test name
     * @param apiURL     the API URL
     * @param suitelabel the suite name
     * @param BuildNum   the build number
     */
    public synchronized void apiCustomReportHeader(final int columns, final String testName, final String apiURL, final String suitelabel, final String BuildNum) {
        APIHtmlReport.table = new StringBuilder();
        log.info("Initializing Table for API Custom Report");
        this.columns = columns;
        APIHtmlReport.table.append(HTML_START);
        APIHtmlReport.table.append("<head> <meta charset=\"UTF-8\"><title>API Custom Report</title>    <style>         table {             border-collapse: collapse;             border: 1px solid #e9eaec;            width: 100%;            background-color:#E3E3E3;font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }          td, th {            padding: 9px;             border: 2px solid #e9eaec; font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }          td {            font-size: 12px;            text-align: center;            background-color:white;        }        th {            text-align: center;            font-size: 12px;            font-weight: normal;         }        .labels{             font-weight: bold; font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }         .testlabel{             font-weight: bold; font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;            background-color:#e7dcf2;        }         .headlabels{             font-weight: bold;             background-color:#d3dee3cc;            text-align: center;            font-size: 12px;font-family: Roboto,-apple-system, system-ui, BlinkMacSystemFont, \"Segoe UI\", \"Helvetica Neue\", Arial, sans-serif;\ncolor: #585858;        }          .label tr td label {             display: block;         }              </style> </head> ");
        APIHtmlReport.table.append(TABLE_START);
        APIHtmlReport.table.append(TABLE_END);
        APIHtmlReport.table.append(HTML_END);
        this.addApiTableHeader(suitelabel, testName, apiURL, BuildNum, CommonConstants.API_HEADER_VALUES.toArray(new String[CommonConstants.API_HEADER_VALUES.size()]));
    }

    /**
     * This method is to add the Table Headers to the API HTML report
     * The Headers are defined from the Constants file
     *
     * @param suitelabel the suite name
     * @param testName   the test name
     * @param apiURL     the API URL
     * @param BuildNum   the build number
     * @param values     the data to append to the API HTML report
     */
    public void addApiTableHeader(final String suitelabel, final String testName, final String apiURL, final String BuildNum, final String... values) {
        log.info("Adding Table Header");
        if (values.length != this.columns) {
            log.error("Error column lenth");
        } else {
            final int lastIndex = APIHtmlReport.table.lastIndexOf(TABLE_END);
            if (lastIndex > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(ROW_START);
                sb.append("<tr>        <th class=\"labels\" colspan=\"8\"><ul style=\"list-style-type:none;\">\n<li style=\"text-align: center;\">Suite Name : " + suitelabel + "</li>\n  <li style=\"text-align: center;\">Test Environment Details:</li>\n\n  <li style=\"text-align: center;\">OS = " + System.getProperty("os.name") + "</li>\n  <li style=\"text-align: center;\">TestNg Version =  7.1.0</li>\n  <li style=\"text-align: center;\">RestAssured Version =  5.3.0</li>\n  <li style=\"text-align: center;\">ExecutedBy =  " + System.getProperty("user.name") + "</li>\n  <li style=\"text-align: center;\">Build Number =  " + BuildNum + "</li>\n</ul>          </th></tr>");
                sb.append("<tr>        <th class=\"testlabel\" colspan= \"4\"><label>Test Name : " + testName + "</label> </th>        <th class=\"testlabel\" colspan= \"4\"><label>URI : " + apiURL + "</label> </th>    </tr>");
                for (final String value : values) {
                    sb.append("<th class=\"labels\">");
                    sb.append(value);
                    sb.append(HEADER_END);
                }
                sb.append(ROW_END);
                APIHtmlReport.table.insert(lastIndex, sb.toString());
                log.info("Table Header Added");
            }
        }

    }

    /**
     * This method is to add the Header for each set of Test Cases to the Api HTML report
     *
     * @param rowLabel the row Label in API HTML report
     */
    public void addApiRowHeader(final String rowLabel) {
        log.info("Adding Row Header");
        if (rowLabel.isEmpty()) {
            log.error("Label Cannot be Empty");
        } else {
            final int lastIndex = APIHtmlReport.table.lastIndexOf(ROW_END);
            if (lastIndex > 0) {
                final int index = lastIndex + ROW_END.length();
                final StringBuilder sb = new StringBuilder();
                sb.append("<tr>        <th class=\"headlabels\" colspan=\"8\">            <label>" + rowLabel + "</label>\n        </th>    </tr>");
                APIHtmlReport.table.insert(index, sb.toString());
                log.info("Row Header Addded");
            }
        }
    }

    /**
     * This method is to add the Values for each test case
     *
     * @param tc the beans class--collecting the values from the beans class
     */
    public void addApiRowValues(final CommonPojo tc) {

        List<String> finalstatus = finalStatustoReports(tc);

        log.info("Adding Row values");
        final int lastIndex = APIHtmlReport.table.lastIndexOf(ROW_END);
        if (lastIndex > 0) {
            final int index = lastIndex + ROW_END.length();
            final StringBuilder sb = new StringBuilder();
            for (int size = tc.getHttpMethod().size(), i = 0; i < size; ++i) {
                if (i == 0) {
                    sb.append("<tr><td rowspan= " + size + "> " + tc.getTestMethodName() + COLUMN_END);
                    sb.append(COLUMN_START1 + size + "> " + tc.getTestScenario() + COLUMN_END);
                    sb.append(COLUMN_START + tc.getHttpMethod().get(i) + " : " + tc.getEndpoint().get(i) + COLUMN_END);
                    sb.append(COLUMN_START + tc.getExpStatusCode().get(i) + COLUMN_END);
                    sb.append(COLUMN_START + tc.getActStatusCode().get(i) + COLUMN_END);
                    sb.append(COLUMN_START + tc.getResponseTime().get(i) + COLUMN_END);
                    sb.append(COLUMN_START1 + size + ">" + finalstatus.get(0) + COLUMN_END);
                    sb.append(COLUMN_START1 + size + ">" + finalstatus.get(1) + "</td></tr>");
                } else {
                    sb.append("<tr><td>" + tc.getHttpMethod().get(i) + " : " + tc.getEndpoint().get(i) + COLUMN_END);
                    sb.append(COLUMN_START + tc.getExpStatusCode().get(i) + COLUMN_END);
                    sb.append(COLUMN_START + tc.getActStatusCode().get(i) + COLUMN_END);
                    sb.append(COLUMN_START + tc.getResponseTime().get(i) + "</td></tr>");
                }
            }
            APIHtmlReport.table.insert(index, sb.toString());
            log.info("Row Values added");
        }
    }

    /**
     * This method is to build the Table Data in the form of String
     *
     * @return the API HTML report to build
     */
    public String build() {
        log.info("Building Table as String");
        return APIHtmlReport.table.toString();
    }

    /**
     * This method is to end and flush the data to the Api HTML report
     */
    public void apiHtmlReportBuild() {

        try {
            APIHtmlReport.apiHTMLBufferWriter.write(build());
            APIHtmlReport.apiHTMLBufferWriter.flush();
            APIHtmlReport.apiHTMLWriter.flush();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public enum HtmlColor {
        GREEN,
        BLUE,
        RED,
        BROWN,
        BLACK;
    }
}
