package pojo;

import com.google.protobuf.Message;

import java.util.Map;

public class ServiceRequest {

    private String httpMethod;
    private String uri;
    private String endpoint;
    private Map<String, Object> headers;
    private Map<String, Object> cookies;
    private Map<String, Object> queryparams;
    private Map<String, Object> formparams;
    private Map<String, Object> pathparams;
    private Object payload;
    private Object query;

    private Message protoRequest;

    public Message getProtoRequest() {
        return protoRequest;
    }

    public void setProtoRequest(Message protoRequest) {
        this.protoRequest = protoRequest;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUri() {
        return uri;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, Object> cookies) {
        this.cookies = cookies;
    }

    public Map<String, Object> getQueryparams() {
        return queryparams;
    }

    public void setQueryparams(Map<String, Object> queryparams) {
        this.queryparams = queryparams;
    }

    public Map<String, Object> getFormparams() {
        return formparams;
    }

    public void setFormparams(Map<String, Object> formparams) {
        this.formparams = formparams;
    }

    public Map<String, Object> getPathparams() {
        return pathparams;
    }

    public void setPathparams(Map<String, Object> pathparams) {
        this.pathparams = pathparams;
    }

    public Object getPayload() {
        return payload;
    }
    public Object getQuery() {
        return query;
    }



    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public void setQuery(Object query) {
        this.query = query;
    }



}
