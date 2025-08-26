package models.gettoken;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Headers{

	@JsonProperty("Content-Type")
	private String contentType;

	public String getContentType(){
		return contentType;
	}
}