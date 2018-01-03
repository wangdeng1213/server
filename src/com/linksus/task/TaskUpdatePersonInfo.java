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
 * ��Ա������ϢĬ��ֵ����
 * ����˳������   ��Ѷ   ΢��
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
		// �ӷ������˲��÷�ҳȡ����
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
		checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
	}

	public void updatePersonInfo(LinksusRelationPerson person){

		String headimgurl = "";//ͷ��URL
		String userName = "";//��Ա����
		String gender = "";//�Ա�
		String birthDay = "";//����   ΢���û��޴���Ϣ
		String location = "";//���ڵ�����
		String countryCode = "";//���ڹ���ID
		String stateCode = "";//���ڵ���ID
		String cityCode = "";//���ڳ���ID

		//��ȡ��Ա����Ա�����û���Ϣ
		String sinaIds = person.getSinaIds();
		if(StringUtils.isNotBlank(sinaIds) && !sinaIds.equals("|")){
			int flag = 0;
			for(String sinaId : sinaIds.split("\\|")){
				//����sinaId��ȡ�����û�ͷ�����Ϣ����Ϣ��������ѭ��
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
							//������Ϊ��ʼֵ
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
				//��Ϣ��������ѭ��
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

		//����Ա�û���Ϣ����ȫʱ����ȡ��Ѷ�û���Ϣ	
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
					//��Ϣ��������ѭ��
					if(!person.getHeadimgurl().equals("") && !person.getCityCode().equals("")
							&& !person.getBirthDay().equals("") && !person.getStateCode().equals("")
							&& !person.getCountryCode().equals("") && !person.getGender().equals("")
							&& !person.getPersonName().equals("") && !person.getLocation().equals("")){
						break;
					}
				}
			}
		}

		//����Ա�û���Ϣ����ȫʱ����ȡ΢���û���Ϣ	,΢���û���������Ϣ
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

								//���������Ϣ
								headimgurl = wxUser.getHeadimgurl();
								if(person.getHeadimgurl().equals("") && StringUtils.isNotBlank(headimgurl)){
									person.setHeadimgurl(headimgurl);
								}
								userName = wxUser.getNickname();
								if(person.getPersonName().equals("") && StringUtils.isNotBlank(userName)){
									person.setPersonName(userName);
								}
								gender = wxUser.getSex();//n Ϊδ֪
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
		//������Ա������Ϣ
		person.setSynctime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
		//linksusRelationPersonService.updateLinksusRelationPersonInfo(person);		
		QueueDataSave.addDataToQueue(person, Constants.OPER_TYPE_UPDATE_INFO);
	}

}
