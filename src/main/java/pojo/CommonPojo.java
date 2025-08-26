package pojo;

import java.util.ArrayList;
import java.util.List;

public class CommonPojo {

    public final List<String> rptHttpMethod;
    public final List<String> rptEndpoint;
    public final List<Double> rptResponseTime;
    public final List<String> rptExpStatusCode;
    public final List<String> rptActStatusCode;
    public final List<String> rptTestStatus;
    public final List<String> rptComments;
    private String suiteTestType;
    private List<String> suiteApiURI;
    private String testMethodName;
    private List<String> httpMethod;
    private List<String> endpoint;
    private List<Double> responseTime;
    private List<String> expStatusCode;
    private List<String> actStatusCode;
    private List<String> testStatus;
    private List<String> comments;
    private String testScenario;

    public CommonPojo() {

        this.rptHttpMethod = new ArrayList<>();
        this.rptEndpoint = new ArrayList<>();
        this.rptResponseTime = new ArrayList<>();
        this.rptExpStatusCode = new ArrayList<>();
        this.rptActStatusCode = new ArrayList<>();
        this.rptTestStatus = new ArrayList<>();
        this.rptComments = new ArrayList<>();
    }

    public String getSuiteTestType() {
        return suiteTestType;
    }

    public void setSuiteTestType(final String suiteTestType) {
        this.suiteTestType = suiteTestType;
    }

    public List<String> getSuiteApiURI() {
        return suiteApiURI;
    }


    public void setSuiteApiURI(List<String> suiteApiURI) {
        this.suiteApiURI = suiteApiURI;
    }

    public String getTestMethodName() {
        return this.testMethodName;
    }

    public void setTestMethodName(final String testMethodName) {
        this.testMethodName = testMethodName;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(List<String> httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<String> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<String> endpoint) {
        this.endpoint = endpoint;
    }

    public List<Double> getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(List<Double> responseTime) {
        this.responseTime = responseTime;
    }

    public List<String> getExpStatusCode() {
        return expStatusCode;
    }

    public void setExpStatusCode(List<String> expStatusCode) {
        this.expStatusCode = expStatusCode;
    }

    public List<String> getActStatusCode() {
        return actStatusCode;
    }

    public void setActStatusCode(List<String> actStatusCode) {
        this.actStatusCode = actStatusCode;
    }

    public List<String> getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(List<String> testStatus) {
        this.testStatus = testStatus;
    }

    public String getTestScenario() {
        return testScenario;
    }

    public void setTestScenario(String testScenario) {
        this.testScenario = testScenario;
    }



}