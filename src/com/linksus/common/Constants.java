package com.linksus.common;

public class Constants{

	public static final String TASK_COMPANY_ACCOUNT = "companyAccount";
	public static final String TASK_AUTH_TOKEN = "authTOken";
	public static final String TASK_APP_ACCOUNT = "appAccount";
	/** 客户端无任务数据返回代码*/
	//public static final String NO_TASK_DATA= "no_task_data"; 
	public static final String CLIENT_CONFIG_INFO = "client_config_info";
	public static final String CLIENT_EXCEPTION_DATA = "client_exception_data";
	public static final String CLIENT_STATISTIC_DATA = "client_statistic_data";
	public static final String CLIENT_LIMIT_TIME = "client_limit_time";
	public static final String LOCAL_IP = "服务端";
	/** 服务端端返回客户端数据类型*/
	public static final String RETRUN_DATA_TASK_LIST = "taskList";
	public static final String RETRUN_DATA_TOKEN_MAP = "tokenMap";
	/** 新浪接口访问限制 */
	public static final String LIMIT_SEND_WEIBO_PER_HOUR = "sendWeiBoPerHour1";
	public static final String LIMIT_SEND_COMMENT_PER_HOUR = "sendCommentPerHour2";
	public static final String LIMIT_ADD_FOLLOWER_PER_HOUR = "addFollowerPerHour3";
	public static final String LIMIT_ADD_FOLLOWER_PER_DAY = "addFollowerPerDay4";
	/** 私信队列的缓存名称 */
	public static final String CACHE_SLAVE_MESSAGE_NAME = "cache_slave_message";

	public static final String APP_TYPE_SINA = "sina";
	public static final String APP_TYPE_TENCENT = "tencent";
	public static final String APP_TYPE_WEIXIN = "weixin";
	/** 任务执行状态 */
	public static final String TASK_STATUS_EXEC = "执行中";
	public static final String TASK_STATUS_SLEEP = "休眠中";
	public static final String TASK_STATUS_FINISH = "已完成";
	public static final String TASK_STATUS_STOP = "已停用";
	public static final String TASK_STATUS_USE = "启用中";
	public static final String TASK_STATUS_INVALID = "无效状态";
	/** 接口调用频次 */
	public static final String LIMIT_SINA_TOTAL_PER_HOUR = "sinaLimitPerHour";
	public static final String LIMIT_TENCENT_READ_PER_HOUR = "tencentReadLimitPerHour";
	public static final String LIMIT_TENCENT_WRITE_PER_HOUR = "tencentWriteLimitPerHour";
	/** 批量操作类型 */
	public static final String OPER_TYPE_INSERT = "insert";
	public static final String OPER_TYPE_UPDATE = "update";
	public static final String OPER_TYPE_UPDATE_INFO = "updateInfo";
	/** 批量任务常量 */
	public static final String IMG_URL_BASE = "http://www.wbcloud.com.cn";
	//public static final String TENCENT_APP_KEY="801058005";//"801380663";
	public static final String TENCENT_CLIENT_IP = "114.249.178.225";
	public static final String RECORD_GROUP_NAME = "RECORD_GROUP";
	public static final String RESPONSE_RECORD_GROUP_NAME = "RESPONSE_RECORD_GROUP";
	public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmss";
	public static final String STATUS_DELETE = "98";
	public static final String STATUS_PAUSE = "99";

	/** 微博类型:直发*/
	public static final String WEIBO_TYPE_RECORD = "1";
	/** 微博类型:转发*/
	public static final String WEIBO_TYPE_RESPONSE = "2";

	/** 微博发布类型:立即发布*/
	public static final String WEIBO_SEND_TYPE_IMME = "1";
	/** 微博发布类型:定时发布*/
	public static final String WEIBO_SEND_TYPE_TIMER = "2";
	/** 错误类型 */
	public static final String ERROR_TYPE_SYSTEM = "0";
	public static final String ERROR_TYPE_SINA = "1";
	public static final String INVALID_RECORD_EXCEPTION = "EXCEPTION";
	/** 缓存代码 */
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
	/** 无效操作类型 1 微博发布 2 营销@ 3 营销评论 4 互动微博 5 互动私信 6 互动微信7增加关注8取消关注9移除粉丝10仅插入记录*/
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
	/** 用户所在地为其他情况处理*/
	public static final String PROVINCE_CODE = "100";
	public static final String CITY_CODE = "1000";

	public static final String QUEUE_FLASH_STATISTIC = "queueFlushStatistic";
	/** 微信类型 */
	public static final String WEIXINTEXTTYPE = "text";//文本类型
	public static final String WEIXINIMAGETYPE = "image";//图片类型
	public static final String WEIXINVOICETYPE = "voice";//语音类型
	public static final String WEIXINVIDEOTYPE = "video";//视频类型
	public static final String WEIXINLOCATIONTYPE = "location";//地理位置类型
	public static final String WEIXINLINKTYPE = "link";//链接类型
	public static final String WEIXINEVENTTYPE = "event";//消息类型	
	public static final String WEIXINEVENTSUB = "subscribe";//订阅
	public static final String WEIXINEVENTUNSUB = "unsubscribe";//取消订阅
	public static final String WEIXINEVENTCLICK = "CLICK";//自定义菜单事件推送
	public static final String WEIXINEVENTMASS = "MASSSENDJOBFINISH";//微信群发消息回复事件

	public static final int TASK_EXCPTION_TYPE_SERVER = 2;
	public static final int TASK_EXCPTION_TYPE_CLIENT = 1;

	public static final Integer DEFAULTORG = 0; //政务版默认组织ID

	public static Integer ROOTORGID = 1; //默认组织根节点ID(问政银川gid)

	/** 连接请求类型  任务队列*/
	public static final String REQUEST_TYPE_TASK_LIST = "task_list";
	public static final String REMOTE_QUEUE_TASK = "remote_queue";
	/** 连接请求类型  返回数据*/
	public static final String REQUEST_TYPE_DATA_RETURN = "data_return";

	/**新浪状态码*/
	public static final String MESSAGENOTEXISTENT = "26403,20305";//私信不存在
	public static final String COMMENTOTEXISTENT = "20201,20101";//评论不存在
	public static final String WEIBONOTEXISTENT = "20101";//微博不存在

	/**腾讯状态码*/
	public static final String TCWEIBONOTEXISTENT = "4_6,4_11";//父节点不存在

	/**微信状态码*/
	public static final String WECHATNOTEXISTENT = "43004";

}
