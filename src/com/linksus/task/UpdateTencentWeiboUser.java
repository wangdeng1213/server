package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.CopyMapToBean;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 更新腾讯微博用户
 */
public class UpdateTencentWeiboUser extends BaseTask{

	private CacheUtil cache = CacheUtil.getInstance();

	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	private LinksusAppaccountService accountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	@Override
	public void cal(){
		// 从服务器端采用分页取数据
		LinksusRelationWeibouser linksusWeibouser = new LinksusRelationWeibouser();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeibouser.setStartCount(startCount);
		linksusWeibouser.setPageSize(pageSize);
		List<LinksusRelationWeibouser> accountUser = relationWeiboUserService
				.getLinksusTencentWeibouserList(linksusWeibouser);
		if(accountUser != null && accountUser.size() > 0){
			for(LinksusRelationWeibouser userRecord : accountUser){
				updateUserInfo(userRecord);
			}
		}
		checkTaskListEnd(accountUser);//判断任务是否轮询完成
	}

	//更新微博用户信息
	public void updateUserInfo(LinksusRelationWeibouser userRecord){
		try{
			//获取腾讯微博用户信息
			UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCUerInfo");
			Map paraMap = new HashMap();
			paraMap.put("oauth_consumer_key", "801378249");
			paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			paraMap.put("oauth_version", "2.a");
			paraMap.put("format", "json");
			paraMap.put("openid", "8445D0EED5565CC6E1B0D0B6614F7301");
			paraMap.put("access_token", "cdeaa7bd96eb7af7616b2203b0abed19");
			paraMap.put("name", "Z690184115");
			String strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
			String data = JsonUtil.getNodeValueByName(strjson, "data");
			Map infoMap = JsonUtil.json2map(data, Map.class);
			LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
			CopyMapToBean.copyTencentUserMapToBean(infoMap, linkWeiboUser);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strjson);
			if(error != null){
				return;
			}
			//String data = JsonUtil.getNodeValueByName(strjson, "data");
			if(StringUtils.equals(data, "null")){
				return;
			}
			Map commonStrMap = JsonUtil.json2map(data, Map.class);
			String rpsId = String.valueOf(commonStrMap.get("openid"));
			LinksusRelationWeibouser user = new LinksusRelationWeibouser();
			user.setRpsId(rpsId);
			user.setUserId(userRecord.getUserId());
			//更新用户信息表中的rpsId 字段
			relationWeiboUserService.updateLinksusRelationWeibouserById(user);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception{
		UpdateTencentWeiboUser user = new UpdateTencentWeiboUser();
		LinksusRelationWeibouser userreco = new LinksusRelationWeibouser();
		userreco.setRpsName("wangdeng1213");
		user.updateUserInfo(userreco);
	}
}
