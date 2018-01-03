package com.linksus.entity;

public class PersonInfo{

	// table linksus_relation_person_info
	// 姓名
	private String person_name;
	// 性别
	private String gender;

	// 生日
	private String birth_day;
	// 所在地中文
	private String location;
	// 所在国家代码
	private String country_code;

	// 所在地区/省份代码
	private String state_code;
	// 所在城市代码
	private String city_code;

	// 所在国家
	private String country_name;

	// 所在地区/省份
	private String state_name;
	// 所在城市
	private String city_name;

	// 认证信息
	private String verified_info;

	// table linksus_relation_person_contact contact_type tinyint(3) COMMENT
	// '联系方式类型:1 移动电话 2 固定电话 2 办公电话 4 家庭电话 5 传真号码 6 其他号码 10 工作邮箱 11 私人邮箱 12 其他邮箱

	// 移动电话
	private String contact_mobile;
	// 固定电话
	private String contact_tel;

	// 办公电话
	private String contact_tel_office;
	// 家庭电话
	private String contact_tel_home;

	// 传真号码
	private String contact_fax;

	// 工作邮箱
	private String email_office;
	// 私人邮箱
	private String email_personal;

	// 其他邮箱
	private String email_other;

	// table linksus_relation_person_edu edu_type tinyint(3) COMMENT '教育类型:1 大学
	// 2 中学 3 小学 4 学前

	// 教育信息
	private String edu_info;
	// 教育类型
	private String edu_type;

	// 学校
	private String school_name;

	// 开始时间
	private String begin_year;

	// 结束时间
	private String end_year;

	// table linksus_relation_person_job edu_type tinyint(3) COMMENT '教育类型:1 大学
	// 2 中学 3 小学 4 学前

	// 公司信息
	private String job_info;
	// 公司名称
	private String company_name;

	// 开始时间
	private String wbegin_year;

	// 结束时间
	private String wend_year;

	// table linksus_relation_group、linksus_relation_person_group 分组

	// 分组信息
	private String group_info;
	// 分组名称
	private String group_name;

	// 分组编号
	private String group_id;

	// table linksus_relation_person_tag、linksus_relation_person_tagdef 标签

	// 标签信息
	private String tag_info;
	// 标签名称
	private String tag_name;

	// 标签编号
	private String tag_id;

	// table linksus_relation_person 人员主表

	// 新浪UIDS
	private String sina_uids;
	// 新浪昵称
	private String sina_names;

	// 腾讯UIDS
	private String tencent_uids;
	// 腾讯昵称
	private String tencent_names;

	// 微信UIDS
	private String weixin_infos;
	// 微信id
	private String weixin_id;
	// 微信 sex
	private String weixin_sex;
	// 微信账号所在国家
	private String weixin_country_code;
	// 微信账号所在省份/直辖市
	private String weixin_state_code;
	// 微信账号所在城市
	private String weixin_city_code;

	public String getPerson_name(){
		return person_name;
	}

	public void setPerson_name(String person_name){
		this.person_name = person_name;
	}

	public String getGender(){
		return gender;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getBirth_day(){
		return birth_day;
	}

	public void setBirth_day(String birth_day){
		this.birth_day = birth_day;
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getCountry_code(){
		return country_code;
	}

	public void setCountry_code(String country_code){
		this.country_code = country_code;
	}

	public String getState_code(){
		return state_code;
	}

	public void setState_code(String state_code){
		this.state_code = state_code;
	}

	public String getCity_code(){
		return city_code;
	}

	public void setCity_code(String city_code){
		this.city_code = city_code;
	}

	public String getCountry_name(){
		return country_name;
	}

	public void setCountry_name(String country_name){
		this.country_name = country_name;
	}

	public String getState_name(){
		return state_name;
	}

	public void setState_name(String state_name){
		this.state_name = state_name;
	}

	public String getCity_name(){
		return city_name;
	}

	public void setCity_name(String city_name){
		this.city_name = city_name;
	}

	public String getVerified_info(){
		return verified_info;
	}

	public void setVerified_info(String verified_info){
		this.verified_info = verified_info;
	}

	public String getContact_mobile(){
		return contact_mobile;
	}

	public void setContact_mobile(String contact_mobile){
		this.contact_mobile = contact_mobile;
	}

	public String getContact_tel(){
		return contact_tel;
	}

	public void setContact_tel(String contact_tel){
		this.contact_tel = contact_tel;
	}

	public String getContact_tel_office(){
		return contact_tel_office;
	}

	public void setContact_tel_office(String contact_tel_office){
		this.contact_tel_office = contact_tel_office;
	}

	public String getContact_tel_home(){
		return contact_tel_home;
	}

	public void setContact_tel_home(String contact_tel_home){
		this.contact_tel_home = contact_tel_home;
	}

	public String getContact_fax(){
		return contact_fax;
	}

	public void setContact_fax(String contact_fax){
		this.contact_fax = contact_fax;
	}

	public String getEmail_office(){
		return email_office;
	}

	public void setEmail_office(String email_office){
		this.email_office = email_office;
	}

	public String getEmail_personal(){
		return email_personal;
	}

	public void setEmail_personal(String email_personal){
		this.email_personal = email_personal;
	}

	public String getEmail_other(){
		return email_other;
	}

	public void setEmail_other(String email_other){
		this.email_other = email_other;
	}

	public String getEdu_info(){
		return edu_info;
	}

	public void setEdu_info(String edu_info){
		this.edu_info = edu_info;
	}

	public String getEdu_type(){
		return edu_type;
	}

	public void setEdu_type(String edu_type){
		this.edu_type = edu_type;
	}

	public String getSchool_name(){
		return school_name;
	}

	public void setSchool_name(String school_name){
		this.school_name = school_name;
	}

	public String getBegin_year(){
		return begin_year;
	}

	public void setBegin_year(String begin_year){
		this.begin_year = begin_year;
	}

	public String getEnd_year(){
		return end_year;
	}

	public void setEnd_year(String end_year){
		this.end_year = end_year;
	}

	public String getJob_info(){
		return job_info;
	}

	public void setJob_info(String job_info){
		this.job_info = job_info;
	}

	public String getCompany_name(){
		return company_name;
	}

	public void setCompany_name(String company_name){
		this.company_name = company_name;
	}

	public String getWbegin_year(){
		return wbegin_year;
	}

	public void setWbegin_year(String wbegin_year){
		this.wbegin_year = wbegin_year;
	}

	public String getWend_year(){
		return wend_year;
	}

	public void setWend_year(String wend_year){
		this.wend_year = wend_year;
	}

	public String getGroup_info(){
		return group_info;
	}

	public void setGroup_info(String group_info){
		this.group_info = group_info;
	}

	public String getGroup_name(){
		return group_name;
	}

	public void setGroup_name(String group_name){
		this.group_name = group_name;
	}

	public String getGroup_id(){
		return group_id;
	}

	public void setGroup_id(String group_id){
		this.group_id = group_id;
	}

	public String getTag_info(){
		return tag_info;
	}

	public void setTag_info(String tag_info){
		this.tag_info = tag_info;
	}

	public String getTag_name(){
		return tag_name;
	}

	public void setTag_name(String tag_name){
		this.tag_name = tag_name;
	}

	public String getTag_id(){
		return tag_id;
	}

	public void setTag_id(String tag_id){
		this.tag_id = tag_id;
	}

	public String getSina_uids(){
		return sina_uids;
	}

	public void setSina_uids(String sina_uids){
		this.sina_uids = sina_uids;
	}

	public String getSina_names(){
		return sina_names;
	}

	public void setSina_names(String sina_names){
		this.sina_names = sina_names;
	}

	public String getTencent_uids(){
		return tencent_uids;
	}

	public void setTencent_uids(String tencent_uids){
		this.tencent_uids = tencent_uids;
	}

	public String getTencent_names(){
		return tencent_names;
	}

	public void setTencent_names(String tencent_names){
		this.tencent_names = tencent_names;
	}

	public String getWeixin_infos(){
		return weixin_infos;
	}

	public void setWeixin_infos(String weixin_infos){
		this.weixin_infos = weixin_infos;
	}

	public String getWeixin_id(){
		return weixin_id;
	}

	public void setWeixin_id(String weixin_id){
		this.weixin_id = weixin_id;
	}

	public String getWeixin_sex(){
		return weixin_sex;
	}

	public void setWeixin_sex(String weixin_sex){
		this.weixin_sex = weixin_sex;
	}

	public String getWeixin_country_code(){
		return weixin_country_code;
	}

	public void setWeixin_country_code(String weixin_country_code){
		this.weixin_country_code = weixin_country_code;
	}

	public String getWeixin_state_code(){
		return weixin_state_code;
	}

	public void setWeixin_state_code(String weixin_state_code){
		this.weixin_state_code = weixin_state_code;
	}

	public String getWeixin_city_code(){
		return weixin_city_code;
	}

	public void setWeixin_city_code(String weixin_city_code){
		this.weixin_city_code = weixin_city_code;
	}

}
