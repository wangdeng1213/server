package com.linksus.entity;

public class LetterData{

	private String longitude;
	private String latitude;
	private String vfid;
	private String tovfid;
	private String subtype;
	private String key;

	public String getLongitude(){
		return longitude;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getVfid(){
		return vfid;
	}

	public void setVfid(String vfid){
		this.vfid = vfid;
	}

	public String getTovfid(){
		return tovfid;
	}

	public void setTovfid(String tovfid){
		this.tovfid = tovfid;
	}

	public String getSubtype(){
		return subtype;
	}

	public void setSubtype(String subtype){
		this.subtype = subtype;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

}
