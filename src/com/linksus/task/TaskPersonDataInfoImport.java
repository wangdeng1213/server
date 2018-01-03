package com.linksus.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboPersonCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.ExcelJxlsUtils;
import com.linksus.common.util.FileUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationGroup;
import com.linksus.entity.LinksusRelationPerson;
import com.linksus.entity.LinksusRelationPersonContact;
import com.linksus.entity.LinksusRelationPersonEdu;
import com.linksus.entity.LinksusRelationPersonGroup;
import com.linksus.entity.LinksusRelationPersonInfo;
import com.linksus.entity.LinksusRelationPersonJob;
import com.linksus.entity.LinksusRelationPersonTag;
import com.linksus.entity.LinksusRelationPersonTagdef;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusRelationWxuser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTypeAreaCode;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.PersonInfo;
import com.linksus.entity.ResponseAndRecordDTO;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationGroupService;
import com.linksus.service.LinksusRelationPersonContactService;
import com.linksus.service.LinksusRelationPersonEduService;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationPersonInfoService;
import com.linksus.service.LinksusRelationPersonJobService;
import com.linksus.service.LinksusRelationPersonOperService;
import com.linksus.service.LinksusRelationPersonService;
import com.linksus.service.LinksusRelationPersonTagService;
import com.linksus.service.LinksusRelationPersonTagdefService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusRelationWxuserService;
import com.linksus.service.LinksusTypeAreaCodeService;

/**
 * 导入用户数据 文件名做参数
 */
public class TaskPersonDataInfoImport extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskPersonDataInfoImport.class);

	protected Map map;

	public TaskPersonDataInfoImport() {

	}

	public TaskPersonDataInfoImport(Map map) {
		this.map = map;
	}

	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusRelationPersonService linksusRelationPersonService = (LinksusRelationPersonService) ContextUtil
			.getBean("linksusRelationPersonService");

	LinksusRelationPersonContactService linksusRelationPersonContactService = (LinksusRelationPersonContactService) ContextUtil
			.getBean("linksusRelationPersonContactService");

	LinksusRelationPersonEduService linksusRelationPersonEduService = (LinksusRelationPersonEduService) ContextUtil
			.getBean("linksusRelationPersonEduService");

	LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");

	LinksusRelationGroupService linksusRelationGroupService = (LinksusRelationGroupService) ContextUtil
			.getBean("linksusRelationGroupService");

	LinksusRelationPersonInfoService linksusRelationPersonInfoService = (LinksusRelationPersonInfoService) ContextUtil
			.getBean("linksusRelationPersonInfoService");

	LinksusRelationPersonJobService linksusRelationPersonJobService = (LinksusRelationPersonJobService) ContextUtil
			.getBean("linksusRelationPersonJobService");

	LinksusRelationPersonOperService linksusRelationPersonOperService = (LinksusRelationPersonOperService) ContextUtil
			.getBean("linksusRelationPersonOperService");

	LinksusRelationPersonTagdefService linksusRelationPersonTagdefService = (LinksusRelationPersonTagdefService) ContextUtil
			.getBean("linksusRelationPersonTagdefService");

	LinksusRelationPersonTagService linksusRelationPersonTagService = (LinksusRelationPersonTagService) ContextUtil
			.getBean("linksusRelationPersonTagService");

	LinksusTypeAreaCodeService linksusTypeAreaCodeService = (LinksusTypeAreaCodeService) ContextUtil
			.getBean("linksusTypeAreaCodeService");

	LinksusRelationWxuserService linksusRelationWxuserService = (LinksusRelationWxuserService) ContextUtil
			.getBean("linksusRelationWxuserService");

	public void cal(){

		try{
			Map paramap = new HashMap();
			paramap.put("filename", "d:\\导入模板.xlsx");
			// paramap.put("institution_id", institution_id);
			// paramap.put("inputstream", bufferedInputStream);
			// this.start(paramap);
			this.parseClientFile(new File("d:\\导入用户.xlsx"));
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 解析前台传递过来的xls文件 parm:filename 全路径参数
	public String start(Map map){
		String filename = "";
		BufferedInputStream bufinput = null;
		if(null != map.get("filename")){
			filename = map.get("filename").toString();
		}

		if(null != map.get("inputstream")){
			bufinput = (BufferedInputStream) map.get("inputstream");
		}

		try{
			File filefrom = new File(filename);
			File fileto = new File("d:\\tmp\\" + filefrom.getName());

			if(null != bufinput){
				FileUtil.copyFile(bufinput, fileto.getAbsoluteFile().toString());
			}

			FileUtil.copyFile(filefrom, fileto);// 第一步,拷贝文件
			// FileUtil.copyFile(filefrom, fileto);// 第一步,拷贝文件
			// this.parseClientFile(fileto);//第二部,解析xls文件
			// this.insertDataBase("");//第二部,将解析后的xls文件数据入库
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";
	}

	/**
	 * 查找地理信息
	 * 
	 * @param commonCache
	 * @return
	 */
	public String getAreaCode(Map map){
		String areacode = "";
		try{
			LinksusTypeAreaCode linksusTypeAreaCode = linksusTypeAreaCodeService.getLinksusTypeAreaCode(map);
			areacode = linksusTypeAreaCode.getAreaCode().toString();

		}catch (Exception e){
			LogUtil.saveException(logger, e);

		}
		return areacode;
	}

	// 解析前台传递过来的xls文件
	protected String parseClientFile(File rsData){
		ExcelJxlsUtils exj = new ExcelJxlsUtils();
		String dataXLS = "d:\\导入用户2.xlsx";
		String xmlConfig = "d:\\Person.xml";
		List personInfoList = new ArrayList();
		try{
			personInfoList = exj.readExcelFile(dataXLS, xmlConfig);

			Map sinatokenMap = this.getAccountTokenMap("1");
			Map tencenttokenMap = this.getAccountTokenMap("2");
			Map weixintokenMap = new HashMap();
			weixintokenMap.put("type", "3");
			List<LinksusRelationWeibouser> newlist = new ArrayList();
			List<LinksusRelationWeibouser> oldlist = new ArrayList();
			LinksusRelationWeibouser weibouser = null;
			Map pmap = new HashMap();
			Map contactmap = new HashMap();

			Map contactmap1 = new HashMap();
			Map contactmap2 = new HashMap();
			Map contactmap3 = new HashMap();
			Map contactmap4 = new HashMap();
			Map contactmap5 = new HashMap();
			Map contactmap10 = new HashMap();
			Map contactmap11 = new HashMap();
			Map contactmap12 = new HashMap();

			Map edumap = new HashMap();
			Map jobmap = new HashMap();
			Map groupmap = new HashMap();
			Map tagmap = new HashMap();

			Map areamap = new HashMap();
			String country_code = "";
			String state_code = "";
			String city_code = "";
			for(int i = 2; i < personInfoList.size(); i++){
				// PersonInfo person = new PersonInfo();
				newlist.clear();
				oldlist.clear();
				PersonInfo person = (PersonInfo) personInfoList.get(i);
				// System.err.println("person.getName()="+person.getName());
				// System.err.println("person.getSex()="+person.getSex());
				// 1.从接口判断用户是否存在,并将用户信息返回 2.判断微博账户表,是否存在 3.存在,

				// 新浪微博 以uid和name分别查询
				this.getWeiboUserDataFromSina(person.getSina_uids(), "1", sinatokenMap, newlist, oldlist);
				this.getWeiboUserDataFromSina(person.getSina_names(), "2", sinatokenMap, newlist, oldlist);
				// 腾讯微博 以openid和name分别查询
				this.getWeiboUserDataFromTentent(person.getTencent_uids(), "1", tencenttokenMap, newlist, oldlist);
				this.getWeiboUserDataFromTentent(person.getTencent_names(), "2", tencenttokenMap, newlist, oldlist);
				// 微信  以name等五个字段分别在库中查询
				this.getWeixinUserDataFromTentent(person.getWeixin_infos(), "2", weixintokenMap, newlist, oldlist);

				String sinaids = "";
				String tencentids = "";
				String weixinids = "";
				for(LinksusRelationWeibouser linkWeiboUser : newlist){
					if(linkWeiboUser.getUserType() == 1){
						sinaids += linkWeiboUser.getUserId() + "|";
					}
					if(linkWeiboUser.getUserType() == 2){
						tencentids += linkWeiboUser.getUserId() + "|";
					}
					if(linkWeiboUser.getUserType() == 3){
						weixinids += linkWeiboUser.getUserId() + "|";
					}

				}

				for(LinksusRelationWeibouser linkWeiboUser : oldlist){
					if(linkWeiboUser.getUserType() == 1){
						sinaids += linkWeiboUser.getUserId() + "|";
					}
					if(linkWeiboUser.getUserType() == 2){
						tencentids += linkWeiboUser.getUserId() + "|";
					}
					if(linkWeiboUser.getUserType() == 3){
						weixinids += linkWeiboUser.getUserId() + "|";
					}

				}

				if(sinaids.lastIndexOf("|") > 0){
					sinaids = sinaids.substring(0, sinaids.lastIndexOf("|"));
				}

				if(tencentids.lastIndexOf("|") > 0){
					tencentids = tencentids.substring(0, tencentids.lastIndexOf("|"));
				}

				if(weixinids.lastIndexOf("|") > 0){
					weixinids = weixinids.substring(0, weixinids.lastIndexOf("|"));
				}

				// 人员信息设置 START
				pmap.put("sinaids", sinaids);
				pmap.put("tencentids", tencentids);
				pmap.put("weixinids", weixinids);

				// pmap.put("person_id", person);
				pmap.put("institution_id", Long.parseLong("15"));
				pmap.put("person_name", person.getPerson_name());

				pmap.put("gender", person.getGender());
				pmap.put("birth_day", person.getBirth_day());
				pmap.put("verified_info", person.getVerified_info());
				pmap.put("location", "TEST");

				areamap.clear();
				areamap.put("area_name", person.getCountry_name());

				country_code = this.getAreaCode(areamap);

				areamap.put("area_name", person.getState_name());
				areamap.put("parent_code", country_code);

				state_code = this.getAreaCode(areamap);

				areamap.put("area_name", person.getCity_name());
				areamap.put("parent_code", state_code);

				city_code = this.getAreaCode(areamap);
				pmap.put("country_code", country_code);
				pmap.put("state_code", state_code);

				pmap.put("city_code", city_code);

				contactmap1 = this.getPersonSectionList(person.getContact_mobile());
				contactmap2 = this.getPersonSectionList(person.getContact_tel());
				contactmap3 = this.getPersonSectionList(person.getContact_tel_office());
				contactmap4 = this.getPersonSectionList(person.getContact_tel_home());
				contactmap5 = this.getPersonSectionList(person.getContact_fax());
				contactmap10 = this.getPersonSectionList(person.getEmail_office());
				contactmap11 = this.getPersonSectionList(person.getEmail_personal());
				contactmap12 = this.getPersonSectionList(person.getEmail_other());
				contactmap.put(1, contactmap1);
				contactmap.put(2, contactmap2);
				contactmap.put(3, contactmap3);
				contactmap.put(4, contactmap4);
				contactmap.put(5, contactmap5);
				contactmap.put(10, contactmap10);
				contactmap.put(11, contactmap11);
				contactmap.put(12, contactmap12);

				edumap = this.getPersonSectionList(person.getEdu_info());
				jobmap = this.getPersonSectionList(person.getJob_info());
				groupmap = this.getPersonSectionList(person.getGroup_info());
				tagmap = this.getPersonSectionList(person.getTag_info());

				pmap.put("contactmaps", contactmap);
				pmap.put("default_flag", 0);
				pmap.put("edumaps", edumap);
				pmap.put("jobmaps", jobmap);
				pmap.put("groupmaps", groupmap);
				pmap.put("tagmaps", tagmap);
				pmap.put("group_type", "1");
				// 人员信息设置 END

				if(oldlist.size() > 0 && newlist.size() == 0){// 全部为旧账户
					weibouser = oldlist.get(0);
					Long personId = weibouser.getPersonId();
					pmap.put("person_id", personId);
					this.insertPerson(pmap, "update");// 插入人员主表
					this.insertPersonInfo(pmap, "update");// 插入人员信息表
					//			 
					this.insertWeiboUser(oldlist, personId);// 插入微博账户表

					this.insertPersonContact(pmap, "update");// 插入人员联系方式信息表
					this.insertPersonEdu(pmap, "update"); // 插入人员教育信息表
					this.insertPersonGroupDef(pmap, "update");// 插入人员分组信息表
					// this.insertPersonGroup(pmap, "update");// 插入人员分组信息表
					this.insertPersonJob(pmap, "update"); // 插入人员工作信息表

					Map tagidmap = this.insertPersonTagDef(pmap, "update"); // 插入人员标签定义表
					pmap.put("tagidmaps", tagidmap);
					this.insertPersonTag(pmap, "update"); // 插入人员标签信息表
				}

				if(oldlist.size() == 0 && newlist.size() > 0){// 全部为新增账户

					Long personId = this.insertPerson(pmap, "insert");// 插入人员主表
					pmap.put("person_id", personId);
					this.insertPersonInfo(pmap, "insert");// 插入人员信息表
					//			 
					this.insertWeiboUser(newlist, personId);// 插入微博账户表

					this.insertPersonContact(pmap, "insert");// 插入人员联系方式信息表
					this.insertPersonEdu(pmap, "insert"); // 插入人员教育信息表
					this.insertPersonGroupDef(pmap, "insert");// 插入人员分组信息表
					// this.insertPersonGroup(pmap, "insert");// 插入人员分组信息表
					this.insertPersonJob(pmap, "insert"); // 插入人员工作信息表

					Map tagidmap = this.insertPersonTagDef(pmap, "insert"); // 插入人员标签定义表
					pmap.put("tagidmaps", tagidmap);
					this.insertPersonTag(pmap, "insert"); // 插入人员标签信息表
				}

				if(oldlist.size() > 0 && newlist.size() > 0){// 有新的,也有旧的账户
					weibouser = oldlist.get(0);
					Long personId = weibouser.getPersonId();
					pmap.put("person_id", personId);
					this.insertPerson(pmap, "update");// 插入人员主表
					this.insertPersonInfo(pmap, "update");// 插入人员信息表
					//			 
					this.insertWeiboUser(newlist, personId);// 插入微博账户表
					this.insertWeiboUser(oldlist, personId);// 插入微博账户表

					this.insertPersonContact(pmap, "update");// 插入人员联系方式信息表
					this.insertPersonEdu(pmap, "update"); // 插入人员教育信息表
					this.insertPersonGroupDef(pmap, "update");// 插入人员分组信息表
					// this.insertPersonGroup(pmap, "update");// 插入人员分组信息表
					this.insertPersonJob(pmap, "update"); // 插入人员工作信息表
					Map tagidmap = this.insertPersonTagDef(pmap, "update"); // 插入人员标签定义表
					pmap.put("tagidmaps", tagidmap);
					this.insertPersonTag(pmap, "update"); // 插入人员标签信息表
				}
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
	}

	// 插入人员主表
	protected Long insertPerson(Map map,String flag) throws Exception{

		Long personId = null;
		// 获取主键 和 头像
		LinksusRelationPerson linkPerson = new LinksusRelationPerson();
		LinksusRelationPerson exlinkPerson = null;
		String exsinaids = "";
		String extencentids = "";
		String exweixinids = "";
		String exheadimgurl = "";

		String sinaids = "";
		String tencentids = "";
		String weixinids = "";
		String headimgurl = "";

		if(flag.equals("insert")){
			personId = PrimaryKeyGen.getPrimaryKey("linksus_relation_person", "person_id");
		}
		if(flag.equals("update")){
			personId = (Long) map.get("person_id");

			exlinkPerson = linksusRelationPersonService.getLinksusRelationPersonById(personId);
			if(null != exlinkPerson){
				exsinaids = exlinkPerson.getSinaIds();
				extencentids = exlinkPerson.getTencentIds();
				exweixinids = exlinkPerson.getWeixinIds();
				exheadimgurl = exlinkPerson.getHeadimgurl();
			}
			linksusRelationPersonService.deleteLinksusRelationPersonById(personId);
		}

		map.put("person_id", personId);// 返回为入库人员信息表准备

		if(!exsinaids.trim().equals("")){

			//			  exsinaidsmap =getSectionList(exsinaids);
			//			  extencentidsmap = getSectionList(extencentids);
			//			  exweixinidsmap = getSectionList(exweixinids);
			//			  
			//			  if(exsinaidsmap.containsKey(key)){
			//				  
			//			  }
			sinaids = exsinaids + "|" + map.get("sinaids").toString();
		}else{
			sinaids = map.get("sinaids").toString();
		}
		if(!extencentids.trim().equals("")){
			tencentids = extencentids + "|" + map.get("tencentids").toString();
		}else{
			tencentids = map.get("tencentids").toString();
		}
		if(!exweixinids.trim().equals("")){
			weixinids = exweixinids + "|" + map.get("weixinids").toString();
		}else{
			weixinids = map.get("weixinids").toString();
		}
		if(!exheadimgurl.trim().equals("")){
			headimgurl = exheadimgurl;
		}else{
			headimgurl = "";
		}

		int add_time = DateUtil.getUnixDate(new Date());
		//add邢泽江
		int synctime = DateUtil.getUnixDate(new Date());

		linkPerson.setPersonId(personId);
		linkPerson.setHeadimgurl(headimgurl);
		linkPerson.setSinaIds(sinaids);
		linkPerson.setTencentIds(tencentids);

		linkPerson.setWeixinIds(weixinids);
		linkPerson.setAddTime(add_time);
		//add 邢泽江
		linkPerson.setSynctime(synctime);
		// if (flag.equals("insert")) {
		//linksusRelationPersonService.insertLinksusRelationPerson(linkPerson);
		QueueDataSave.addDataToQueue(linkPerson, Constants.OPER_TYPE_INSERT);
		// }
		// if (flag.equals("update")) {
		// linksusRelationPersonService
		// .updateLinksusRelationPerson(linkPerson);
		// }

		// linkWeiboUser.setPersonId(personId);
		return personId;
	}

	// 插入人员信息表
	protected String insertPersonInfo(Map map,String flag){

		// Long personId = PrimaryKeyGen.getPrimaryKey(
		// "linksus_relation_person_info", "person_id");

		LinksusRelationPersonInfo linkPersoninfo = new LinksusRelationPersonInfo();

		Long person_id = Long.parseLong(map.get("person_id").toString());
		Long institution_id = Long.parseLong(map.get("institution_id").toString());
		String person_name = map.get("person_name").toString();

		String gender = map.get("gender").toString();
		String birth_day = map.get("birth_day").toString();
		String verified_info = map.get("verified_info").toString();
		String location = map.get("location").toString();

		String country_code = map.get("country_code").toString();
		String state_code = map.get("state_code").toString();

		String city_code = map.get("city_code").toString();
		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));

		if(flag.equals("update")){
			linksusRelationPersonInfoService.deleteLinksusRelationPersonInfoById(person_id);
		}

		linkPersoninfo.setPersonId(person_id);
		linkPersoninfo.setInstitutionId(institution_id);
		linkPersoninfo.setPersonName(person_name);
		linkPersoninfo.setGender(gender);
		linkPersoninfo.setBirthDay(birth_day);
		linkPersoninfo.setVerifiedInfo(verified_info);
		linkPersoninfo.setLocation(location);
		linkPersoninfo.setCountryCode(country_code);
		linkPersoninfo.setStateCode(state_code);
		linkPersoninfo.setCityCode(city_code);
		linkPersoninfo.setLastUpdtTime(last_updt_time);

		// if (flag.equals("insert")) {
		linksusRelationPersonInfoService.insertLinksusRelationPersonInfo(linkPersoninfo);
		// }
		// if (flag.equals("update")) {
		// linksusRelationPersonInfoService
		// .updateLinksusRelationPersonInfo(linkPersoninfo);
		// }

		return "success";
	}

	// 插入人员联系方式表
	protected String insertPersonContact(Map map,String flag) throws Exception{

		Map<Integer, Map> contactmaps = (HashMap) map.get("contactmaps");

		Long pid = null;

		LinksusRelationPersonContact linkPersoninfoContact = new LinksusRelationPersonContact();

		Long person_id = (Long) map.get("person_id");
		Long institution_id = (Long) map.get("institution_id");
		int default_flag = (Integer) map.get("default_flag");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));

		linkPersoninfoContact.setPersonId(person_id);
		linkPersoninfoContact.setInstitutionId(institution_id);

		linkPersoninfoContact.setDefaultFlag(default_flag);
		linkPersoninfoContact.setLastUpdtTime(last_updt_time);

		if(flag.equals("update")){
			linksusRelationPersonContactService.deleteByPersonId(person_id);
		}

		for(int ctype : contactmaps.keySet()){
			Map<String, String> contactmap = (HashMap) contactmaps.get(ctype);
			for(String key : contactmap.keySet()){
				pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_person_contact", "pid");
				linkPersoninfoContact.setPid(pid);
				linkPersoninfoContact.setContactType(ctype);
				linkPersoninfoContact.setContact(key);

				linksusRelationPersonContactService.insertLinksusRelationPersonContact(linkPersoninfoContact);

			}
		}

		return "success";
	}

	// 插入人员教育信息表
	protected String insertPersonEdu(Map map,String flag) throws Exception{

		// Long personId = PrimaryKeyGen.getPrimaryKey(
		// "linksus_relation_person_info", "person_id");

		LinksusRelationPersonEdu entity = new LinksusRelationPersonEdu();

		Long person_id = (Long) map.get("person_id");
		Long institution_id = (Long) map.get("institution_id");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		Map<String, String> contactmap = (HashMap) map.get("edumaps");
		if(flag.equals("update")){
			linksusRelationPersonEduService.deleteByPersonId(person_id);
		}
		Long pid = null;
		Map edumap = null;
		for(String key : contactmap.keySet()){
			pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_person_edu", "pid");
			entity.setPid(pid);
			entity.setPersonId(person_id);
			entity.setInstitutionId(institution_id);
			edumap = (Map) getPersonEduSection(key);
			entity.setEduType(Integer.parseInt((String) edumap.get("edutype")));
			entity.setSchoolName((String) edumap.get("schoolname"));
			entity.setBeginYear(Integer.parseInt((String) edumap.get("beginyear")));
			entity.setEndYear(Integer.parseInt((String) edumap.get("endyear")));
			entity.setLastUpdtTime(last_updt_time);

			linksusRelationPersonEduService.insertLinksusRelationPersonEdu(entity);

		}

		return "success";
	}

	// 插入人员职业信息表
	protected String insertPersonJob(Map map,String flag) throws Exception{

		LinksusRelationPersonJob entity = new LinksusRelationPersonJob();

		Long person_id = (Long) map.get("person_id");
		Long institution_id = (Long) map.get("institution_id");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		Map<String, String> datamap = (HashMap) map.get("jobmaps");
		if(flag.equals("update")){
			linksusRelationPersonJobService.deleteByPersonId(person_id);
		}
		Long pid = null;
		Map emap = null;
		for(String key : datamap.keySet()){
			pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_person_job", "pid");
			entity.setPid(pid);
			entity.setPersonId(person_id);
			entity.setInstitutionId(institution_id);
			emap = (Map) getPersonJobSection(key);

			entity.setCompanyName((String) emap.get("company_name"));
			entity.setBeginYear(Integer.parseInt((String) emap.get("beginyear")));
			entity.setEndYear(Integer.parseInt((String) emap.get("endyear")));
			entity.setLastUpdtTime(last_updt_time);

			linksusRelationPersonJobService.insertLinksusRelationPersonJob(entity);

		}

		return "success";

	}

	// 插入人员分组表
	protected String insertPersonGroup(Map map,String flag){

		LinksusRelationPersonGroup entity = new LinksusRelationPersonGroup();

		Long person_id = (Long) map.get("person_id");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		Map<String, String> datamap = (HashMap) map.get("groupmaps");
		if(flag.equals("update")){
			linksusRelationPersonGroupService.deleteByPersonId(person_id);
		}

		for(String key : datamap.keySet()){

			entity.setGroupId(Long.parseLong(key));
			entity.setPersonId(person_id);
			entity.setGroupSource(1);// 1: 人工添加 2:自动筛选

			entity.setLastUpdtTime(last_updt_time);

			linksusRelationPersonGroupService.insertLinksusRelationPersonGroup(entity);

		}
		return "success";
	}

	// 插入人员标签表
	protected String insertPersonTag(Map map,String flag){

		LinksusRelationPersonTag entity = new LinksusRelationPersonTag();

		Long person_id = (Long) map.get("person_id");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));

		if(flag.equals("update")){
			linksusRelationPersonTagService.deleteByPersonId(person_id);
		}
		Map<String, String> datamap = (HashMap) map.get("tagidmaps");
		for(String key : datamap.keySet()){
			entity.setTagId(Long.parseLong(key));
			entity.setPersonId(person_id);
			entity.setCreateTime(last_updt_time);

			linksusRelationPersonTagService.insertLinksusRelationPersonTag(entity);

		}

		return "success";
	}

	// 插入人员标签定义表
	protected Map insertPersonTagDef(Map map,String flag) throws Exception{

		LinksusRelationPersonTagdef entity = new LinksusRelationPersonTagdef();
		LinksusRelationPersonTagdef entityat = null;

		Long person_id = (Long) map.get("person_id");
		Long institution_id = (Long) map.get("institution_id");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));

		Map tagidsmap = new HashMap();
		Map<String, String> datamap = (HashMap) map.get("tagmaps");
		Long pid = null;
		for(String key : datamap.keySet()){
			entityat = linksusRelationPersonTagdefService.getLinksusRelationPersonTagdefByTagName(key);
			if(entityat == null){
				pid = PrimaryKeyGen.getPrimaryKey("linksus_relation_person_tagdef", "pid");
				entity.setPid(pid);
				entity.setInstitutionId(institution_id);
				entity.setTagName(key);
				entity.setUseCount(0);
				entity.setInstPersonId(person_id);
				entity.setCreateTime(last_updt_time);
				linksusRelationPersonTagdefService.insertLinksusRelationPersonTagdef(entity);
			}else{
				pid = entityat.getPid();
			}
			tagidsmap.put(pid.toString(), pid.toString());

		}

		return tagidsmap;
	}

	// 插入人员组定义表
	protected String insertPersonGroupDef(Map map,String flag){

		Long person_id = (Long) map.get("person_id");
		Long institution_id = (Long) map.get("institution_id");

		int last_updt_time = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));

		if(flag.equals("update")){
			linksusRelationPersonGroupService.deleteByPersonId(person_id);
		}

		Map<String, String> datamap = (HashMap) map.get("groupmaps");

		map.put("last_updt_time", last_updt_time);
		Long pid = null;
		for(String key : datamap.keySet()){
			map.put("group_name", key);
			this.Deal_Person_Group(map);

		}
		map.put("group_name", "我添加的");
		map.put("group_type", "4");

		this.Deal_Person_Group(map);
		return "success";
	}

	// 微博用户分组
	public String Deal_Person_Group(Map map){

		LinksusRelationGroup linksusRelationGroup = new LinksusRelationGroup();
		LinksusRelationGroup linksusRelationGrouprs = new LinksusRelationGroup();
		LinksusRelationPersonGroup linksusRelationPersonGroup = new LinksusRelationPersonGroup();

		try{

			// 微博用户分组

			linksusRelationGroup.setInstitutionId(Long.parseLong((map.get("institution_id").toString())));
			linksusRelationGroup.setGroupName(map.get("group_name").toString());

			linksusRelationGroup.setGroupType(Integer.parseInt(map.get("group_type").toString()));

			linksusRelationGrouprs = linksusRelationGroupService.getLinksusRelationGroup(linksusRelationGroup);

			if(null == linksusRelationGrouprs){
				linksusRelationGroup.setGroupId(PrimaryKeyGen.getPrimaryKey("linksus_relation_group", "group_id"));
				linksusRelationGroup.setAccountId(Long.parseLong("0"));
				linksusRelationGroup.setAccountType(0);
				// linksusRelationGroup.setGroupType(1);

				linksusRelationGroup.setStatus(2);
				linksusRelationGroup.setAddDate("");
				linksusRelationGroup.setFansNum("");
				linksusRelationGroup.setFansQuality("");
				linksusRelationGroup.setRelationType("");
				linksusRelationGroup.setAccountIdQq("");
				linksusRelationGroup.setAccountIdSina("");
				linksusRelationGroup.setAreaCode(0l);
				linksusRelationGroup.setRelationSource("");
				linksusRelationGroup.setRpsGender("n");
				linksusRelationGroup.setLastUpdateTime((Integer) map.get("last_updt_time"));

				linksusRelationGroupService.insertLinksusRelationGroup(linksusRelationGroup);

				// 维护人员与组表
				linksusRelationPersonGroup.setGroupId(linksusRelationGroup.getGroupId());
				linksusRelationPersonGroup.setGroupSource(2);
				linksusRelationPersonGroup.setLastUpdtTime((Integer) map.get("last_updt_time"));
				linksusRelationPersonGroup.setPersonId(Long.parseLong(map.get("person_id").toString()));

				linksusRelationPersonGroupService.insertLinksusRelationPersonGroup(linksusRelationPersonGroup);
			}else{
				// 维护人员与组表
				linksusRelationPersonGroup.setGroupId(linksusRelationGrouprs.getGroupId());
				linksusRelationPersonGroup.setGroupSource(2);
				linksusRelationPersonGroup.setPersonId(Long.parseLong(map.get("person_id").toString()));
				linksusRelationPersonGroup.setLastUpdtTime((Integer) map.get("last_updt_time"));
				linksusRelationPersonGroupService.insertLinksusRelationPersonGroup(linksusRelationPersonGroup);

			}
		}catch (Exception e){
			logger.error("分组任务出错!");
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return "success";

	}

	// 插入微博帐户表
	protected String insertWeiboUser(List<LinksusRelationWeibouser> list,Long personId){
		LinksusRelationWxuser weixinuser = new LinksusRelationWxuser();
		for(LinksusRelationWeibouser linkWeiboUser : list){
			linkWeiboUser.setPersonId(personId);
			weixinuser.setPersonId(personId);
			weixinuser.setUserId(linkWeiboUser.getUserId());
			if(linkWeiboUser.getUserType() != 3){
				linksusRelationWeibouserService.updatePersonId(linkWeiboUser);
			}else if(linkWeiboUser.getUserType() == 3){
				linksusRelationWxuserService.updatePersonId(weixinuser);
			}

		}

		return "success";
	}

	// 将解析后的文件进行入库操作
	protected String insertDataBase(String rsData){
		List<ResponseAndRecordDTO> linksusWeibos = (List<ResponseAndRecordDTO>) JsonUtil.json2list(rsData,
				ResponseAndRecordDTO.class);
		for(ResponseAndRecordDTO dto : linksusWeibos){
			LinksusWeibo linksusWeibo = new LinksusWeibo();
			linksusWeibo.setCommentCount(dto.getComments());

		}
		return "success";
	}

	// 竖线分隔字符串的解析
	public Map getSectionList(String strs){
		// List lstObject = new ArrayList();
		Map map = new HashMap();
		String[] sts = strs.split("\\|");
		for(String s : sts){
			// lstObject.add(s);
			map.put(s.trim(), s.trim());
		}
		return map;
	}

	// 逗号分隔字符串的解析
	public Map getPersonSectionList(String strs){
		// List lstObject = new ArrayList();
		Map map = new HashMap();
		String[] sts = strs.replaceAll("，", ",").replaceAll("（", "(").replaceAll("）", ")").split(",");
		for(String s : sts){
			// lstObject.add(s);
			map.put(s, s);
		}
		return map;
	}

	// 逗号分隔字符串的解析
	public Map getWeixinSectionList(String strs){
		// List lstObject = new ArrayList();
		Map map = new HashMap();
		String[] sts = strs.replaceAll("，", ",").split(",");
		for(String s : sts){
			// lstObject.add(s);
			map.put(s, s);
		}
		return map;
	}

	// 取教育信息()中的数据
	public Map getPersonEduSection(String strs){
		// List lstObject = new ArrayList();
		Map map = new HashMap();
		// String[] edutype = strs.split("/");
		String[] sts = strs.split("\\(");
		String[] eduyear = sts[1].split("\\)")[0].split("-");

		// map.put("edutype", edutype[0]);
		map.put("edutype", "1");
		map.put("schoolname", sts[0].trim());
		map.put("beginyear", eduyear[0].trim());
		map.put("endyear", eduyear[1].trim());

		return map;
	}

	// 取职业信息()中的数据
	public Map getPersonJobSection(String strs){
		// List lstObject = new ArrayList();
		Map map = new HashMap();

		String[] sts = strs.split("\\(");
		String[] year = sts[1].split("\\)")[0].split("-");

		map.put("company_name", sts[0].trim());
		map.put("beginyear", year[0].trim());
		map.put("endyear", year[1].trim());

		return map;
	}

	// 获取微博用户的基本信息 从腾讯
	public String getWeiboUserDataFromTentent(String sts,String lable,Map tokenMap,List newlist,List oldlist)
			throws Exception{
		String strResult = "";
		// 通过配置文件从腾讯接口取最新的微博数据
		UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCUerInfo");
		String type = tokenMap.get("type") + "";
		String strjson = "";
		Map<String, String> uidsmap = (HashMap) this.getPersonSectionList(sts);
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		paraMap.put("openid", tokenMap.get("openid").toString());
		paraMap.put("access_token", tokenMap.get("token").toString());
		paraMap.put("type", type);
		boolean hasData = false;
		Map parmMap = new HashMap();
		String strToken = "";
		Map rsMap = new HashMap();
		WeiboPersonCommon wp = new WeiboPersonCommon();
		// List<HashMap> newlist = new ArrayList();
		// List<HashMap> oldlist = new ArrayList();

		for(String vstr : uidsmap.keySet()){

			if(lable.equals("1")){// 以UID来获取用户信息
				paraMap.put("fopenid", vstr);
			}else if(lable.equals("2")){// 以昵称来获取用户信息
				paraMap.put("name", vstr);
			}
			strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strjson);
			if(error != null){
				continue;
			}

			rsMap = wp.dealWeiboPersonUser(strjson, parmMap, true);
			LinksusRelationWeibouser linkWeiboUser = (LinksusRelationWeibouser) rsMap.get("linkWeiboUser");
			if(rsMap.get("exist").equals("1")){// 新增 uid
				newlist.add(linkWeiboUser);

			}else if(rsMap.get("exist").equals("2")){// 已存在 uid
				oldlist.add(linkWeiboUser);
			}
			// str1.append(singlestr + ",");
			// str2.append(fopenid + ",");
			// hasData = true;
		}
		// if (hasData) {
		// str1.deleteCharAt(str1.length() - 1);
		// str2.deleteCharAt(str2.length() - 1);
		// }
		// strResult = str1.append("]").toString() + "|" + str2.toString();
		return strResult.toString();
	}

	protected Map getAccountTokenMap(String accountType){
		Map tokenMap = null;
		try{
			Map accountMap = cache.getAccountTokenMap();
			LinksusAppaccount tokenObj = (LinksusAppaccount) accountMap.get(accountType);
			if(tokenObj != null){// 无相应授权
				tokenMap = new HashMap();
				tokenMap.put("token", tokenObj.getToken());
				tokenMap.put("openid", tokenObj.getAppid());
				tokenMap.put("type", accountType);
				tokenMap.put("appkey", tokenObj.getAppKey());
			}
		}catch (CacheException e){
			LogUtil.saveException(logger, e);
		}
		return tokenMap;
	}

	// 获取微博用户的基本信息 从新浪
	public String getWeiboUserDataFromSina(String sts,String lable,Map tokenMap,List newlist,List oldlist)
			throws Exception{
		String strjson = "";
		String strResult = "";
		UrlEntity strUrl = LoadConfig.getUrlEntity("fensiinfo");
		String type = tokenMap.get("type") + "";
		Map<String, String> uidsmap = (HashMap) this.getPersonSectionList(sts);
		boolean hasData = false;
		String tags = "";
		String strToken = "";
		Map paraMap = new HashMap();
		strToken = tokenMap.get("token").toString();
		paraMap.put("access_token", strToken);
		paraMap.put("type", type);

		Map rsMap = new HashMap();

		WeiboPersonCommon wp = new WeiboPersonCommon();
		// List<HashMap> newlist = new ArrayList();
		// List<HashMap> oldlist = new ArrayList();
		for(String vstr : uidsmap.keySet()){

			if(lable.equals("1")){// 以UID来获取用户信息
				paraMap.put("uid", vstr);
			}else if(lable.equals("2")){// 以昵称来获取用户信息
				paraMap.put("screen_name", vstr);
			}

			strjson = HttpUtil.getRequest(strUrl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strjson);
			if(error != null){
				continue;
			}
			rsMap = wp.dealWeiboPersonUser(strjson, paraMap, true);
			LinksusRelationWeibouser linkWeiboUser = (LinksusRelationWeibouser) rsMap.get("linkWeiboUser");
			if(rsMap.get("exist").equals("1")){// 新增 uid
				newlist.add(linkWeiboUser);

			}else if(rsMap.get("exist").equals("2")){// 已存在 uid
				oldlist.add(linkWeiboUser);
			}

		}

		return strResult.toString();
	}

	// 获取微信用户的基本信息 从腾讯
	public String getWeixinUserDataFromTentent(String sts,String lable,Map tokenMap,List newlist,List oldlist)
			throws Exception{
		String strResult = "";

		String type = tokenMap.get("type") + "";

		Map<String, String> uidsmap = (HashMap) this.getWeixinSectionList(sts);

		Map parmMap = new HashMap();
		parmMap.put("type", type);

		Map rsMap = new HashMap();
		WeiboPersonCommon wp = new WeiboPersonCommon();

		for(String vstr : uidsmap.keySet()){

			rsMap = wp.dealWeixinPersonUser(vstr, parmMap, true);
			LinksusRelationWeibouser linkWeiboUser = (LinksusRelationWeibouser) rsMap.get("linkWeiboUser");
			if(rsMap.get("exist").equals("1")){// 新增 uid
				newlist.add(linkWeiboUser);

			}else if(rsMap.get("exist").equals("2")){// 已存在 uid
				oldlist.add(linkWeiboUser);
			}

		}

		return strResult.toString();
	}

	// 获取腾讯用户标签
	public List getTenCentWeiBoUserTagsList(List<Map> list){
		List tagValueList = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				Map nameMap = list.get(i);
				String nameStr = nameMap.get("name").toString();
				tagValueList.add(nameStr);
			}
		}
		return tagValueList;
	}

	public static void main(String[] args) throws Exception{
		TaskPersonDataInfoImport tm = new TaskPersonDataInfoImport();
		try{
			System.err.println("XXX=XXX");
			// tm.cal();
			tm.parseClientFile(new File("d:\\导入用户.xlsx"));

		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
