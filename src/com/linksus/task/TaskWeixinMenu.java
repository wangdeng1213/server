package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractWxMenuAcct;
import com.linksus.entity.LinksusInteractWxMenuItem;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractWxMenuAcctService;
import com.linksus.service.LinksusInteractWxMenuItemService;

/**
 * 创建自定义菜单
 * 
 */

public class TaskWeixinMenu{

	protected static final Logger logger = LoggerFactory.getLogger(TaskWeixinMenu.class);

	LinksusInteractWxMenuAcctService linksusInteractWxMenuAcctService = (LinksusInteractWxMenuAcctService) ContextUtil
			.getBean("linksusInteractWxMenuAcctService");
	LinksusInteractWxMenuItemService linksusInteractWxMenuItemService = (LinksusInteractWxMenuItemService) ContextUtil
			.getBean("linksusInteractWxMenuItemService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	//通过端口创建自定义菜单
	public String createWeixinMenu(Long accountId){
		String resStr = "";
		//获取平台账号创建菜单id
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById(accountId);
		if(appcount != null){
			//删除该用户的自定义菜单
			Map paramMap = new HashMap();
			paramMap.put("access_token", appcount.getToken());
			String deleteData = HttpUtil.getRequest(LoadConfig.getUrlEntity("deleteWeixinMenu"), paramMap);
			if(StringUtils.isNotBlank(deleteData) && JsonUtil.getNodeValueByName(deleteData, "errcode").equals("0")){//成功
				logger.info("-----------------删除微信用户{}自定义菜单-------------", appcount.getAppid());
			}
			//创建用户自定义菜单
			LinksusInteractWxMenuAcct wxMenuAcct = linksusInteractWxMenuAcctService
					.getLinksusInteractWxMenuAcctByAccountId(accountId);
			String content = "";
			if(wxMenuAcct != null){
				//根据菜单ID获取菜单内容
				List<LinksusInteractWxMenuItem> menus = linksusInteractWxMenuItemService
						.getLinksusInteractWxMenuItemByMenuId(wxMenuAcct.getMenuId());
				if(menus != null && menus.size() > 0){
					content = "{\"button\":[";
					String firstContent = "";
					String secondContent = "";
					for(LinksusInteractWxMenuItem wxMenuItem : menus){
						//获取一级菜单内容  
						if(wxMenuItem.getItemType() == 1){//click类型
							firstContent = firstContent + "{\"type\":\"click\",\"name\":\"" + wxMenuItem.getItemName()
									+ "\",\"key\":\"" + wxMenuItem.getPid() + "\"},";

						}else if(wxMenuItem.getItemType() == 2){//view类型
							firstContent = firstContent + "{\"type\":\"view\",\"name\":\"" + wxMenuItem.getItemName()
									+ "\",\"url\":\"" + wxMenuItem.getRedirectUrl() + "\"},";
						}
						//查询该一级菜单是否有二级菜单
						List<LinksusInteractWxMenuItem> linksusInteractWxMenus = linksusInteractWxMenuItemService
								.getLinksusInteractWxMenuItemByPrarentId(wxMenuItem.getPid());
						if(linksusInteractWxMenus != null && linksusInteractWxMenus.size() > 0){
							secondContent = "{" + "\"name\":\"" + wxMenuItem.getItemName() + "\",\"sub_button\":[";
							for(int j = 0; j < linksusInteractWxMenus.size(); j++){
								LinksusInteractWxMenuItem linksusInteractWxSecondMenu = linksusInteractWxMenus.get(j);
								if(j == linksusInteractWxMenus.size() - 1){
									if(linksusInteractWxSecondMenu.getItemType() == 1){
										secondContent = secondContent + "{\"type\":\"click\",\"name\":\""
												+ linksusInteractWxSecondMenu.getItemName() + "\",\"key\":\""
												+ linksusInteractWxSecondMenu.getPid() + "\"}]},";
									}else{
										secondContent = secondContent + "{\"type\":\"view\",\"name\":\""
												+ linksusInteractWxSecondMenu.getItemName() + "\"," + "\"url\":\""
												+ linksusInteractWxSecondMenu.getRedirectUrl() + "\"}]},";
									}
								}else{
									if(linksusInteractWxSecondMenu.getItemType() == 1){
										secondContent = secondContent + "{\"type\":\"click\",\"name\":\""
												+ linksusInteractWxSecondMenu.getItemName() + "\",\"key\":\""
												+ linksusInteractWxSecondMenu.getPid() + "\"},";
									}else{
										secondContent = secondContent + "{\"type\":\"view\",\"name\":\""
												+ linksusInteractWxSecondMenu.getItemName() + "\"," + "\"url\":\""
												+ linksusInteractWxSecondMenu.getRedirectUrl() + "\"},";
									}
								}
							}
						}
					}
					if(StringUtils.isNotBlank(secondContent)){
						content = content + firstContent + secondContent.substring(0, secondContent.length() - 1);
					}else{
						content = content + firstContent.substring(0, firstContent.length() - 1)
								+ secondContent.substring(0, secondContent.length() - 1);
					}
					content = content + "]}";
				}else{
					return "20020";
				}

			}else{
				String noMenuContentData = HttpUtil.getRequest(LoadConfig.getUrlEntity("deleteWeixinMenu"), paramMap);
				//删除自定义菜单
				if(StringUtils.isNotBlank(noMenuContentData)
						&& JsonUtil.getNodeValueByName(noMenuContentData, "errcode").equals("0")){//成功
					logger.info("<<<<<<<<<<<<<<删除无定义的自定义菜单成功<<<<<<<<<<<<<");
				}else{
					logger.info("<<<<<<<<<<<<<<删除无定义的自定义菜单失败<<<<<<<<<<<<<");
				}
			}
			if(StringUtils.isNotBlank(content)){
				//发送创建请求
				String createData = HttpUtil.postBodyRequest(LoadConfig.getUrlEntity("createWeixinMenu"),
						"access_token=" + appcount.getToken(), content);
				if(StringUtils.isNotBlank(createData)){
					String errcode = JsonUtil.getNodeValueByName(createData, "errcode");
					if(errcode.equals("0")){
						logger.info("<<<<<<<<<<<<<<创建成功<<<<<<<<<<<<<");
						return "sucess";
					}else{
						logger.info("<<<<<<<<<<<<<<创建失败<<<<<<<<<<<<<");
						return "#" + createData;
					}
				}
			}
		}else{
			return "20006";
		}
		return resStr;
	}

}
