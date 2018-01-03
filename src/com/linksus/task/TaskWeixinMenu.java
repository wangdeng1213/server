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
 * �����Զ���˵�
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

	//ͨ���˿ڴ����Զ���˵�
	public String createWeixinMenu(Long accountId){
		String resStr = "";
		//��ȡƽ̨�˺Ŵ����˵�id
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById(accountId);
		if(appcount != null){
			//ɾ�����û����Զ���˵�
			Map paramMap = new HashMap();
			paramMap.put("access_token", appcount.getToken());
			String deleteData = HttpUtil.getRequest(LoadConfig.getUrlEntity("deleteWeixinMenu"), paramMap);
			if(StringUtils.isNotBlank(deleteData) && JsonUtil.getNodeValueByName(deleteData, "errcode").equals("0")){//�ɹ�
				logger.info("-----------------ɾ��΢���û�{}�Զ���˵�-------------", appcount.getAppid());
			}
			//�����û��Զ���˵�
			LinksusInteractWxMenuAcct wxMenuAcct = linksusInteractWxMenuAcctService
					.getLinksusInteractWxMenuAcctByAccountId(accountId);
			String content = "";
			if(wxMenuAcct != null){
				//���ݲ˵�ID��ȡ�˵�����
				List<LinksusInteractWxMenuItem> menus = linksusInteractWxMenuItemService
						.getLinksusInteractWxMenuItemByMenuId(wxMenuAcct.getMenuId());
				if(menus != null && menus.size() > 0){
					content = "{\"button\":[";
					String firstContent = "";
					String secondContent = "";
					for(LinksusInteractWxMenuItem wxMenuItem : menus){
						//��ȡһ���˵�����  
						if(wxMenuItem.getItemType() == 1){//click����
							firstContent = firstContent + "{\"type\":\"click\",\"name\":\"" + wxMenuItem.getItemName()
									+ "\",\"key\":\"" + wxMenuItem.getPid() + "\"},";

						}else if(wxMenuItem.getItemType() == 2){//view����
							firstContent = firstContent + "{\"type\":\"view\",\"name\":\"" + wxMenuItem.getItemName()
									+ "\",\"url\":\"" + wxMenuItem.getRedirectUrl() + "\"},";
						}
						//��ѯ��һ���˵��Ƿ��ж����˵�
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
				//ɾ���Զ���˵�
				if(StringUtils.isNotBlank(noMenuContentData)
						&& JsonUtil.getNodeValueByName(noMenuContentData, "errcode").equals("0")){//�ɹ�
					logger.info("<<<<<<<<<<<<<<ɾ���޶�����Զ���˵��ɹ�<<<<<<<<<<<<<");
				}else{
					logger.info("<<<<<<<<<<<<<<ɾ���޶�����Զ���˵�ʧ��<<<<<<<<<<<<<");
				}
			}
			if(StringUtils.isNotBlank(content)){
				//���ʹ�������
				String createData = HttpUtil.postBodyRequest(LoadConfig.getUrlEntity("createWeixinMenu"),
						"access_token=" + appcount.getToken(), content);
				if(StringUtils.isNotBlank(createData)){
					String errcode = JsonUtil.getNodeValueByName(createData, "errcode");
					if(errcode.equals("0")){
						logger.info("<<<<<<<<<<<<<<�����ɹ�<<<<<<<<<<<<<");
						return "sucess";
					}else{
						logger.info("<<<<<<<<<<<<<<����ʧ��<<<<<<<<<<<<<");
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
