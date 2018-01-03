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

	//������Ѷ���ݵ�΢���û�����
	public static void copyTencentUserMapToBean(Map infoMap,LinksusRelationWeibouser linkWeiboUser){
		CacheUtil cache = CacheUtil.getInstance();
		/** ������Ѷ΢���û���ʶ ����API����  */
		linkWeiboUser.setRpsId((String) infoMap.get("openid") == null ? "" : (String) infoMap.get("openid"));
		/** �û����� 1:���� 2 ��Ѷ */
		linkWeiboUser.setUserType(2);
		/** ��ԱID:������Ա���� */
		//linkWeiboUser.setPersonId(infoMap.get());
		/** �û��ǳ� */
		linkWeiboUser.setRpsScreenName((String) infoMap.get("nick") == null ? "" : (String) infoMap.get("nick"));
		/** �Ѻ���ʾ����|�û��˻��� */
		linkWeiboUser.setRpsName((String) infoMap.get("name") == null ? "" : (String) infoMap.get("name"));
		/** �û����ڹ���ID */
		String countryCode = (String) infoMap.get("country_code");
		if(StringUtils.isNotBlank(countryCode) && !StringUtils.equals(countryCode, "0")){
			linkWeiboUser.setCountryCode(cache.getTencentAreaCode(countryCode));
		}else{
			linkWeiboUser.setCountryCode("");
			linkWeiboUser.setRpsProvince("");
			linkWeiboUser.setRpsCity("");
		}
		/** �û����ڵ���ID */
		String rpsProvince = (String) infoMap.get("province_code");
		if(StringUtils.isNotBlank(rpsProvince) && !StringUtils.equals(rpsProvince, "0")){
			linkWeiboUser.setRpsProvince(cache.getTencentAreaCode(countryCode + "-" + rpsProvince));
		}else{
			rpsProvince = "";
			linkWeiboUser.setRpsProvince("");
		}

		/** �û����ڳ���ID */
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
		/** �û����ڵ� */
		linkWeiboUser.setRpsLocation((String) infoMap.get("location") == null ? "" : (String) infoMap.get("location"));
		/** �û�����|���˽��� */
		if(infoMap.containsKey("introduction")){
			linkWeiboUser.setRpsDescription((String) infoMap.get("introduction") == null ? "" : (String) infoMap
					.get("introduction"));
		}
		/** �û����͵�ַ|������ҳ */
		linkWeiboUser.setRpsUrl((String) infoMap.get("homepage") == null ? "" : (String) infoMap.get("homepage"));
		/** �û�ͷ���ַ */
		linkWeiboUser
				.setRpsProfileImageUrl(StringUtils.isBlank((String) infoMap.get("head")) ? "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png"
						: (String) infoMap.get("head") + "/120");
		/** ���� */
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
		/** �û��ĸ��Ի����� */
		//������
		//linkWeiboUser.setRpsDomain(infoMap.get());
		/** �Ա�m���С�f��Ů��n��δ֪  */
		String rpsGender = "n";
		if(infoMap.get("sex") != null){
			if(infoMap.get("sex").toString().equals("1")){
				rpsGender = "m";
			}else if(infoMap.get("sex").toString().equals("2")){
				rpsGender = "f";
			}
		}
		linkWeiboUser.setRpsGender(rpsGender);
		/** ��˿��|������ */
		linkWeiboUser.setRpsFollowersCount(infoMap.get("fansnum") == null ? 0 : Integer.valueOf(infoMap.get("fansnum")
				+ ""));
		/** ��ע��|���������� */
		linkWeiboUser.setRpsFriendsCount(infoMap.get("idolnum") == null ? 0 : Integer.valueOf(infoMap.get("idolnum")
				+ ""));
		/** �û��Ļ�����|���������� */
		linkWeiboUser.setRpsBiFollowersCount(infoMap.get("mutual_fans_num") == null ? 0 : Integer.valueOf(infoMap
				.get("mutual_fans_num")
				+ ""));
		/** ΢���� */
		linkWeiboUser.setRpsStatusesCount(infoMap.get("tweetnum") == null ? 0 : Integer.valueOf(infoMap.get("tweetnum")
				+ ""));
		/** �ղ��� */
		linkWeiboUser.setRpsFavouritesCount(infoMap.get("favnum") == null ? 0 : Integer.valueOf(infoMap.get("favnum")
				+ ""));
		/** ����ʱ�� */
		linkWeiboUser
				.setRpsCreatedAt(infoMap.get("regtime") == null ? 0 : Integer.valueOf(infoMap.get("regtime") + ""));
		/** �Ƿ����������˸��ҷ�˽�� */
		linkWeiboUser.setRpsAllowAllActMsg(infoMap.get("send_private_flag") == null ? 0 : Integer.valueOf(infoMap
				.get("send_private_flag")
				+ ""));
		/** �Ƿ�������е�����Ϣ */
		//������
		//linkWeiboUser.setRpsGeoEnabled(infoMap.get());
		//�Ƿ���ҵ����
		Object isSendObj = infoMap.get("isent");
		//�Ƿ���֤�û�
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

		/** �Ƿ���΢����֤�û�������V�û� */
		linkWeiboUser.setRpsVerified(infoMap.get("isvip") == null ? 0 : new Integer(infoMap.get("isvip") + ""));
		/** ��֤����-0 ��V,  1-7 ��V,10  ΢��Ů��,  200 220 ΢������ | 30 ��ҵʵ����֤ 31 ��ҵδʵ����֤ 32 ����ʵ����֤ 33 ����δʵ����֤ */
		linkWeiboUser.setRpsVerifiedType(verifiedType);
		Integer verifiedTypeFlag = 1;
		if(verifiedType == 30){
			verifiedTypeFlag = 3;
		}else if(verifiedType == 32){
			verifiedTypeFlag = 2;
		}
		linkWeiboUser.setRpsVerifiedTypeFlag(verifiedTypeFlag);
		/** �Ƿ����������˶��ҵ�΢���������� */
		//linkWeiboUser.setRpsAllowAllComment(infoMap.get());
		/** �û���ͷ���ַ */
		//linkWeiboUser.setRpsAvatarLarge(infoMap.get());
		/** ��֤ԭ��|��֤��Ϣ */
		linkWeiboUser.setRpsVerifiedReason((String) infoMap.get("verifyinfo") == null ? "" : infoMap.get("verifyinfo")
				.toString());
		/** ������ */
		//linkWeiboUser.setRpsCommentNum(infoMap.get());
		/** @���� */
		//linkWeiboUser.setRpsAtmeNum(infoMap.get());
		/** ���Ƕ� */
		//linkWeiboUser.setRpsCoverageNum(infoMap.get());
		/** ��ҵID */
		linkWeiboUser.setIndustryCode(infoMap.get("industry_code") == null ? "" : infoMap.get("industry_code")
				.toString());
		/** ���һ��΢��id */
		//Map tweetInfomap=(Map)infoMap.get("tweetinfo");
		//δ������ 
		//--String lastid=tweetInfomap.get("id").toString();
		//linkWeiboUser.setLastweiboid(lastid);
		/** ����΢���ķ���ʱ�� */
		//linkWeiboUser.setLastweibotime(infoMap.get());
		/** �û���ǩ */
		//linkWeiboUser.setTags(tagValue);
		/** �û�����:3��������2��ͨ��1��������0δ��� */
		/*
		 * String fansQuality=""; Integer followersCount
		 * =Integer.parseInt((String
		 * )infoMap.get("fansnum")==null?"0":infoMap.get("fansnum").toString());
		 * Integer friendsCount
		 * =Integer.parseInt((String)infoMap.get("idolnum")==
		 * null?"0":infoMap.get("idolnum").toString()); String proFileImageUrl
		 * =infoMap.get("head").toString();
		 * if(Integer.valueOf(infoMap.get("isvip").toString())==1){ fansQuality
		 * = "3"; }else if(followersCount<100){////��˿���Ƿ�<100 fansQuality = "1";
		 * }else if(followersCount>1000){////��˿���Ƿ�>=1000 fansQuality = "3";
		 * }else if((friendsCount/followersCount)>20){////��ע ���� ��˿ ���� (20)
		 * fansQuality = "1"; }else
		 * if(proFileImageUrl.substring(0,proFileImageUrl
		 * .length()-7)=="/50/0/1"){//��ͷ���(/50/0/1) fansQuality = "1"; }else
		 * if((new Date().getTime()-new
		 * Long(infoMap.get("regtime").toString())>86400*30)){//���һ��΢����ʱ��1����֮ǰ
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
		/** ΢���ȼ� */
		linkWeiboUser.setWeiboLevel(infoMap.get("level") == null ? 0 : new Integer(infoMap.get("level").toString()));
		/** ͬ��ʱ�� */
		linkWeiboUser.setSytime(Integer.parseInt(String.valueOf(new Date().getTime() / 1000)));
		/** ΢������ʱ�� */
		//linkWeiboUser.setWeiboCreateTime(infoMap.get());
		/** ΢��ͬ��ʱ�� */
		//linkWeiboUser.setWeiboLastSytime(infoMap.get());
		/** ����ͬ��΢��mid */
		//linkWeiboUser.setWeiboLastMid(infoMap.get());
	}

	//�����������ݵ�΢���û�����
	public static void copySinaUserMapToBean(Map infoMap,LinksusRelationWeibouser linkWeiboUser){
		CacheUtil cache = CacheUtil.getInstance();
		/** ������Ѷ΢���û���ʶ ����API����  */
		linkWeiboUser.setRpsId(infoMap.get("id") == null ? "" : infoMap.get("id") + "");
		/** ��ԱID:������Ա���� */
		//���뵽��Ա����������
		linkWeiboUser.setUserType(1);
		//linkWeiboUser.setPersonId(infoMap.get());
		/** �û��ǳ� */
		linkWeiboUser.setRpsScreenName(infoMap.get("screen_name") == null ? "" : infoMap.get("screen_name") + "");
		/** �Ѻ���ʾ����|�û��˻��� */
		linkWeiboUser.setRpsName(infoMap.get("name") == null ? "" : infoMap.get("name") + "");

		/** �û����ڹ���ID */
		String countryCode = "1";
		linkWeiboUser.setCountryCode(countryCode);
		/** �û����ڵ���ID */
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
		/** �û����ڳ���ID */
		if(StringUtils.isNotBlank(foreignCode)){//������д���
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
		/** �û����ڵ� */
		linkWeiboUser.setRpsLocation(infoMap.get("location") == null ? "" : infoMap.get("location") + "");
		/** �û�����|���˽��� */
		linkWeiboUser.setRpsDescription(infoMap.get("description") == null ? "" : infoMap.get("description") + "");
		/** �û����͵�ַ|������ҳ */
		linkWeiboUser.setRpsUrl(infoMap.get("url") == null ? "" : infoMap.get("url") + "");
		/** �û�ͷ���ַ */
		linkWeiboUser.setRpsProfileImageUrl(infoMap.get("profile_image_url") == null ? "" : infoMap
				.get("profile_image_url")
				+ "");
		/** ���� */
		//linkWeiboUser.setBirthDay(infoMap.get());
		/** �û��ĸ��Ի����� */
		linkWeiboUser.setRpsDomain(infoMap.get("domain") == null ? "" : infoMap.get("domain") + "");
		/** �Ա�m���С�f��Ů��n��δ֪  */
		linkWeiboUser.setRpsGender(infoMap.get("gender") == null ? "" : infoMap.get("gender") + "");
		/** ��˿��|������ */
		linkWeiboUser.setRpsFollowersCount(infoMap.get("followers_count") == null ? 0 : Integer.parseInt(infoMap
				.get("followers_count")
				+ ""));
		/** ��ע��|���������� */
		linkWeiboUser.setRpsFriendsCount(infoMap.get("friends_count") == null ? 0 : Integer.parseInt(infoMap
				.get("friends_count")
				+ ""));
		/** �û��Ļ�����|���������� */
		Integer biFollowersCount = infoMap.get("bi_followers_count") == null ? 0 : Integer.parseInt(infoMap
				.get("bi_followers_count")
				+ "");
		if(biFollowersCount < 0){
			biFollowersCount = 0;
		}
		linkWeiboUser.setRpsBiFollowersCount(biFollowersCount);
		/** ΢���� */
		linkWeiboUser.setRpsStatusesCount(infoMap.get("statuses_count") == null ? 0 : Integer.parseInt(infoMap
				.get("statuses_count")
				+ ""));
		/** �ղ��� */
		linkWeiboUser.setRpsFavouritesCount(infoMap.get("favourites_count") == null ? 0 : Integer.parseInt(infoMap
				.get("favourites_count")
				+ ""));
		/** ����ʱ�� */
		if(infoMap.get("created_at") != null){
			Date createTime = DateUtil.parse(infoMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
					Locale.US);
			linkWeiboUser.setRpsCreatedAt(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
		} else {
			linkWeiboUser.setRpsCreatedAt(0);
		}
		/** �Ƿ����������˸��ҷ�˽�� */
		if(infoMap.get("allow_all_act_msg") != null) {
			linkWeiboUser.setRpsAllowAllActMsg(infoMap.get("allow_all_act_msg").toString().equals("true") ? 1 : 0);
		}else {
			linkWeiboUser.setRpsAllowAllActMsg(0);
		}
		/** �Ƿ�������е�����Ϣ */
		linkWeiboUser.setRpsGeoEnabled(infoMap.get("geo_enabled").toString().equals("true") ? 1 : 0);
		/** �Ƿ���΢����֤�û�������V�û� */
		linkWeiboUser.setRpsVerified(infoMap.get("verified").toString().equals("true") ? 1 : 0);
		/** ��֤����--1 ��ͨ0 ��V,  1-7 ��V,10  ΢��Ů��,  200 220 ΢������ | 30 ��ҵ��֤ 31 ��ҵδ��֤ 32 ������֤ 33 ����δ��֤ */
		Integer verifiedType = infoMap.get("verified_type") == null ? -1 : Integer.valueOf(infoMap.get("verified_type")
				+ "");
		if(verifiedType == null){
			linkWeiboUser.setRpsVerifiedType(-1);
		}else{
			linkWeiboUser.setRpsVerifiedType(verifiedType);
		}
		/**'��֤�����־:1��ͨ�û� 2 ������֤ 3 ��ҵ��֤'*/
		Integer verifiedFlag = 1;
		if(verifiedType == 0){
			verifiedFlag = 2;
		}else if(verifiedType >= 1 && verifiedType <= 7){
			verifiedFlag = 3;
		}
		linkWeiboUser.setRpsVerifiedTypeFlag(verifiedFlag);
		//linkWeiboUser.setRpsVerifiedType(rpsVerifiedType);
		/** �Ƿ����������˶��ҵ�΢���������� */
		linkWeiboUser.setRpsAllowAllComment(infoMap.get("allow_all_comment").toString().equals("true") ? 1 : 0);
		/** �û���ͷ���ַ */
		linkWeiboUser.setRpsAvatarLarge(infoMap.get("avatar_large").toString());
		/** ��֤ԭ��|��֤��Ϣ */
		linkWeiboUser.setRpsVerifiedReason(infoMap.get("verified_reason").toString());
		/** ������ */
		//linkWeiboUser.setRpsCommentNum(infoMap.get());
		/** @���� */
		//linkWeiboUser.setRpsAtmeNum(infoMap.get());
		/** ���Ƕ� */
		//linkWeiboUser.setRpsCoverageNum(infoMap.get());
		/** ��ҵID */
		//linkWeiboUser.setIndustryCode(infoMap.get());
		/** ���һ��΢��id */
		//ȡ��status�е�΢����Ϣ
		//linkWeiboUser.setLastweiboid(infoMap.get());
		/** ����΢���ķ���ʱ�� */
		//linkWeiboUser.setLastweibotime(infoMap.get());
		/** �û���ǩ */
		//linkWeiboUser.setTags(infoMap.get());
		/** �û�����:3��������2��ͨ��1��������0δ��� */
		/*
		 * String fansQuality=""; Integer followersCount
		 * =Integer.parseInt(infoMap.get("followers_count").toString()); Integer
		 * friendsCount
		 * =Integer.parseInt(infoMap.get("friends_count").toString()); String
		 * proFileImageUrl =infoMap.get("profile_image_url").toString();
		 * if(StringUtils.equals("true", infoMap.get("verified").toString())){
		 * fansQuality = "3"; }else if(followersCount<100){////��˿���Ƿ�<100
		 * fansQuality = "1"; }else if(followersCount>1000){////��˿���Ƿ�>=1000
		 * fansQuality = "3"; }else if((friendsCount/followersCount)>20){////��ע
		 * ���� ��˿ ���� (20) fansQuality = "1"; }else
		 * if(proFileImageUrl.substring(0,
		 * proFileImageUrl.length()-7)=="/50/0/1"){//��ͷ���(/50/0/1) fansQuality =
		 * "1"; }else if((new
		 * Date().getTime()-createTime.getTime())>86400*30){//���һ��΢����ʱ��1����֮ǰ
		 * fansQuality = "1"; }else{ fansQuality = "2"; }
		 */
		linkWeiboUser.setFansQuality(0);
		/** ΢���ȼ� */
		//linkWeiboUser.setWeiboLevel(infoMap.get());
		/** ͬ��ʱ�� */
		linkWeiboUser.setSytime(Integer.parseInt(String.valueOf(new Date().getTime() / 1000)));
		/** ΢������ʱ�� */
		//linkWeiboUser.setWeiboCreateTime(infoMap.get());
		/** ΢��ͬ��ʱ�� */
		//linkWeiboUser.setWeiboLastSytime(infoMap.get());
		/** ����ͬ��΢��mid */
		//linkWeiboUser.setWeiboLastMid(infoMap.get());
	}
}
