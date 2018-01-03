package com.linksus.entity;

public class PersonInfo{

	// table linksus_relation_person_info
	// ����
	private String person_name;
	// �Ա�
	private String gender;

	// ����
	private String birth_day;
	// ���ڵ�����
	private String location;
	// ���ڹ��Ҵ���
	private String country_code;

	// ���ڵ���/ʡ�ݴ���
	private String state_code;
	// ���ڳ��д���
	private String city_code;

	// ���ڹ���
	private String country_name;

	// ���ڵ���/ʡ��
	private String state_name;
	// ���ڳ���
	private String city_name;

	// ��֤��Ϣ
	private String verified_info;

	// table linksus_relation_person_contact contact_type tinyint(3) COMMENT
	// '��ϵ��ʽ����:1 �ƶ��绰 2 �̶��绰 2 �칫�绰 4 ��ͥ�绰 5 ������� 6 �������� 10 �������� 11 ˽������ 12 ��������

	// �ƶ��绰
	private String contact_mobile;
	// �̶��绰
	private String contact_tel;

	// �칫�绰
	private String contact_tel_office;
	// ��ͥ�绰
	private String contact_tel_home;

	// �������
	private String contact_fax;

	// ��������
	private String email_office;
	// ˽������
	private String email_personal;

	// ��������
	private String email_other;

	// table linksus_relation_person_edu edu_type tinyint(3) COMMENT '��������:1 ��ѧ
	// 2 ��ѧ 3 Сѧ 4 ѧǰ

	// ������Ϣ
	private String edu_info;
	// ��������
	private String edu_type;

	// ѧУ
	private String school_name;

	// ��ʼʱ��
	private String begin_year;

	// ����ʱ��
	private String end_year;

	// table linksus_relation_person_job edu_type tinyint(3) COMMENT '��������:1 ��ѧ
	// 2 ��ѧ 3 Сѧ 4 ѧǰ

	// ��˾��Ϣ
	private String job_info;
	// ��˾����
	private String company_name;

	// ��ʼʱ��
	private String wbegin_year;

	// ����ʱ��
	private String wend_year;

	// table linksus_relation_group��linksus_relation_person_group ����

	// ������Ϣ
	private String group_info;
	// ��������
	private String group_name;

	// ������
	private String group_id;

	// table linksus_relation_person_tag��linksus_relation_person_tagdef ��ǩ

	// ��ǩ��Ϣ
	private String tag_info;
	// ��ǩ����
	private String tag_name;

	// ��ǩ���
	private String tag_id;

	// table linksus_relation_person ��Ա����

	// ����UIDS
	private String sina_uids;
	// �����ǳ�
	private String sina_names;

	// ��ѶUIDS
	private String tencent_uids;
	// ��Ѷ�ǳ�
	private String tencent_names;

	// ΢��UIDS
	private String weixin_infos;
	// ΢��id
	private String weixin_id;
	// ΢�� sex
	private String weixin_sex;
	// ΢���˺����ڹ���
	private String weixin_country_code;
	// ΢���˺�����ʡ��/ֱϽ��
	private String weixin_state_code;
	// ΢���˺����ڳ���
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
