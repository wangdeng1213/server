package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractAttentionReply;
import com.linksus.entity.LinksusInteractAttentionReplyAcct;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskAttentionUser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInstitutionService;
import com.linksus.service.LinksusInteractAttentionReplyAcctService;
import com.linksus.service.LinksusInteractAttentionReplyService;
import com.linksus.service.LinksusRelationPersonInfoService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskAttentionUserService;

public class TaskIncrementalAttentionAutoReply extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskIncrementalAttentionAutoReply.class);

	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusInstitutionService linksusInstitutionService = (LinksusInstitutionService) ContextUtil
			.getBean("linksusInstitutionService");
	LinksusRelationPersonInfoService linksusRelationPersonInfoService = (LinksusRelationPersonInfoService) ContextUtil
			.getBean("linksusRelationPersonInfoService");
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	LinksusTaskAttentionUserService linksusTaskAttentionUserService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");

	LinksusInteractAttentionReplyAcctService linksusInteractAttentionReplyAcctService = (LinksusInteractAttentionReplyAcctService) ContextUtil
			.getBean("linksusInteractAttentionReplyAcctService");

	LinksusInteractAttentionReplyService linksusInteractAttentionReplyService = (LinksusInteractAttentionReplyService) ContextUtil
			.getBean("linksusInteractAttentionReplyService");

	public static final void main(String[] args){
		TaskIncrementalAttentionAutoReply tm = new TaskIncrementalAttentionAutoReply();
		tm.attentionautoReply();

	}

	@Override
	public void cal(){
		attentionautoReply();
	}

	/**
	 * �����Զ���ע����
	 */
	public void attentionautoReply(){

		// ��ע�û��б�
		LinksusTaskAttentionUser attuser = new LinksusTaskAttentionUser();
		int startCount = (currentPage - 1) * pageSize;
		attuser.setPageSize(pageSize);
		attuser.setStartCount(startCount);
		// ��ȡ����/��Ѷ������ע�˺�
		List<LinksusTaskAttentionUser> attuserList = linksusTaskAttentionUserService.getAllAttentionByUserList(attuser);

		if(attuserList != null && attuserList.size() != 0){
			for(LinksusTaskAttentionUser entity : attuserList){
				//				if (entity != null) {
				if(entity.getAccountType() == 1){// ����
					// �����˺� ͨ��������ע�û������,��������
					this.sendSinaComment(entity);
				}
				if(entity.getAccountType() == 2){// ��Ѷ
					// ��Ѷ�˺� ͨ��������ע�û������,��������
					this.sendTencentComment(entity);
				}
				//				}
			}
		}
		checkTaskListEnd(attuserList);// �ж������Ƿ���ѯ���
	}

	// �����˺ŷ�������
	public Boolean sendSinaComment(LinksusTaskAttentionUser entity){

		// ��ȡ���������û�token
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(entity
				.getAccountId());
		LinksusInteractAttentionReplyAcct linksusInteractAttentionReplyAcct = linksusInteractAttentionReplyAcctService
				.getLinksusInteractAttentionReplyAcctByAccountId(entity.getAccountId());
		LinksusInteractAttentionReply linksusInteractAttentionReply = null;
		if(linksusInteractAttentionReplyAcct != null){
			linksusInteractAttentionReply = linksusInteractAttentionReplyService
					.getLinksusInteractAttentionReplyById(linksusInteractAttentionReplyAcct.getReplyId());
		}
		LinksusRelationWeibouser linksusRelationWeibouser = linksusRelationWeibouserService
				.getRelationWeibouserByUserId(entity.getUserId());
		try{
			// �жϸ��û�sinaд����
			int hcount = cache.getCurrHourWriteCountByUser(entity.getUserId().toString(),
					Constants.LIMIT_SEND_COMMENT_PER_HOUR);
			if(hcount == -1){// ��������
				return false;
			}
			int status = 3;
			// ����û�����			
			if(linksusAppaccount != null && linksusInteractAttentionReply != null){
				// ��ע�������
				if(linksusRelationWeibouser != null){
					/** ���۷������û�����һ��΢�� */
					// ��ȡӪ�������ǰ5ҳ΢���б�
					Map mapGetPara = new HashMap();
					mapGetPara.put("access_token", linksusAppaccount.getToken());
					mapGetPara.put("count", 5);
					mapGetPara.put("trim_user", 0);
					mapGetPara.put("uid", linksusRelationWeibouser.getRpsId());

					String weiboData = HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiBoData"), mapGetPara);
					if(StringUtils.isNotBlank(weiboData)){
						// ��ȡ������ȡ��������
						String statusesFromsina = JsonUtil.getNodeByName(weiboData, "statuses");
						if(!StringUtils.isBlank(statusesFromsina)){
							List<Map> statusesList = (List<Map>) JsonUtil.json2list(statusesFromsina, Map.class);
							if(statusesList != null && statusesList.size() > 0){
								// ȡ�÷��Ͷ��������΢��id
								String weiboId = (String) statusesList.get(0).get("idstr");
								// ���΢��������
								Map mapPara = new HashMap();
								mapPara.put("access_token", linksusAppaccount.getToken());
								mapPara.put("comment", linksusInteractAttentionReply.getContent());
								mapPara.put("id", weiboId);
								String strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("replyWeibo"), mapPara);// �������������Ϣ					
								LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
								if(error == null){// ������sina���ش������
									status = 2;
									// �����û�sina�������۴���
									cache.addCurrHourWriteCountByUser(entity.getUserId().toString(),
											Constants.LIMIT_SEND_COMMENT_PER_HOUR);
								}
							}
						}else{
							logger.debug(">>>>>>>>>>>>>>>��ע�ظ�:weiboData={}", weiboData);
						}
					}
				}
			}
			// ���¹�ע�����ִ��״̬
			entity.setStatus(status);
			linksusTaskAttentionUserService.updateLinksusTaskAttentionUser(entity);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return true;
	}

	// ��Ѷ�˺ŷ�������
	public Boolean sendTencentComment(LinksusTaskAttentionUser entity){

		// ��ȡ���������û�token
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(entity
				.getAccountId());
		LinksusInteractAttentionReplyAcct linksusInteractAttentionReplyAcct = linksusInteractAttentionReplyAcctService
				.getLinksusInteractAttentionReplyAcctByAccountId(entity.getAccountId());
		LinksusInteractAttentionReply linksusInteractAttentionReply = null;
		if(linksusInteractAttentionReplyAcct != null){
			linksusInteractAttentionReply = linksusInteractAttentionReplyService
					.getLinksusInteractAttentionReplyById(linksusInteractAttentionReplyAcct.getReplyId());
		}
		LinksusRelationWeibouser linksusRelationWeibouser = linksusRelationWeibouserService
				.getRelationWeibouserByUserId(entity.getUserId());
		int status = 3;
		try{
			// ����û�����
			if(linksusAppaccount != null && linksusInteractAttentionReply != null){
				// ��ע�������
				if(linksusRelationWeibouser != null){
					/** ���۷������û�����һ��΢�� */
					UrlEntity strurl = LoadConfig.getUrlEntity("TCUserLastWeiboID");
					Map paraMap = new HashMap();
					paraMap.put("access_token", linksusAppaccount.getToken());
					paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					paraMap.put("openid", linksusAppaccount.getAppid());
					paraMap.put("fopenid", linksusRelationWeibouser.getRpsId());
					paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
					paraMap.put("oauth_version", "2.a");
					paraMap.put("format", "json");
					paraMap.put("type", "3");
					paraMap.put("pageflag", 0);
					paraMap.put("pagetime", 0);
					paraMap.put("reqnum", 20);
					paraMap.put("lastid", 0);
					paraMap.put("contenttype", 0);
					boolean hasWeibo = true;
					String tencentRes = HttpUtil.getRequest(strurl, paraMap);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(tencentRes);
					if(error != null){
						hasWeibo = false;
					}else{
						//У��data�Ƿ�Ϊ��
						String data = JsonUtil.getNodeByName(tencentRes, "data");
						if(StringUtils.isBlank(data) || StringUtils.equals(data, "null")){
							hasWeibo = false;
						}
						//У��info�Ƿ�Ϊ��
						String info = JsonUtil.getNodeByName(data, "info");
						if(StringUtils.isBlank(info) || StringUtils.equals(info, "null")){
							hasWeibo = false;
						}
						if(hasWeibo){
							List<Map> statusesList = (List<Map>) JsonUtil.json2list(info, Map.class);
							if(statusesList != null && statusesList.size() > 0){
								// ȡ�÷��Ͷ��������΢��id
								String weiboId = (String) statusesList.get(0).get("id");
								// ���΢��������
								Map mapPara = new HashMap();
								mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO)
										.getAppKey());
								mapPara.put("openid", linksusAppaccount.getAppid());
								mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
								mapPara.put("access_token", linksusAppaccount.getToken());
								mapPara.put("oauth_version", "2.a");
								mapPara.put("format", "json");
								mapPara.put("content", linksusInteractAttentionReply.getContent());
								mapPara.put("reid", weiboId);
								String strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("TCreplyWeibo"),
										mapPara);// �������������Ϣ
								error = StringUtil.parseTencentErrorCode(strResult);
								if(error == null){
									status = 2;
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		// ���¹�ע�����ִ��״̬
		entity.setStatus(status);
		linksusTaskAttentionUserService.updateLinksusTaskAttentionUser(entity);
		return true;
	}
}