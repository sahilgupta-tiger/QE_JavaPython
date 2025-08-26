package models.loginapi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionDataItem{

	@JsonProperty("is_default_region")
	private int isDefaultRegion;

	@JsonProperty("region_name")
	private String regionName;

	@JsonProperty("region_code")
	private String regionCode;

	public int getIsDefaultRegion(){
		return isDefaultRegion;
	}

	public String getRegionName(){
		return regionName;
	}

	public String getRegionCode(){
		return regionCode;
	}
}