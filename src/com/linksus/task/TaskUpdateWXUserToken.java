package com.linksus.task;

import java.util.Calendar;
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
import com.linksus.entity.LinksusSourceAppaccount;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusSourceAppaccountService;

public class TaskUpdateWXUserToken extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskUpdateWXUserToken.class);

	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusSourceAppaccountService linksusSourceAppaccountService = (LinksusSourceAppaccountService) ContextUtil
			.getBean("linksusSourceAppaccountService");

	public static void main(String[] args){

		//		TaskUpdateWXUserToken dd = new TaskUpdateWXUserToken();
		//		dd.cal();
		//		System.exit(0);
	}

	@Override
	public void cal(){
		LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
		int startCount = (currentPage - 1) * pageSize;
		linksusAppaccount.setPageSize(pageSize);
		linksusAppaccount.setStartCount(startCount);
		List<LinksusAppaccount> wxAppaccounts = linksusAppaccountService.getLinksusAppaccountWXUser(linksusAppaccount);
		if(wxAppaccounts != null){
			for(LinksusAppaccount wxAppaccount : wxAppaccounts){
				updateWXUserToken(wxAppaccount);
			}
		}
		checkTaskListEnd(wxAppaccounts);//�ж������Ƿ���ѯ���
		setTaskListEndTime(wxAppaccounts.size());
	}

	//����΢���û�token
	public void updateWXUserToken(LinksusAppaccount wxAppaccount){
		UrlEntity strUrlUser = LoadConfig.getUrlEntity("getwxUserToken");
		Map paraMap = new HashMap();
		paraMap.put("grant_type", "client_credential");
		paraMap.put("appid", wxAppaccount.getAppKey());
		paraMap.put("secret", wxAppaccount.getAppSecrect());
		String resultinfo = HttpUtil.getRequest(strUrlUser, paraMap);
		if(StringUtils.isNotBlank(resultinfo) && JsonUtil.getNodeValueByName(resultinfo, "errcode") == null){//�����޴�����Ϣ
			String token = JsonUtil.getNodeByName(resultinfo, "access_token");
			//���µ�ǰ�û�token
			wxAppaccount.setToken(token.replace("\"", ""));
			//��ȡtoken��Чʱ��
			Calendar cal = Calendar.getInstance();
			String lastUpdateTime = String.valueOf(cal.getTime().getTime() / 1000);
			cal.add(Calendar.HOUR, 2);
			String tokenTime = String.valueOf(cal.getTime().getTime() / 1000);
			wxAppaccount.setTokenEtime(Integer.parseInt(tokenTime));
			wxAppaccount.setLastUpdTime(Integer.parseInt(lastUpdateTime));
			linksusAppaccountService.updateWXUserToken(wxAppaccount);

			//�����Զ�����Դ���ʺŹ�������Ϣ��token������ʱ�䡢token����ʱ��
			LinksusSourceAppaccount sourceAppaccount = new LinksusSourceAppaccount();
			sourceAppaccount.setAccountId(wxAppaccount.getId());
			sourceAppaccount.setToken(token.replace("\"", ""));
			sourceAppaccount.setTokenEtime(Integer.parseInt(tokenTime));
			sourceAppaccount.setLastUpdTime(Integer.parseInt(lastUpdateTime));
			linksusSourceAppaccountService.updateAppaccountToken(sourceAppaccount);
		}
	}

	public Map getWxUserToken(String appKey,String secret){
		UrlEntity strUrlUser = LoadConfig.getUrlEntity("getwxUserToken");
		Map rsMap = new HashMap();
		Map paraMap = new HashMap();
		paraMap.put("grant_type", "client_credential");
		paraMap.put("appid", appKey);
		paraMap.put("secret", secret);
		String resultinfo = HttpUtil.getRequest(strUrlUser, paraMap);
		if(StringUtils.isNotBlank(resultinfo) && JsonUtil.getNodeValueByName(resultinfo, "errcode") == null){//�����޴�����Ϣ
			String token = JsonUtil.getNodeValueByName(resultinfo, "access_token");
			//���µ�ǰ�û�token
			//��ȡtoken��Чʱ��
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.HOUR, 2);
			cal.getTime();
			String tokenTime = String.valueOf(cal.getTime().getTime() / 1000);
			Integer etime = Integer.parseInt(tokenTime);
			rsMap.put("access_token", token);
			rsMap.put("token_etime", etime);
		}else{//���ڴ�����Ϣ
			rsMap.put("errcode", "#΢�Ż�ȡ��Ȩʧ��:" + resultinfo);
			return rsMap;
		}
		return rsMap;
	}

}
