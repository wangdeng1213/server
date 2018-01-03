package com.linksus.common.module;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.entity.LinksusRelationWeibouser;

public class CopyMapToBean{

	protected static final Logger logger = LoggerFactory.getLogger(WeiboUserCommon.class);

	//更新腾讯数据到微博用户表中
	public static void copyTencentUserMapToBean(Map infoMap,LinksusRelationWeibouser linkWeiboUser){
		CacheUtil cache = CacheUtil.getInstance();
		/** 新浪腾讯微博用户标识 用于API请求  */
		linkWeiboUser.setRpsId((String) infoMap.get("openid") == null ? "" : (String) infoMap.get("openid"));
		/** 用户类型 1:新浪 2 腾讯 */
		linkWeiboUser.setUserType(2);
		/** 人员ID:关联人员主表 */
		//linkWeiboUser.setPersonId(infoMap.get());
		/** 用户昵称 */
		linkWeiboUser.setRpsScreenName((String) infoMap.get("nick") == null ? "" : (String) infoMap.get("nick"));
		/** 友好显示名称|用户账户名 */
		linkWeiboUser.setRpsName((String) infoMap.get("name") == null ? "" : (String) infoMap.get("name"));
		/** 用户所在国家ID */
		String countryCode = (String) infoMap.get("country_code");
		if(StringUtils.isNotBlank(countryCode) && !StringUtils.equals(countryCode, "0")){
			linkWeiboUser.setCountryCode(cache.getTencentAreaCode(countryCode));
		}else{
			linkWeiboUser.setCountryCode("");
			linkWeiboUser.setRpsProvince("");
			linkWeiboUser.setRpsCity("");
		}
		/** 用户所在地区ID */
		String rpsProvince = (String) infoMap.get("province_code");
		if(StringUtils.isNotBlank(rpsProvince) && !StringUtils.equals(rpsProvince, "0")){
			linkWeiboUser.setRpsProvince(cache.getTencentAreaCode(countryCode + "-" + rpsProvince));
		}else{
			rpsProvince = "";
			linkWeiboUser.setRpsProvince("");
		}

		/** 用户所在城市ID */
		String city = (String) infoMap.get("city_code");
		if(StringUtils.isNotBlank(city) && !StringUtils.equals(city, "0")){
			if(rpsProvince.equals("")){
				city = countryCode + "-" + infoMap.get("city_code").toString();
				linkWeiboUser.setRpsCity(cache.getTencentAreaCode(city));
			}else{
				city = countryCode + "-" + rpsProvince + "-" + infoMap.get("city_code").toString();
				linkWeiboUser.setRpsCity(cache.getTencentAreaCode(city));
			}
		}else{
			linkWeiboUser.setRpsCity("");
		}

		if(StringUtils.isBlank(linkWeiboUser.getRpsProvince()) && !StringUtils.isBlank(linkWeiboUser.getRpsCity())){
			linkWeiboUser.setRpsProvince(linkWeiboUser.getRpsCity());
			linkWeiboUser.setRpsCity("");
		}
		/** 用户所在地 */
		linkWeiboUser.setRpsLocation((String) infoMap.get("location") == null ? "" : (String) infoMap.get("location"));
		/** 用户描述|个人介绍 */
		if(infoMap.containsKey("introduction")){
			linkWeiboUser.setRpsDescription((String) infoMap.get("introduction") == null ? "" : (String) infoMap
					.get("introduction"));
		}
		/** 用户博客地址|个人主页 */
		linkWeiboUser.setRpsUrl((String) infoMap.get("homepage") == null ? "" : (String) infoMap.get("homepage"));
		/** 用户头像地址 */
		linkWeiboUser
				.setRpsProfileImageUrl(StringUtils.isBlank((String) infoMap.get("head")) ? "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png"
						: (String) infoMap.get("head") + "/120");
		/** 生日 */
		//linkWeiboUser.setBirthDay((String)infoMap.get("birth_year") +(String)infoMap.get("birth_month") +(String)infoMap.get("birth_day"));
		Integer birth_year = infoMap.get("birth_year") == null ? 0 : (Integer) infoMap.get("birth_year");
		Integer birth_month = infoMap.get("birth_month") == null ? 0 : (Integer) infoMap.get("birth_month");
		Integer birth_day = infoMap.get("birth_day") == null ? 0 : (Integer) infoMap.get("birth_day");
		StringBuffer buff = new StringBuffer();
		if(birth_year != 0){
			buff.append(birth_year).append("-");
			if(birth_month == 0 && birth_day == 0){
				buff.deleteCharAt(buff.length() - 1);
			}
		}
		if(birth_month != 0){
			buff.append(birth_month).append("-");
		}
		if(birth_day != 0){
			buff.append(birth_day);
		}
		linkWeiboUser.setBirthDay(buff.toString());
		/** 用户的个性化域名 */
		//不存在
		//linkWeiboUser.setRpsDomain(infoMap.get());
		/** 性别，m：男、f：女、n：未知  */
		String rpsGender = "n";
		if(infoMap.get("sex") != null){
			if(infoMap.get("sex").toString().equals("1")){
				rpsGender = "m";
			}else if(infoMap.get("sex").toString().equals("2")){
				rpsGender = "f";
			}
		}
		linkWeiboUser.setRpsGender(rpsGender);
		/** 粉丝数|听众数 */
		linkWeiboUser.setRpsFollowersCount(infoMap.get("fansnum") == null ? 0 : Integer.valueOf(infoMap.get("fansnum")
				+ ""));
		/** 关注数|收听的人数 */
		linkWeiboUser.setRpsFriendsCount(infoMap.get("idolnum") == null ? 0 : Integer.valueOf(infoMap.get("idolnum")
				+ ""));
		/** 用户的互粉数|互听好友数 */
		linkWeiboUser.setRpsBiFollowersCount(infoMap.get("mutual_fans_num") == null ? 0 : Integer.valueOf(infoMap
				.get("mutual_fans_num")
				+ ""));
		/** 微博数 */
		linkWeiboUser.setRpsStatusesCount(infoMap.get("tweetnum") == null ? 0 : Integer.valueOf(infoMap.get("tweetnum")
				+ ""));
		/** 收藏数 */
		linkWeiboUser.setRpsFavouritesCount(infoMap.get("favnum") == null ? 0 : Integer.valueOf(infoMap.get("favnum")
				+ ""));
		/** 创建时间 */
		linkWeiboUser
				.setRpsCreatedAt(infoMap.get("regtime") == null ? 0 : Integer.valueOf(infoMap.get("regtime") + ""));
		/** 是否允许所有人给我发私信 */
		linkWeiboUser.setRpsAllowAllActMsg(infoMap.get("send_private_flag") == null ? 0 : Integer.valueOf(infoMap
				.get("send_private_flag")
				+ ""));
		/** 是否允许带有地理信息 */
		//不存在
		//linkWeiboUser.setRpsGeoEnabled(infoMap.get());
		//是否企业机构
		Object isSendObj = infoMap.get("isent");
		//是否认证用户
		Object isVipObj = infoMap.get("isvip");
		Integer verifiedType = -1;
		if(isSendObj != null && isVipObj != null){
			Integer isSend = Integer.valueOf(isSendObj + "");
			Integer isVip = Integer.valueOf(isVipObj + "");
			if(isSend == 1 && isVip == 1){
				verifiedType = 30;
			}else if(isSend == 1 && isVip == 0){
				verifiedType = 31;
			}else if(isSend == 0 && isVip == 1){
				verifiedType = 32;
			}else if(isSend == 0 && isVip == 0){
				verifiedType = 33;
			}else{
				verifiedType = -1;
			}
		}

		/** 是否是微博认证用户，即带V用户 */
		linkWeiboUser.setRpsVerified(infoMap.get("isvip") == null ? 0 : new Integer(infoMap.get("isvip") + ""));
		/** 认证种类-0 黄V,  1-7 蓝V,10  微博女郎,  200 220 微博达人 | 30 企业实名认证 31 企业未实名认证 32 个人实名认证 33 个人未实名认证 */
		linkWeiboUser.setRpsVerifiedType(verifiedType);
		Integer verifiedTypeFlag = 1;
		if(verifiedType == 30){
			verifiedTypeFlag = 3;
		}else if(verifiedType == 32){
			verifiedTypeFlag = 2;
		}
		linkWeiboUser.setRpsVerifiedTypeFlag(verifiedTypeFlag);
		/** 是否允许所有人对我的微博进行评论 */
		//linkWeiboUser.setRpsAllowAllComment(infoMap.get());
		/** 用户大头像地址 */
		//linkWeiboUser.setRpsAvatarLarge(infoMap.get());
		/** 认证原因|认证信息 */
		linkWeiboUser.setRpsVerifiedReason((String) infoMap.get("verifyinfo") == null ? "" : infoMap.get("verifyinfo")
				.toString());
		/** 评论数 */
		//linkWeiboUser.setRpsCommentNum(infoMap.get());
		/** @我数 */
		//linkWeiboUser.setRpsAtmeNum(infoMap.get());
		/** 覆盖度 */
		//linkWeiboUser.setRpsCoverageNum(infoMap.get());
		/** 行业ID */
		linkWeiboUser.setIndustryCode(infoMap.get("industry_code") == null ? "" : infoMap.get("industry_code")
				.toString());
		/** 最后一条微博id */
		//Map tweetInfomap=(Map)infoMap.get("tweetinfo");
		//未作处理 
		//--String lastid=tweetInfomap.get("id").toString();
		//linkWeiboUser.setLastweiboid(lastid);
		/** 最新微博的发布时间 */
		//linkWeiboUser.setLastweibotime(infoMap.get());
		/** 用户标签 */
		//linkWeiboUser.setTags(tagValue);
		/** 用户质量:3高质量，2普通，1低质量，0未标记 */
		/*
		 * String fansQuality=""; Integer followersCount
		 * =Integer.parseInt((String
		 * )infoMap.get("fansnum")==null?"0":infoMap.get("fansnum").toString());
		 * Integer friendsCount
		 * =Integer.parseInt((String)infoMap.get("idolnum")==
		 * null?"0":infoMap.get("idolnum").toString()); String proFileImageUrl
		 * =infoMap.get("head").toString();
		 * if(Integer.valueOf(infoMap.get("isvip").toString())==1){ fansQuality
		 * = "3"; }else if(followersCount<100){////粉丝数是否<100 fansQuality = "1";
		 * }else if(followersCount>1000){////粉丝数是否>=1000 fansQuality = "3";
		 * }else if((friendsCount/followersCount)>20){////关注 除以 粉丝 比例 (20)
		 * fansQuality = "1"; }else
		 * if(proFileImageUrl.substring(0,proFileImageUrl
		 * .length()-7)=="/50/0/1"){//无头像的(/50/0/1) fansQuality = "1"; }else
		 * if((new Date().getTime()-new
		 * Long(infoMap.get("regtime").toString())>86400*30)){//最后一条微博的时间1个月之前
		 * fansQuality = "1"; }else{ fansQuality = "2"; }
		 */
		List eduList = (List) infoMap.get("edu");
		if(eduList != null && eduList.size() > 0){
			linkWeiboUser.setEduList(eduList);
		}
		List compList = (List) infoMap.get("comp");
		if(compList != null && compList.size() > 0){
			linkWeiboUser.setCompList(compList);
		}
		linkWeiboUser.setFansQuality(0);
		/** 微博等级 */
		linkWeiboUser.setWeiboLevel(infoMap.get("level") == null ? 0 : new Integer(infoMap.get("level").toString()));
		/** 同步时间 */
		linkWeiboUser.setSytime(Integer.parseInt(String.valueOf(new Date().getTime() / 1000)));
		/** 微博创建时间 */
		//linkWeiboUser.setWeiboCreateTime(infoMap.get());
		/** 微博同步时间 */
		//linkWeiboUser.setWeiboLastSytime(infoMap.get());
		/** 最新同步微博mid */
		//linkWeiboUser.setWeiboLastMid(infoMap.get());
	}

	//更新新浪数据到微博用户表中
	public static void copySinaUserMapToBean(Map infoMap,LinksusRelationWeibouser linkWeiboUser){
		CacheUtil cache = CacheUtil.getInstance();
		/** 新浪腾讯微博用户标识 用于API请求  */
		linkWeiboUser.setRpsId(infoMap.get("id") == null ? "" : infoMap.get("id") + "");
		/** 人员ID:关联人员主表 */
		//插入到人员表再作处理
		linkWeiboUser.setUserType(1);
		//linkWeiboUser.setPersonId(infoMap.get());
		/** 用户昵称 */
		linkWeiboUser.setRpsScreenName(infoMap.get("screen_name") == null ? "" : infoMap.get("screen_name") + "");
		/** 友好显示名称|用户账户名 */
		linkWeiboUser.setRpsName(infoMap.get("name") == null ? "" : infoMap.get("name") + "");

		/** 用户所在国家ID */
		String countryCode = "1";
		linkWeiboUser.setCountryCode(countryCode);
		/** 用户所在地区ID */
		String rpsProvince = "";
		String foreignCode = "";
		if(infoMap.get("province") != null){
			rpsProvince = infoMap.get("province").toString();
			if(StringUtils.isNotBlank(rpsProvince) && rpsProvince.equals("400")){
				foreignCode = infoMap.get("province").toString();
			}
			if(StringUtils.isNotBlank(rpsProvince) && !rpsProvince.equals("100") && !rpsProvince.equals("400")){
				linkWeiboUser.setRpsProvince(cache.getSinaAreaCode(countryCode + "-" + rpsProvince) == null ? "0"
						: cache.getSinaAreaCode(countryCode + "-" + rpsProvince));
			}else{
				rpsProvince = "0";
				linkWeiboUser.setRpsProvince("");
			}
		}else{
			rpsProvince = "0";
			linkWeiboUser.setRpsProvince("");
		}
		/** 用户所在城市ID */
		if(StringUtils.isNotBlank(foreignCode)){//海外城市处理
			String cityCode = foreignCode;
			String city = infoMap.get("city").toString();
			String realCity = "";
			if(StringUtils.isNotBlank(city) && !city.equals("1000")){
				cityCode = cityCode + "-" + infoMap.get("city").toString();
				realCity = cache.getSinaAreaCode(cityCode);
			}
			linkWeiboUser.setCountryCode(realCity);
			linkWeiboUser.setRpsProvince("");
			linkWeiboUser.setRpsCity("");
		}
		String rpsCity = (String) infoMap.get("city");
		if(!StringUtils.isBlank(rpsCity)){
			if(StringUtils.isNotBlank(rpsProvince) && !rpsProvince.equals("0") && StringUtils.isBlank(foreignCode)){
				String cityCode = countryCode + "-" + rpsProvince;
				if(StringUtils.isNotBlank(rpsCity) && !rpsCity.equals("1000")){
					cityCode = cityCode + "-" + rpsCity;
				}else{
					cityCode = countryCode + "-" + rpsProvince;
				}
				linkWeiboUser.setRpsCity(cache.getSinaAreaCode(cityCode));
			}
		}else{
			linkWeiboUser.setRpsCity("");
		}
		if(rpsProvince.equals("0") && "1000".equals(rpsCity)){
			linkWeiboUser.setCountryCode("");
			linkWeiboUser.setRpsProvince("");
			linkWeiboUser.setRpsCity("");
		}
		//		if(StringUtils.isBlank(rpsProvince) || !rpsProvince.equals("0")){
		//			String cityCode = countryCode + "-" + infoMap.get("city").toString();
		//			linkWeiboUser.setRpsCity("");
		//		}		
		/** 用户所在地 */
		linkWeiboUser.setRpsLocation(infoMap.get("location") == null ? "" : infoMap.get("location") + "");
		/** 用户描述|个人介绍 */
		linkWeiboUser.setRpsDescription(infoMap.get("description") == null ? "" : infoMap.get("description") + "");
		/** 用户博客地址|个人主页 */
		linkWeiboUser.setRpsUrl(infoMap.get("url") == null ? "" : infoMap.get("url") + "");
		/** 用户头像地址 */
		linkWeiboUser.setRpsProfileImageUrl(infoMap.get("profile_image_url") == null ? "" : infoMap
				.get("profile_image_url")
				+ "");
		/** 生日 */
		//linkWeiboUser.setBirthDay(infoMap.get());
		/** 用户的个性化域名 */
		linkWeiboUser.setRpsDomain(infoMap.get("domain") == null ? "" : infoMap.get("domain") + "");
		/** 性别，m：男、f：女、n：未知  */
		linkWeiboUser.setRpsGender(infoMap.get("gender") == null ? "" : infoMap.get("gender") + "");
		/** 粉丝数|听众数 */
		linkWeiboUser.setRpsFollowersCount(infoMap.get("followers_count") == null ? 0 : Integer.parseInt(infoMap
				.get("followers_count")
				+ ""));
		/** 关注数|收听的人数 */
		linkWeiboUser.setRpsFriendsCount(infoMap.get("friends_count") == null ? 0 : Integer.parseInt(infoMap
				.get("friends_count")
				+ ""));
		/** 用户的互粉数|互听好友数 */
		Integer biFollowersCount = infoMap.get("bi_followers_count") == null ? 0 : Integer.parseInt(infoMap
				.get("bi_followers_count")
				+ "");
		if(biFollowersCount < 0){
			biFollowersCount = 0;
		}
		linkWeiboUser.setRpsBiFollowersCount(biFollowersCount);
		/** 微博数 */
		linkWeiboUser.setRpsStatusesCount(infoMap.get("statuses_count") == null ? 0 : Integer.parseInt(infoMap
				.get("statuses_count")
				+ ""));
		/** 收藏数 */
		linkWeiboUser.setRpsFavouritesCount(infoMap.get("favourites_count") == null ? 0 : Integer.parseInt(infoMap
				.get("favourites_count")
				+ ""));
		/** 创建时间 */
		if(infoMap.get("created_at") != null){
			Date createTime = DateUtil.parse(infoMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
					Locale.US);
			linkWeiboUser.setRpsCreatedAt(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
		} else {
			linkWeiboUser.setRpsCreatedAt(0);
		}
		/** 是否允许所有人给我发私信 */
		if(infoMap.get("allow_all_act_msg") != null) {
			linkWeiboUser.setRpsAllowAllActMsg(infoMap.get("allow_all_act_msg").toString().equals("true") ? 1 : 0);
		}else {
			linkWeiboUser.setRpsAllowAllActMsg(0);
		}
		/** 是否允许带有地理信息 */
		linkWeiboUser.setRpsGeoEnabled(infoMap.get("geo_enabled").toString().equals("true") ? 1 : 0);
		/** 是否是微博认证用户，即带V用户 */
		linkWeiboUser.setRpsVerified(infoMap.get("verified").toString().equals("true") ? 1 : 0);
		/** 认证种类--1 普通0 黄V,  1-7 蓝V,10  微博女郎,  200 220 微博达人 | 30 企业认证 31 企业未认证 32 个人认证 33 个人未认证 */
		Integer verifiedType = infoMap.get("verified_type") == null ? -1 : Integer.valueOf(infoMap.get("verified_type")
				+ "");
		if(verifiedType == null){
			linkWeiboUser.setRpsVerifiedType(-1);
		}else{
			linkWeiboUser.setRpsVerifiedType(verifiedType);
		}
		/**'认证种类标志:1普通用户 2 个人认证 3 企业认证'*/
		Integer verifiedFlag = 1;
		if(verifiedType == 0){
			verifiedFlag = 2;
		}else if(verifiedType >= 1 && verifiedType <= 7){
			verifiedFlag = 3;
		}
		linkWeiboUser.setRpsVerifiedTypeFlag(verifiedFlag);
		//linkWeiboUser.setRpsVerifiedType(rpsVerifiedType);
		/** 是否允许所有人对我的微博进行评论 */
		linkWeiboUser.setRpsAllowAllComment(infoMap.get("allow_all_comment").toString().equals("true") ? 1 : 0);
		/** 用户大头像地址 */
		linkWeiboUser.setRpsAvatarLarge(infoMap.get("avatar_large").toString());
		/** 认证原因|认证信息 */
		linkWeiboUser.setRpsVerifiedReason(infoMap.get("verified_reason").toString());
		/** 评论数 */
		//linkWeiboUser.setRpsCommentNum(infoMap.get());
		/** @我数 */
		//linkWeiboUser.setRpsAtmeNum(infoMap.get());
		/** 覆盖度 */
		//linkWeiboUser.setRpsCoverageNum(infoMap.get());
		/** 行业ID */
		//linkWeiboUser.setIndustryCode(infoMap.get());
		/** 最后一条微博id */
		//取出status中的微博信息
		//linkWeiboUser.setLastweiboid(infoMap.get());
		/** 最新微博的发布时间 */
		//linkWeiboUser.setLastweibotime(infoMap.get());
		/** 用户标签 */
		//linkWeiboUser.setTags(infoMap.get());
		/** 用户质量:3高质量，2普通，1低质量，0未标记 */
		/*
		 * String fansQuality=""; Integer followersCount
		 * =Integer.parseInt(infoMap.get("followers_count").toString()); Integer
		 * friendsCount
		 * =Integer.parseInt(infoMap.get("friends_count").toString()); String
		 * proFileImageUrl =infoMap.get("profile_image_url").toString();
		 * if(StringUtils.equals("true", infoMap.get("verified").toString())){
		 * fansQuality = "3"; }else if(followersCount<100){////粉丝数是否<100
		 * fansQuality = "1"; }else if(followersCount>1000){////粉丝数是否>=1000
		 * fansQuality = "3"; }else if((friendsCount/followersCount)>20){////关注
		 * 除以 粉丝 比例 (20) fansQuality = "1"; }else
		 * if(proFileImageUrl.substring(0,
		 * proFileImageUrl.length()-7)=="/50/0/1"){//无头像的(/50/0/1) fansQuality =
		 * "1"; }else if((new
		 * Date().getTime()-createTime.getTime())>86400*30){//最后一条微博的时间1个月之前
		 * fansQuality = "1"; }else{ fansQuality = "2"; }
		 */
		linkWeiboUser.setFansQuality(0);
		/** 微博等级 */
		//linkWeiboUser.setWeiboLevel(infoMap.get());
		/** 同步时间 */
		linkWeiboUser.setSytime(Integer.parseInt(String.valueOf(new Date().getTime() / 1000)));
		/** 微博创建时间 */
		//linkWeiboUser.setWeiboCreateTime(infoMap.get());
		/** 微博同步时间 */
		//linkWeiboUser.setWeiboLastSytime(infoMap.get());
		/** 最新同步微博mid */
		//linkWeiboUser.setWeiboLastMid(infoMap.get());
	}
}
