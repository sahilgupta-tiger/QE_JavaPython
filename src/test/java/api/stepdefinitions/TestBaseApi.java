package api.stepdefinitions;

import allurereportGeneration.allureReportGeneration;
import apicoreutils.ApiCoreModel;
import apicoreutils.ExcelDatafetcher;
import apicoreutils.ReadApiTestData;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class TestBaseApi {

    protected Map<String, Object> datavalues = new HashMap<>();
    protected JSONObject responseJson = new JSONObject();
    protected static final ReadApiTestData readApiTestData = new ReadApiTestData();
    protected static String Workbook;
    protected static String Worksheet;
    protected ExcelDatafetcher excelDatafetcher=new ExcelDatafetcher();
    protected static allureReportGeneration alluresetTestcase = new allureReportGeneration();
    protected static final ApiCoreModel apiCoreModel = new ApiCoreModel();

}
