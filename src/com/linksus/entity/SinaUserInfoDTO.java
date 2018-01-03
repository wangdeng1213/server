package com.linksus.entity;

public class SinaUserInfoDTO{

	private String id;
	private String idstr;
	private String screen_name;
	private String name;
	private String province;
	private String city;
	private String location;
	private String description;
	private String url;
	private String profile_image_url;
	private String domain;
	private String gender;
	private Integer followers_count;
	private Integer friends_count;
	private Integer statuses_count;
	private Integer favourites_count;
	private String created_at;
	private Boolean following;
	private Boolean allow_all_act_msg;
	private Boolean geo_enabled;
	private Boolean verified;

	private Boolean allow_all_comment;
	private String avatar_large;
	private String verified_reason;
	private Boolean follow_me;
	private Integer online_status;
	private Integer bi_followers_count;

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getIdstr(){
		return idstr;
	}

	public void setIdstr(String idstr){
		this.idstr = idstr;
	}

	public String getScreen_name(){
		return screen_name;
	}

	public void setScreen_name(String screenName){
		screen_name = screenName;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getProvince(){
		return province;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getCity(){
		return city;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getUrl(){
		return url;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getProfile_image_url(){
		return profile_image_url;
	}

	public void setProfile_image_url(String profileImageUrl){
		profile_image_url = profileImageUrl;
	}

	public String getDomain(){
		return domain;
	}

	public void setDomain(String domain){
		this.domain = domain;
	}

	public String getGender(){
		return gender;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public Integer getFollowers_count(){
		return followers_count;
	}

	public void setFollowers_count(Integer followersCount){
		followers_count = followersCount;
	}

	public Integer getFriends_count(){
		return friends_count;
	}

	public void setFriends_count(Integer friendsCount){
		friends_count = friendsCount;
	}

	public Integer getStatuses_count(){
		return statuses_count;
	}

	public void setStatuses_count(Integer statusesCount){
		statuses_count = statusesCount;
	}

	public Integer getFavourites_count(){
		return favourites_count;
	}

	public void setFavourites_count(Integer favouritesCount){
		favourites_count = favouritesCount;
	}

	public String getCreated_at(){
		return created_at;
	}

	public void setCreated_at(String createdAt){
		created_at = createdAt;
	}

	public Boolean getFollowing(){
		return following;
	}

	public void setFollowing(Boolean following){
		this.following = following;
	}

	public Boolean getAllow_all_act_msg(){
		return allow_all_act_msg;
	}

	public void setAllow_all_act_msg(Boolean allowAllActMsg){
		allow_all_act_msg = allowAllActMsg;
	}

	public Boolean getGeo_enabled(){
		return geo_enabled;
	}

	public void setGeo_enabled(Boolean geoEnabled){
		geo_enabled = geoEnabled;
	}

	public Boolean getVerified(){
		return verified;
	}

	public void setVerified(Boolean verified){
		this.verified = verified;
	}

	public Boolean getAllow_all_comment(){
		return allow_all_comment;
	}

	public void setAllow_all_comment(Boolean allowAllComment){
		allow_all_comment = allowAllComment;
	}

	public String getAvatar_large(){
		return avatar_large;
	}

	public void setAvatar_large(String avatarLarge){
		avatar_large = avatarLarge;
	}

	public String getVerified_reason(){
		return verified_reason;
	}

	public void setVerified_reason(String verifiedReason){
		verified_reason = verifiedReason;
	}

	public Boolean getFollow_me(){
		return follow_me;
	}

	public void setFollow_me(Boolean followMe){
		follow_me = followMe;
	}

	public Integer getOnline_status(){
		return online_status;
	}

	public void setOnline_status(Integer onlineStatus){
		online_status = onlineStatus;
	}

	public Integer getBi_followers_count(){
		return bi_followers_count;
	}

	public void setBi_followers_count(Integer biFollowersCount){
		bi_followers_count = biFollowersCount;
	}
}
