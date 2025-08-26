package models.loginapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Data{

	@JsonProperty("region_data")
	private List<RegionDataItem> regionData;

	@JsonProperty("region_name")
	private String regionName;

	@JsonProperty("expiry")
	private long expiry;

	@JsonProperty("token")
	private String token;

	@JsonProperty("region_code")
	private String regionCode;

	@JsonProperty("username")
	private String username;

	public List<RegionDataItem> getRegionData(){
		return regionData;
	}

	public String getRegionName(){
		return regionName;
	}

	public long getExpiry(){
		return expiry;
	}

	public String getToken(){
		return token;
	}

	public String getRegionCode(){
		return regionCode;
	}

	public String getUsername(){
		return username;
	}
}