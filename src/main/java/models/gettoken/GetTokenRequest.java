package models.gettoken;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTokenRequest {

	@JsonProperty("statuscode")
	private String statuscode;

	@JsonProperty("headers")
	private Headers headers;

	@JsonProperty("endpoint")
	private String endpoint;

	@JsonProperty("payload")
	private Payload payload;

	@JsonProperty("httpmethod")
	private String httpmethod;

	@JsonProperty("uri")
	private String uri;

	public String getStatuscode(){
		return statuscode;
	}

	public Headers getHeaders(){
		return headers;
	}

	public String getEndpoint(){
		return endpoint;
	}

	public Payload getPayload(){
		return payload;
	}

	public String getHttpmethod(){
		return httpmethod;
	}

	public String getUri(){
		return uri;
	}
}