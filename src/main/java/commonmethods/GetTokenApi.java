package commonmethods;

import com.fasterxml.jackson.databind.ObjectMapper;
import apicoreutils.ApiCommonActions;
import models.gettoken.GetTokenRequest;
import models.loginapi.LoginApiResponse;
import pojo.CommonPojo;
import pojo.ServiceRequest;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


public class GetTokenApi {

    public static String token = "";
    protected static final Logger logger = Logger.getLogger(GetTokenApi.class);
    protected static ApiCommonActions api;
    protected CommonPojo commonPojo;

    public GetTokenApi(CommonPojo commonPojo) {
        this.commonPojo = commonPojo;
        api = new ApiCommonActions(commonPojo);

    }
    public String getToken(String path)  {
        System.out.println(path);
        byte[] jsonData = new byte[0];
        GetTokenRequest tokenRequest = null;
        try {
            jsonData = Files.readAllBytes(Paths.get(path));
            ObjectMapper objectMapper = new ObjectMapper();
            tokenRequest = objectMapper.readValue(jsonData, GetTokenRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Response response = null;
        ServiceRequest serviceRequest = new ServiceRequest();
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", tokenRequest.getHeaders().getContentType());
        serviceRequest.setHeaders(headers);
        serviceRequest.setEndpoint(tokenRequest.getEndpoint());
        serviceRequest.setHttpMethod(tokenRequest.getHttpmethod());
        serviceRequest.setUri(tokenRequest.getUri());
        serviceRequest.setPayload(tokenRequest.getPayload());
        api.setServiceRequest(serviceRequest);
        try {
            response = api.sendCustomRequest(tokenRequest.getStatuscode());
        } catch (Exception e) {
            logger.info("Failed to create request");
        }
        LoginApiResponse loginApiResponse = response.as(LoginApiResponse.class);
        token = loginApiResponse.getData().getToken();
        return token;
    }

}
