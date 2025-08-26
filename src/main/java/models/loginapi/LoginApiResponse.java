package models.loginapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginApiResponse{

	@JsonProperty("data")
	private Data data;

	@JsonProperty("status")
	private String status;

	public Data getData(){
		return data;
	}

	public String getStatus(){
		return status;
	}
}