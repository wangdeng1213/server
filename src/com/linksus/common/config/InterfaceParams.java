package com.linksus.common.config;

public class InterfaceParams{

	private String name;
	private String required;
	private String dataType;
	private String paramDisp;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getRequired(){
		return required;
	}

	public void setRequired(String required){
		this.required = required;
	}

	public String getDataType(){
		return dataType;
	}

	public void setDataType(String dataType){
		this.dataType = dataType;
	}

	public String getParamDisp(){
		return paramDisp;
	}

	public void setParamDisp(String paramDisp){
		this.paramDisp = paramDisp;
	}
}
