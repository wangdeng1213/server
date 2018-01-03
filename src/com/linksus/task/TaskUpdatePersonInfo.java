package com.linksus.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusRelationWxuser;
import com.linksus.service.LinksusRelationPersonService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusRelationWxuserService;

/**
 * 人员基本信息默认值任务
 * 优先顺序：新浪   腾讯   微信
 * @author wangrui
 *
 */
public class TaskUpdatePersonInfo extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskUpdatePersonInfo.class);
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusRelationPersonService linksusRelationPersonService = (LinksusRelationPersonService) ContextUtil
			.getBean("linksusRelationPersonService");
	LinksusRelationWxuserService linksusRelationWxuserService = (LinksusRelationWxuserService) ContextUtil
			.getBean("linksusRelationWxuserService");
	CacheUtil cache = CacheUtil.getInstance();

	public static void main(String[] args){
		//	  TaskUpdatePersonInfo add = new TaskUpdatePersonInfo();
		//	  add.cal();	
		//	  QueueDataSave save = new QueueDataSave();
		//	  save.run();
		//	  System.exit(0);
	}

	@Override
	public void cal(){
		// 从服务器端采用分页取数据
		LinksusRelationPerson person = new LinksusRelationPerson();
		int startCount = (currentPage - 1) * pageSize;
		person.setStartCount(startCount);
		person.setPageSize(pageSize);
		List<LinksusRelationPerson> recordList = linksusRelationPersonService.getLinksusRelationPersons(person);
		if(recordList != null && recordList.size() > 0){
			for(LinksusRelationPerson record : recordList){
				updatePersonInfo(record);
			}
		}
		checkTaskListEnd(recordList);//判断任务是否轮询完成
	}

	public void updatePersonInfo(LinksusRelationPerson person){

		String headimgurl = "";//头像URL
		String userName = "";//人员姓名
		String gender = "";//性别
		String birthDay = "";//生日   微信用户无此信息
		String location = "";//所在地中文
		String countryCode = "";//所在国家ID
		String stateCode = "";//所在地区ID
		String cityCode = "";//所在城市ID

		//读取人员表人员新浪用户信息
		String sinaIds = person.getSinaIds();
		if(StringUtils.isNotBlank(sinaIds) && !sinaIds.equals("|")){
			int flag = 0;
			for(String sinaId : sinaIds.split("\\|")){
				//根据sinaId读取新浪用户头像等信息，信息完整跳出循环
				if(StringUtils.isNotBlank(sinaId) && !sinaId.equals("|")){
					long userid = 0;
					try{
						userid = Long.parseLong(sinaId);
					}catch (Exception e){
						userid = 0;
					}
					if(userid != 0){
						LinksusRelationWeibouser sinaUser = linksusRelationWeibouserService
								.getRelationWeibouserByUserId(Long.parseLong(sinaId));
						if(sinaUser != null){
							//数据置为初始值
							if(flag == 0){
								person.setHeadimgurl("");
								person.setPersonName("");
								person.setGender("");
								person.setBirthDay("");
								person.setLocation("");
								person.setCountryCode("");
								person.setStateCode("");
								person.setCityCode("");
							}
							headimgurl = sinaUser.getRpsProfileImageUrl();
							if(person.getHeadimgurl().equals("") && StringUtils.isNotBlank(headimgurl)){
								person.setHeadimgurl(headimgurl);
							}
							userName = sinaUser.getRpsScreenName();
							if(person.getPersonName().equals("") && StringUtils.isNotBlank(userName)){
								person.setPersonName(userName);
							}
							gender = sinaUser.getRpsGender();
							if(person.getGender().equals("") && StringUtils.isNotBlank(gender) && !gender.equals("n")){
								person.setGender(gender);
							}
							birthDay = sinaUser.getBirthDay();
							if(person.getBirthDay().equals("") && StringUtils.isNotBlank(birthDay)){
								person.setBirthDay(birthDay);
							}
							location = sinaUser.getRpsLocation();
							if(person.getLocation().equals("") && StringUtils.isNotBlank(location)){
								person.setLocation(location);
							}
							countryCode = sinaUser.getCountryCode();
							if(person.getCountryCode().equals("") && StringUtils.isNotBlank(countryCode)){
								person.setCountryCode(countryCode);
							}
							stateCode = sinaUser.getRpsProvince();
							if(person.getStateCode().equals("") && StringUtils.isNotBlank(stateCode)){
								person.setStateCode(stateCode);
							}
							cityCode = sinaUser.getRpsCity();
							if(person.getCityCode().equals("") && StringUtils.isNotBlank(cityCode)){
								person.setCityCode(cityCode);
							}
						}
					}
				}
				//信息完整跳出循环
				if(!person.getHeadimgurl().equals("") && !person.getCityCode().equals("")
						&& !person.getBirthDay().equals("") && !person.getStateCode().equals("")
						&& !person.getCountryCode().equals("") && !person.getGender().equals("")
						&& !person.getPersonName().equals("") && !person.getLocation().equals("")){
					break;
				}
				flag++;
			}
		}else{
			person.setHeadimgurl("");
			person.setPersonName("");
			person.setGender("");
			person.setBirthDay("");
			person.setLocation("");
			person.setCountryCode("");
			person.setStateCode("");
			person.setCityCode("");
		}

		//当人员用户信息不完全时，读取腾讯用户信息	
		if(person.getHeadimgurl().equals("") || person.getCityCode().equals("") || person.getBirthDay().equals("")
				|| person.getStateCode().equals("") || person.getCountryCode().equals("")
				|| person.getGender().equals("") || person.getPersonName().equals("")
				|| person.getLocation().equals("")){
			String tencentIds = person.getTencentIds();
			if(StringUtils.isNotBlank(tencentIds) && !tencentIds.equals("|")){
				for(String tencentId : tencentIds.split("\\|")){
					if(StringUtils.isNotBlank(tencentId) && !tencentId.equals("|")){
						long userid = 0;
						try{
							userid = Long.parseLong(tencentId);
						}catch (Exception e){
							userid = 0;
						}
						if(userid != 0){
							LinksusRelationWeibouser tencentUser = linksusRelationWeibouserService
									.getRelationWeibouserByUserId(Long.parseLong(tencentId));
							if(tencentUser != null){
								headimgurl = tencentUser.getRpsProfileImageUrl();
								if(person.getHeadimgurl().equals("") && StringUtils.isNotBlank(headimgurl)){
									person.setHeadimgurl(headimgurl);
								}
								userName = tencentUser.getRpsScreenName();
								if(person.getPersonName().equals("") && StringUtils.isNotBlank(userName)){
									person.setPersonName(userName);
								}
								gender = tencentUser.getRpsGender();
								if(person.getGender().equals("") && StringUtils.isNotBlank(gender)
										&& !gender.equals("n")){
									person.setGender(gender);
								}
								birthDay = tencentUser.getBirthDay();
								if(person.getBirthDay().equals("") && StringUtils.isNotBlank(birthDay)){
									person.setBirthDay(birthDay);
								}
								location = tencentUser.getRpsLocation();
								if(person.getLocation().equals("") && StringUtils.isNotBlank(location)){
									person.setLocation(location);
								}
								countryCode = tencentUser.getCountryCode();
								if(person.getCountryCode().equals("") && StringUtils.isNotBlank(countryCode)){
									person.setCountryCode(countryCode);
								}
								stateCode = tencentUser.getRpsProvince();
								if(person.getStateCode().equals("") && StringUtils.isNotBlank(stateCode)){
									person.setStateCode(stateCode);
								}
								cityCode = tencentUser.getRpsCity();
								if(person.getCityCode().equals("") && StringUtils.isNotBlank(cityCode)){
									person.setCityCode(cityCode);
								}
							}
						}
					}
					//信息完整跳出循环
					if(!person.getHeadimgurl().equals("") && !person.getCityCode().equals("")
							&& !person.getBirthDay().equals("") && !person.getStateCode().equals("")
							&& !person.getCountryCode().equals("") && !person.getGender().equals("")
							&& !person.getPersonName().equals("") && !person.getLocation().equals("")){
						break;
					}
				}
			}
		}

		//当人员用户信息不完全时，读取微信用户信息	,微信用户无生日信息
		if(person.getHeadimgurl().equals("") || person.getCityCode().equals("") || person.getBirthDay().equals("")
				|| person.getStateCode().equals("") || person.getCountryCode().equals("")
				|| person.getGender().equals("") || person.getPersonName().equals("")){
			String weixinIds = person.getWeixinIds();
			if(StringUtils.isNotBlank(weixinIds) && !weixinIds.equals("|")){
				for(String weixinId : weixinIds.split("\\|")){
					if(StringUtils.isNotBlank(weixinId) && !weixinId.equals("|")){
						long userid = 0;
						try{
							userid = Long.parseLong(weixinId);
						}catch (Exception e){
							userid = 0;
						}
						if(userid != 0){
							LinksusRelationWxuser wxUser = linksusRelationWxuserService
									.getLinksusRelationWxuserInfo(Long.parseLong(weixinId));
							if(wxUser != null){

								//处理基本信息
								headimgurl = wxUser.getHeadimgurl();
								if(person.getHeadimgurl().equals("") && StringUtils.isNotBlank(headimgurl)){
									person.setHeadimgurl(headimgurl);
								}
								userName = wxUser.getNickname();
								if(person.getPersonName().equals("") && StringUtils.isNotBlank(userName)){
									person.setPersonName(userName);
								}
								gender = wxUser.getSex();//n 为未知
								if(person.getGender().equals("") && StringUtils.isNotBlank(gender)
										&& !gender.equals("n")){
									person.setGender(gender);
								}
								if(person.getLocation().equals("")){
									if(StringUtils.isNotBlank(wxUser.getCountry())){
										location = wxUser.getCountry();
									}
									if(StringUtils.isNotBlank(location) && StringUtils.isNotBlank(wxUser.getProvince())){
										location = location + " " + wxUser.getProvince();
									}
									if(StringUtils.isNotBlank(location) && StringUtils.isNotBlank(wxUser.getCity())){
										location = location + " " + wxUser.getCity();
									}
									person.setLocation(location);
								}
								countryCode = wxUser.getCountryCode();
								if(person.getCountryCode().equals("") && StringUtils.isNotBlank(countryCode)){
									person.setCountryCode(countryCode);
								}
								stateCode = wxUser.getProvinceCode();
								if(person.getStateCode().equals("") && StringUtils.isNotBlank(stateCode)){
									person.setStateCode(stateCode);
								}
								cityCode = wxUser.getCityCode();
								if(person.getCityCode().equals("") && StringUtils.isNotBlank(cityCode)){
									person.setCityCode(cityCode);
								}
								if(!person.getHeadimgurl().equals("") && !person.getCityCode().equals("")
										&& !person.getStateCode().equals("") && !person.getCountryCode().equals("")
										&& !person.getGender().equals("") && !person.getPersonName().equals("")){
									break;
								}
							}
						}
					}
				}
			}
		}
		//设置人员基本信息
		person.setSynctime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
		//linksusRelationPersonService.updateLinksusRelationPersonInfo(person);		
		QueueDataSave.addDataToQueue(person, Constants.OPER_TYPE_UPDATE_INFO);
	}

}
