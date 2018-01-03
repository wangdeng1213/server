package com.linksus.common;

public class Constants{

	public static final String TASK_COMPANY_ACCOUNT = "companyAccount";
	public static final String TASK_AUTH_TOKEN = "authTOken";
	public static final String TASK_APP_ACCOUNT = "appAccount";
	/** �ͻ������������ݷ��ش���*/
	//public static final String NO_TASK_DATA= "no_task_data"; 
	public static final String CLIENT_CONFIG_INFO = "client_config_info";
	public static final String CLIENT_EXCEPTION_DATA = "client_exception_data";
	public static final String CLIENT_STATISTIC_DATA = "client_statistic_data";
	public static final String CLIENT_LIMIT_TIME = "client_limit_time";
	public static final String LOCAL_IP = "�����";
	/** ����˶˷��ؿͻ�����������*/
	public static final String RETRUN_DATA_TASK_LIST = "taskList";
	public static final String RETRUN_DATA_TOKEN_MAP = "tokenMap";
	/** ���˽ӿڷ������� */
	public static final String LIMIT_SEND_WEIBO_PER_HOUR = "sendWeiBoPerHour1";
	public static final String LIMIT_SEND_COMMENT_PER_HOUR = "sendCommentPerHour2";
	public static final String LIMIT_ADD_FOLLOWER_PER_HOUR = "addFollowerPerHour3";
	public static final String LIMIT_ADD_FOLLOWER_PER_DAY = "addFollowerPerDay4";
	/** ˽�Ŷ��еĻ������� */
	public static final String CACHE_SLAVE_MESSAGE_NAME = "cache_slave_message";

	public static final String APP_TYPE_SINA = "sina";
	public static final String APP_TYPE_TENCENT = "tencent";
	public static final String APP_TYPE_WEIXIN = "weixin";
	/** ����ִ��״̬ */
	public static final String TASK_STATUS_EXEC = "ִ����";
	public static final String TASK_STATUS_SLEEP = "������";
	public static final String TASK_STATUS_FINISH = "�����";
	public static final String TASK_STATUS_STOP = "��ͣ��";
	public static final String TASK_STATUS_USE = "������";
	public static final String TASK_STATUS_INVALID = "��Ч״̬";
	/** �ӿڵ���Ƶ�� */
	public static final String LIMIT_SINA_TOTAL_PER_HOUR = "sinaLimitPerHour";
	public static final String LIMIT_TENCENT_READ_PER_HOUR = "tencentReadLimitPerHour";
	public static final String LIMIT_TENCENT_WRITE_PER_HOUR = "tencentWriteLimitPerHour";
	/** ������������ */
	public static final String OPER_TYPE_INSERT = "insert";
	public static final String OPER_TYPE_UPDATE = "update";
	public static final String OPER_TYPE_UPDATE_INFO = "updateInfo";
	/** ���������� */
	public static final String IMG_URL_BASE = "http://www.wbcloud.com.cn";
	//public static final String TENCENT_APP_KEY="801058005";//"801380663";
	public static final String TENCENT_CLIENT_IP = "114.249.178.225";
	public static final String RECORD_GROUP_NAME = "RECORD_GROUP";
	public static final String RESPONSE_RECORD_GROUP_NAME = "RESPONSE_RECORD_GROUP";
	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
	public static final String STATUS_DELETE = "98";
	public static final String STATUS_PAUSE = "99";

	/** ΢������:ֱ��*/
	public static final String WEIBO_TYPE_RECORD = "1";
	/** ΢������:ת��*/
	public static final String WEIBO_TYPE_RESPONSE = "2";

	/** ΢����������:��������*/
	public static final String WEIBO_SEND_TYPE_IMME = "1";
	/** ΢����������:��ʱ����*/
	public static final String WEIBO_SEND_TYPE_TIMER = "2";
	/** �������� */
	public static final String ERROR_TYPE_SYSTEM = "0";
	public static final String ERROR_TYPE_SINA = "1";
	public static final String INVALID_RECORD_EXCEPTION = "EXCEPTION";
	/** ������� */
	public static final String CACHE_ERROR_CODE = "error_code_map";
	public static final String CACHE_ERROR_CODE_SINA = "sina_error_code_map";
	public static final String CACHE_ERROR_CODE_TENCENT = "tencent_error_code_map";
	public static final String CACHE_ERROR_CODE_NULL = "10014";
	public static final String CACHE_SINA_AREA_CODE = "sina_area_map";
	public static final String CACHE_TENCENT_AREA_CODE = "tencent_area_map";
	public static final String CACHE_WEIXIN_AREA_CODE = "weixin_area_map";
	public static final String CACHE_SINA_APPINFO = "cache_sina_appinfo";
	public static final String CACHE_TENCENT_APPINFO = "cache_tencent_appinfo";
	public static final String CACHE_TASK_STATISTIC_MODEL = "cache_task_statistic_model";
	public static final String CACHE_LOCAL_IP_ADDR = "cache_local_ip_addr";
	public static final String CACHE_COOKIE_MAP = "cache_cookie_map";
	/** ��Ч�������� 1 ΢������ 2 Ӫ��@ 3 Ӫ������ 4 ����΢�� 5 ����˽�� 6 ����΢��7���ӹ�ע8ȡ����ע9�Ƴ���˿10�������¼*/
	public static final int INVALID_RECORD_SEND_WEIBO = 1;
	public static final int INVALID_RECORD_MARKETING_AT = 2;
	public static final int INVALID_RECORD_MARKETING_COMMENT = 3;
	public static final int INVALID_RECORD_INTERACT_WEIBO = 4;
	public static final int INVALID_RECORD_INTERACT_MESSAGE = 5;
	public static final int INVALID_RECORD_INTERACT_WEIXIN = 6;
	public static final int INVALID_RECORD_ADD_ATTENTION = 7;
	public static final int INVALID_RECORD_CANCLE_ATTENTION = 8;
	public static final int INVALID_RECORD_REMOVE_FANS = 9;
	public static final int INVALID_RECORD_INFO = 10;
	/** �û����ڵ�Ϊ�����������*/
	public static final String PROVINCE_CODE = "100";
	public static final String CITY_CODE = "1000";

	public static final String QUEUE_FLASH_STATISTIC = "queueFlushStatistic";
	/** ΢������ */
	public static final String WEIXINTEXTTYPE = "text";//�ı�����
	public static final String WEIXINIMAGETYPE = "image";//ͼƬ����
	public static final String WEIXINVOICETYPE = "voice";//��������
	public static final String WEIXINVIDEOTYPE = "video";//��Ƶ����
	public static final String WEIXINLOCATIONTYPE = "location";//����λ������
	public static final String WEIXINLINKTYPE = "link";//��������
	public static final String WEIXINEVENTTYPE = "event";//��Ϣ����	
	public static final String WEIXINEVENTSUB = "subscribe";//����
	public static final String WEIXINEVENTUNSUB = "unsubscribe";//ȡ������
	public static final String WEIXINEVENTCLICK = "CLICK";//�Զ���˵��¼�����
	public static final String WEIXINEVENTMASS = "MASSSENDJOBFINISH";//΢��Ⱥ����Ϣ�ظ��¼�

	public static final int TASK_EXCPTION_TYPE_SERVER = 2;
	public static final int TASK_EXCPTION_TYPE_CLIENT = 1;

	public static final Integer DEFAULTORG = 0; //�����Ĭ����֯ID

	public static Integer ROOTORGID = 1; //Ĭ����֯���ڵ�ID(��������gid)

	/** ������������  �������*/
	public static final String REQUEST_TYPE_TASK_LIST = "task_list";
	public static final String REMOTE_QUEUE_TASK = "remote_queue";
	/** ������������  ��������*/
	public static final String REQUEST_TYPE_DATA_RETURN = "data_return";

	/**����״̬��*/
	public static final String MESSAGENOTEXISTENT = "26403,20305";//˽�Ų�����
	public static final String COMMENTOTEXISTENT = "20201,20101";//���۲�����
	public static final String WEIBONOTEXISTENT = "20101";//΢��������

	/**��Ѷ״̬��*/
	public static final String TCWEIBONOTEXISTENT = "4_6,4_11";//���ڵ㲻����

	/**΢��״̬��*/
	public static final String WECHATNOTEXISTENT = "43004";

}
