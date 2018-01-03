package com.linksus.entity;

public class LetterReadDTO{

	private String id;
	private String type;
	private Long receiver_id;
	private Long sender_id;
	private String created_at;
	private String text;
	private LetterData data;

	private Long accountId;
	private String token;
	private Long institutionId;

	private String request;
	private String error_code;
	private String error;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public Long getReceiver_id(){
		return receiver_id;
	}

	public void setReceiver_id(Long receiverId){
		receiver_id = receiverId;
	}

	public Long getSender_id(){
		return sender_id;
	}

	public void setSender_id(Long senderId){
		sender_id = senderId;
	}

	public String getCreated_at(){
		return created_at;
	}

	public void setCreated_at(String createdAt){
		created_at = createdAt;
	}

	public String getText(){
		return text;
	}

	public void setText(String text){
		this.text = text;
	}

	public LetterData getData(){
		return data;
	}

	public void setData(LetterData data){
		this.data = data;
	}

	public String getRequest(){
		return request;
	}

	public void setRequest(String request){
		this.request = request;
	}

	public String getError_code(){
		return error_code;
	}

	public void setError_code(String errorCode){
		error_code = errorCode;
	}

	public String getError(){
		return error;
	}

	public void setError(String error){
		this.error = error;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}
}
