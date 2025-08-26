package models.gettoken;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Payload{

	@JsonProperty("password")
	private String password;

	@JsonProperty("username")
	private String username;

	public String getPassword(){
		return password;
	}

	public String getUsername(){
		return username;
	}
}